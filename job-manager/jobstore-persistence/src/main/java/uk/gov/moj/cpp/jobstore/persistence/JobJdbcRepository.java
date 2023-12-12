package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static java.time.ZonedDateTime.now;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static javax.json.Json.createReader;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.fromSqlTimestamp;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.justice.services.common.converter.ZonedDateTimes;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.sql.DataSource;

import org.slf4j.Logger;

@ApplicationScoped
public class JobJdbcRepository implements JobRepository {

    private static final String INSERT_JOB_SQL = "INSERT INTO job(job_id,worker_id,worker_lock_time,next_task,next_task_start_time,job_data) values (?,?,?,?,?,to_jsonb(?::json))";
    private static final String UPDATE_JOB_DATA_SQL = "UPDATE job SET job_data = to_jsonb(?::json) WHERE job_id = ?";
    private static final String UPDATE_NEXT_TASK_DETAILS_SQL = "UPDATE job set next_task= ?, next_task_start_time= ? where job_id= ? ";
    private static final String DELETE_JOB_SQL = "DELETE from job where job_id= ? ";
    private static final String RELEASE_JOB_SQL = "UPDATE job set worker_id= null, worker_lock_time= null where job_id= ? ";
    private static final String JOBS_LOCKED_TO_SQL = "SELECT job_id, job_data, worker_id, worker_lock_time, next_task, next_task_start_time from job WHERE worker_id= ?";

    private static final String LOCK_JOBS_SQL = "UPDATE job set worker_id= ? , worker_lock_time= ? where job_id in " +
            "(select job_id from job where (worker_id is null or worker_lock_time < ?) and next_task_start_time < ? limit ? for update) " +
            "and (worker_id is null or worker_lock_time < ?)";

    protected DataSource dataSource;

    @Inject
    protected PreparedStatementWrapperFactory preparedStatementWrapperFactory;

    @Inject
    protected JdbcResultSetStreamer jdbcResultSetStreamer;

    @Inject
    protected Logger logger;

    @Inject
    private JdbcJobStoreDataSourceProvider jdbcJobStoreDataSourceProvider;

    @PostConstruct
    private void initialiseDataSource() {
        dataSource = jdbcJobStoreDataSourceProvider.getDataSource();
    }

    @Override
    public void insertJob(final Job job) {
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, INSERT_JOB_SQL)) {
            ps.setObject(1, job.getJobId());
            ps.setObject(2, job.getWorkerId().orElse(null));
            ps.setTimestamp(3, convertToTimestamp(job.getWorkerLockTime()));
            ps.setString(4, job.getNextTask());
            ps.setTimestamp(5, convertToTimestamp(job.getNextTaskStartTime()));
            ps.setString(6, job.getJobData().toString());
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error storing job to the database", e);
            throw new JdbcRepositoryException(format("Exception while storing job with id %s",
                    job.getJobId()), e);
        }
    }

    @Override
    public void updateJobData(final UUID jobId, final JsonObject jobData) {
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, UPDATE_JOB_DATA_SQL)) {
            ps.setString(1, jobData.toString());
            ps.setObject(2, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error updating task data for the job", e);
            throw new JdbcRepositoryException(format("Exception while storing task data job with job id %s", jobId), e);
        }
    }

    @Override
    public void updateNextTaskDetails(final UUID jobId, final String nextTask, final Timestamp nextTaskStartTime) {
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, UPDATE_NEXT_TASK_DETAILS_SQL)) {
            ps.setObject(1, nextTask);
            ps.setTimestamp(2, nextTaskStartTime);
            ps.setObject(3, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error updating next task details to the job", e);
            throw new JdbcRepositoryException(format("Exception while storing next task details job with job id %s", jobId), e);
        }
    }

    @Override
    public void lockJobsFor(final UUID workerId, final int jobCountToLock) {
        logger.debug("Locking jobs for worker: {}", workerId);

        ZonedDateTime now = now();
        Timestamp oneHourAgo = toSqlTimestamp((now.minusHours(1)));

        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, LOCK_JOBS_SQL)) {
            ps.setObject(1, workerId);
            ps.setTimestamp(2, toSqlTimestamp(now));
            ps.setTimestamp(3, oneHourAgo);
            ps.setTimestamp(4, toSqlTimestamp(now));
            ps.setLong(5, valueOf(jobCountToLock));
            ps.setTimestamp(6, oneHourAgo);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error locking jobs", e);
            throw new JdbcRepositoryException(format("Exception while locking jobs for with worker id %s", workerId), e);
        }
    }

    @Override
    public Stream<Job> findJobsLockedTo(final UUID workerId) {
        try {
            final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, JOBS_LOCKED_TO_SQL);
            ps.setObject(1, workerId);
            return jdbcResultSetStreamer.streamOf(ps, entityFromFunction());
        } catch (SQLException e) {
            logger.error("Error retrieving locked jobs for workerId " + workerId, e);
            throw new JdbcRepositoryException(format("Exception while retrieving jobs locked to worker id %s", workerId), e);
        }
    }

    @Override
    public void deleteJob(final UUID jobId) {
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, DELETE_JOB_SQL)) {
            ps.setObject(1, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error deleting job", e);
            throw new JdbcRepositoryException(format("Exception while deleting next task job with job id %s", jobId), e);
        }
    }

    @Override
    public void releaseJob(final UUID jobId) {
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(dataSource, RELEASE_JOB_SQL)) {
            ps.setObject(1, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error(format("Error releasing the job %s", jobId), e);
            throw new JdbcRepositoryException(format("Error releasing the job %s", jobId), e);
        }
    }

    protected Function<ResultSet, Job> entityFromFunction() {
        return resultSet -> {
            try {
                return new Job(
                        fromString(resultSet.getString("job_id")),
                        toJsonObject(resultSet.getString("job_data")),
                        resultSet.getString("next_task"),
                        getZoneDateTime(resultSet, "next_task_start_time"),
                        of(getUUID(resultSet, "worker_id")),
                        of(getZoneDateTime(resultSet, "worker_lock_time")));
            } catch (final SQLException e) {
                throw new JdbcRepositoryException("Unexpected SQLException mapping ResultSet to Job instance", e);
            }
        };
    }

    protected ZonedDateTime getZoneDateTime(final ResultSet resultSet, final String column) throws SQLException {
        final Timestamp timestamp = resultSet.getTimestamp(column);
        return timestamp == null ? null : fromSqlTimestamp(timestamp);
    }

    protected UUID getUUID(final ResultSet resultSet, final String column) throws SQLException {
        final String uuid = resultSet.getString(column);
        return uuid == null ? null : UUID.fromString(uuid);
    }

    private Timestamp convertToTimestamp(final Optional<ZonedDateTime> date) {
        return date
                .map(ZonedDateTimes::toSqlTimestamp)
                .orElse(null);
    }

    private Timestamp convertToTimestamp(final ZonedDateTime date) {
        return ZonedDateTimes.toSqlTimestamp(date);
    }

    private JsonObject toJsonObject(final String json) {
        try (final JsonReader reader = createReader(new StringReader(json))) {
            return reader.readObject();
        }
    }
}
