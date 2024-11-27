package uk.gov.moj.cpp.jobstore.persistence;

import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static java.time.ZonedDateTime.now;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static javax.json.Json.createReader;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.fromSqlTimestamp;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.justice.framework.libraries.datasource.providers.jobstore.JobStoreDataSourceProvider;
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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.sql.DataSource;

import org.slf4j.Logger;

@ApplicationScoped
public class JobJdbcRepository implements JobRepository {

    private static final String INSERT_JOB_SQL = "INSERT INTO job(job_id,worker_id,worker_lock_time,next_task,next_task_start_time,job_data,retry_attempts_remaining,priority) values (?,?,?,?,?,to_jsonb(?::json),?,?)";
    private static final String UPDATE_JOB_DATA_SQL = "UPDATE job SET job_data = to_jsonb(?::json) WHERE job_id = ?";
    private static final String UPDATE_NEXT_TASK_DETAILS_SQL = "UPDATE job set next_task= ?, next_task_start_time= ?, retry_attempts_remaining= ? where job_id= ? ";
    private static final String UPDATE_NEXT_TASK_RETRY_DETAILS_SQL = "UPDATE job set next_task_start_time= ?, retry_attempts_remaining= ? where job_id= ? ";
    private static final String DELETE_JOB_SQL = "DELETE from job where job_id= ? ";
    private static final String RELEASE_JOB_SQL = "UPDATE job set worker_id= null, worker_lock_time= null where job_id= ? ";
    private static final String JOBS_LOCKED_TO_SQL = "SELECT job_id, job_data, worker_id, worker_lock_time, next_task, next_task_start_time, retry_attempts_remaining, priority from job WHERE worker_id= ?";

    private static final String LOCK_JOBS_SQL = """
            UPDATE job SET worker_id= ? , worker_lock_time= ? WHERE job_id in
            (SELECT
                job_id
                FROM job
                WHERE (worker_id IS NULL OR worker_lock_time < ?)
                AND priority = ?
                AND next_task_start_time < ?
                LIMIT GREATEST(LEAST(? - (SELECT COUNT(*) FROM job WHERE worker_id IS NOT NULL AND worker_lock_time > ?), ?), 0)
                FOR UPDATE SKIP LOCKED)
            AND (worker_id IS NULL OR worker_lock_time < ?)
            """;

    @Inject
    protected PreparedStatementWrapperFactory preparedStatementWrapperFactory;

    @Inject
    protected JdbcResultSetStreamer jdbcResultSetStreamer;

    @Inject
    protected Logger logger;

    @Inject
    protected JobStoreDataSourceProvider jobStoreDataSourceProvider;


    @Override
    public void insertJob(final Job job) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, INSERT_JOB_SQL)) {
            ps.setObject(1, job.getJobId());
            ps.setObject(2, job.getWorkerId().orElse(null));
            ps.setTimestamp(3, convertToTimestamp(job.getWorkerLockTime()));
            ps.setString(4, job.getNextTask());
            ps.setTimestamp(5, convertToTimestamp(job.getNextTaskStartTime()));
            ps.setString(6, job.getJobData().toString());
            ps.setInt(7, job.getRetryAttemptsRemaining());
            ps.setString(8, job.getPriority().name());
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error storing job to the database", e);
            throw new JdbcRepositoryException(format("Exception while storing job with id %s",
                    job.getJobId()), e);
        }
    }

    @Override
    public void updateJobData(final UUID jobId, final JsonObject jobData) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, UPDATE_JOB_DATA_SQL)) {
            ps.setString(1, jobData.toString());
            ps.setObject(2, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error updating task data for the job", e);
            throw new JdbcRepositoryException(format("Exception while storing task data job with job id %s", jobId), e);
        }
    }

    @Override
    public void updateNextTaskDetails(final UUID jobId, final String nextTask, final Timestamp nextTaskStartTime, final Integer retryAttemptsRemaining) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, UPDATE_NEXT_TASK_DETAILS_SQL)) {
            ps.setObject(1, nextTask);
            ps.setTimestamp(2, nextTaskStartTime);
            ps.setObject(3, retryAttemptsRemaining);
            ps.setObject(4, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error updating next task details to the job", e);
            throw new JdbcRepositoryException(format("Exception while storing next task details job with job id %s", jobId), e);
        }
    }

    @Override
    public void updateNextTaskRetryDetails(final UUID jobId, final Timestamp nextTaskStartTime, final Integer retryAttemptsRemaining) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, UPDATE_NEXT_TASK_RETRY_DETAILS_SQL)) {
            ps.setTimestamp(1, nextTaskStartTime);
            ps.setObject(2, retryAttemptsRemaining);
            ps.setObject(3, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error updating next task retry details to the job", e);
            throw new JdbcRepositoryException(format("Exception while storing next task retry details job with job id %s", jobId), e);
        }
    }

    @Override
    public int lockJobsFor(final UUID workerId, final Priority priority, final int inProgressJobCountLimit, final int jobCountToLock) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        logger.debug("Locking jobs for worker: {}", workerId);

        final ZonedDateTime now = now();
        final Timestamp oneHourAgo = toSqlTimestamp((now.minusHours(1)));

        try (final PreparedStatementWrapper preparedStatementWrapper = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, LOCK_JOBS_SQL)) {
            preparedStatementWrapper.setObject(1, workerId);
            preparedStatementWrapper.setTimestamp(2, toSqlTimestamp(now));
            preparedStatementWrapper.setTimestamp(3, oneHourAgo);
            preparedStatementWrapper.setString(4, priority.toString());
            preparedStatementWrapper.setTimestamp(5, toSqlTimestamp(now));
            preparedStatementWrapper.setLong(6, valueOf(inProgressJobCountLimit));
            preparedStatementWrapper.setTimestamp(7, oneHourAgo);
            preparedStatementWrapper.setLong(8, valueOf(jobCountToLock));
            preparedStatementWrapper.setTimestamp(9, oneHourAgo);
            return preparedStatementWrapper.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error locking jobs", e);
            throw new JdbcRepositoryException(format("Exception while locking jobs for with worker id %s", workerId), e);
        }
    }

    @Override
    public Stream<Job> findJobsLockedTo(final UUID workerId) {

        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try {
            final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, JOBS_LOCKED_TO_SQL);
            ps.setObject(1, workerId);
            return jdbcResultSetStreamer.streamOf(ps, mapAssignedJobFromResultSet());
        } catch (SQLException e) {
            logger.error("Error retrieving locked jobs for workerId " + workerId, e);
            throw new JdbcRepositoryException(format("Exception while retrieving jobs locked to worker id %s", workerId), e);
        }
    }

    @Override
    public void deleteJob(final UUID jobId) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, DELETE_JOB_SQL)) {
            ps.setObject(1, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error("Error deleting job", e);
            throw new JdbcRepositoryException(format("Exception while deleting next task job with job id %s", jobId), e);
        }
    }

    @Override
    public void releaseJob(final UUID jobId) {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        try (final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, RELEASE_JOB_SQL)) {
            ps.setObject(1, jobId);
            ps.executeUpdate();
        } catch (final SQLException e) {
            logger.error(format("Error releasing the job %s", jobId), e);
            throw new JdbcRepositoryException(format("Error releasing the job %s", jobId), e);
        }
    }

    protected Function<ResultSet, Job> mapAssignedJobFromResultSet() {
        return resultSet -> {
            try {
                return new Job(
                        fromString(resultSet.getString("job_id")),
                        toJsonObject(resultSet.getString("job_data")),
                        resultSet.getString("next_task"),
                        getZoneDateTime(resultSet, "next_task_start_time"),
                        of(getUUID(resultSet, "worker_id")),
                        of(getZoneDateTime(resultSet, "worker_lock_time")),
                        resultSet.getInt("retry_attempts_remaining"),
                        Priority.valueOf(resultSet.getString("priority")));
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
