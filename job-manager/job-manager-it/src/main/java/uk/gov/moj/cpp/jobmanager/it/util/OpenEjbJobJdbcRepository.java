package uk.gov.moj.cpp.jobmanager.it.util;

import static java.lang.String.format;
import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.justice.services.test.utils.core.messaging.Poller;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobJdbcRepository;
import uk.gov.moj.cpp.jobstore.persistence.PreparedStatementWrapper;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.sql.DataSource;

public class OpenEjbJobJdbcRepository extends JobJdbcRepository {

    private static final String SQL_DELETE_PATTERN = "DELETE FROM %s";
    private static final String JOBS_PROCESSED_COUNT = "SELECT COUNT(*) FROM job where worker_id is not null and worker_lock_time > ?";
    private static final String JOBS_NOTPROCESSED_COUNT = "SELECT COUNT(*) FROM job where worker_id is null and worker_lock_time is null";
    private static final String JOBS_PROCESSED = "SELECT * FROM job where worker_id is not null and worker_lock_time > ?";
    private static final String JOBS_PROCESSED_FOR_WORKER = "SELECT * FROM job where worker_id = ?";
    private static final String JOB_DATA_JSON = "{\"some\": \"json\"}";

    public void waitForAllJobsToBeProcessed() {

        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        final Poller poller = new Poller(100, 500);
        poller.pollUntilFound(() -> {
            final PreparedStatementWrapper ps;
            try {
                ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, "SELECT COUNT(*) FROM job");
                final ResultSet rs = ps.executeQuery();
                return rs.next() ? Optional.empty() : Optional.of(true);
            } catch (SQLException e) {
                return Optional.of(false);
            }
        });
    }

    public void cleanJobTables() throws SQLException {

        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        final String sql = format(SQL_DELETE_PATTERN, "job");
        try (Connection connection = jobStoreDataSource.getConnection()) {
            executeDelete(sql, connection);
        }
    }

    private void executeDelete(String sql, Connection connection) throws SQLException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
    }

    public int jobsProcessed() throws SQLException {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, JOBS_PROCESSED_COUNT);
        ps.setTimestamp(1, toSqlTimestamp(now().minus(30, ChronoUnit.SECONDS)));
        final ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public int jobsNotProcessed() throws SQLException {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, JOBS_NOTPROCESSED_COUNT);
        final ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public Stream<Job> getProcessedRecords() throws SQLException {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, JOBS_PROCESSED);
        ps.setTimestamp(1, toSqlTimestamp(now().minus(30, ChronoUnit.SECONDS)));
        ps.executeQuery();
        return jdbcResultSetStreamer.streamOf(ps, mapAssignedJobFromResultSet());
    }

    public Stream<Job> getProcessedRecords(final UUID workerId) throws SQLException {
        final DataSource jobStoreDataSource = jobStoreDataSourceProvider.getJobStoreDataSource();
        final PreparedStatementWrapper ps = preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSource, JOBS_PROCESSED_FOR_WORKER);
        ps.setObject(1, workerId);
        ps.executeQuery();
        return jdbcResultSetStreamer.streamOf(ps, mapAssignedJobFromResultSet());
    }

    public void createJobs(final int count) {
        createJobsWith(count, "sample-task", now(), empty(), empty());
    }

    public void createIdleJobs(final int count, final Optional<ZonedDateTime> workerLockTime, final ZonedDateTime nextTaskStartTime) {
        createJobsWith(count, "nextTask", nextTaskStartTime, of(randomUUID()), workerLockTime);
    }

    private void createJobsWith(int count, String nextTask, ZonedDateTime nextTaskStartTime, Optional<UUID> workerId, Optional<ZonedDateTime> workerLockTime) {
        for (int i = 0; i < count; i++) {
            final Job job = new Job(randomUUID(), jobData(JOB_DATA_JSON), nextTask, nextTaskStartTime, workerId, workerLockTime, 0, HIGH);
            insertJob(job);
        }
    }

    private JsonObject jobData(final String json) {
        try (JsonReader jsonReader = getJsonReaderFactory().createReader(new StringReader(json))) {
            return jsonReader.readObject();
        }
    }

    public void collectDuplicates(final Set<UUID> jobs, final List<UUID> duplicates, final Map<UUID, List<Job>> workersToJobs) {
        workersToJobs.forEach((key, jobsForAWorker) -> {
            for (final Job job : jobsForAWorker) {
                final boolean added = jobs.add(job.getJobId());
                if (!added) {
                    duplicates.add(job.getJobId());
                }
            }
        });
    }

}