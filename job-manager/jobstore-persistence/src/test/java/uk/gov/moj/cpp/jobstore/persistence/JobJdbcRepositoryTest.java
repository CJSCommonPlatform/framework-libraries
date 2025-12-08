package uk.gov.moj.cpp.jobstore.persistence;

import static java.time.ZonedDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.justice.framework.libraries.datasource.providers.jobstore.JobStoreDataSourceProvider;
import uk.gov.justice.framework.libraries.datasource.providers.jobstore.TestJobStoreDataSourceProvider;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;
import uk.gov.justice.services.test.utils.core.messaging.Poller;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class JobJdbcRepositoryTest {

    private static final String LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML = "liquibase/jobstore-db-changelog.xml";
    private static final String JOBS_COUNT = "SELECT COUNT(*) FROM job";
    private static final String JOB_DATA_JSON = "{\"some\": \"json\"}";

    private final JobStoreDataSourceProvider jobStoreDataSourceProvider = new TestJobStoreDataSourceProvider();
    private final JobJdbcRepository jdbcRepository = new JobJdbcRepository();

    @BeforeEach
    public void createJdbcRepository() throws Exception {

        try (final Connection connection = jobStoreDataSourceProvider.getJobStoreDataSource().getConnection()) {
            new LiquibaseDatabaseBootstrapper().bootstrap(LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML, connection);
        }
        jdbcRepository.jobStoreDataSourceProvider = jobStoreDataSourceProvider;
        jdbcRepository.logger = mock(Logger.class);
        jdbcRepository.preparedStatementWrapperFactory = new PreparedStatementWrapperFactory();
        jdbcRepository.jdbcResultSetStreamer = new JdbcResultSetStreamer();
        checkIfReady();
    }

    private void checkIfReady() {
        final Poller poller = new Poller();

        poller.pollUntilFound(() -> {
            try {
                jobStoreDataSourceProvider.getJobStoreDataSource().getConnection().prepareStatement(JOBS_COUNT).execute();
                return of("Success");
            } catch (final SQLException e) {
                e.printStackTrace();
                fail("Job store construction failed");
                return empty();
            }
        });
    }

    @Test
    public void shouldAddEmailNotificationWithMandatoryDataOnly() {

        final int retryAttemptsRemaining = 0;
        final Job job = new Job(randomUUID(), jobData(JOB_DATA_JSON), "nextTask", now(), empty(), empty(), retryAttemptsRemaining, MEDIUM);

        jdbcRepository.insertJob(job);

        final int jobsCount = jobsCount();
        assertThat(jobsCount, is(1));
    }

    @Test
    public void shouldInsertJob() throws Exception {
        final Job job = new Job(
                randomUUID(),
                jobData(JOB_DATA_JSON),
                "nextTask",
                now(),
                Optional.of(randomUUID()),
                Optional.of(now()),
                1,
                MEDIUM);

        jdbcRepository.insertJob(job);

        final Job insertedJob = getJobById(job.getJobId());
        assertThat(insertedJob.getJobId(), is(job.getJobId()));
        assertThat(insertedJob.getJobData(), is(job.getJobData()));
        assertThat(insertedJob.getNextTask(), is(job.getNextTask()));
        assertTrue(insertedJob.getNextTaskStartTime().truncatedTo(MILLIS).isEqual(job.getNextTaskStartTime().truncatedTo(MILLIS)));
        assertThat(insertedJob.getWorkerId(), is(job.getWorkerId()));
        assertTrue(insertedJob.getWorkerLockTime().get().truncatedTo(MILLIS).isEqual(job.getWorkerLockTime().get().truncatedTo(MILLIS)));
        assertThat(insertedJob.getRetryAttemptsRemaining(), is(job.getRetryAttemptsRemaining()));
        assertThat(insertedJob.getPriority(), is(job.getPriority()));
    }

    @Test
    public void shouldAddEmailNotificationWithMandatoryAndOptionalData() {
        final UUID jobId1 = randomUUID();
        final UUID jobId2 = randomUUID();

        final Job job1 = new Job(jobId1, jobData(JOB_DATA_JSON), "nextTask", now(), of(randomUUID()), of(now()), 0, LOW);

        jdbcRepository.insertJob(job1);

        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), of(randomUUID()), of(now()), 0, HIGH);
        jdbcRepository.insertJob(job2);

        final int jobsCount = jobsCount();
        assertThat(jobsCount, is(2));

    }

    @Test
    public void shouldUpdateJobData() {

        final UUID jobId = randomUUID();
        final String jobDataBeforeUpdate = "{\"some\": \"json before update\"}";
        final String jobDataAfterUpdate = "{\"some\": \"json after update\"}";
        final UUID workerId = randomUUID();
        final Job job1 = new Job(jobId, jobData(jobDataBeforeUpdate), "nextTask", now(), of(workerId), of(now()), 0, HIGH);

        jdbcRepository.insertJob(job1);
        jdbcRepository.updateJobData(jobId, jobData(jobDataAfterUpdate));

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId).collect(toList());
        assertThat(jobs.size(), is(1));
        assertThat(jobs.get(0).getJobData(), is(jobData(jobDataAfterUpdate)));

    }

    @Test
    public void shouldUpdateNextTask() {

        final UUID jobId = randomUUID();
        final String nextTaskBeforeUpdate = "Next Task Before Update";
        final String nextTaskAfterUpdate = "Next Task After Update";
        final Integer retryAttemptsRemaining = 1;

        final ZonedDateTime nextTaskStartTimeBeforeUpdate = new UtcClock().now().minusHours(2).truncatedTo(MILLIS);
        final ZonedDateTime nextTaskStartTimeAfterUpdate = new UtcClock().now().truncatedTo(MILLIS);

        final Optional<UUID> workerId = of(randomUUID());
        final Job job1 = new Job(jobId, jobData(JOB_DATA_JSON), nextTaskBeforeUpdate, nextTaskStartTimeBeforeUpdate, workerId, of(now()), 0, HIGH);

        jdbcRepository.insertJob(job1);
        jdbcRepository.updateNextTaskDetails(jobId, nextTaskAfterUpdate, toSqlTimestamp(nextTaskStartTimeAfterUpdate), retryAttemptsRemaining);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId.get()).toList();
        assertThat(jobs.size(), is(1));
        assertThat(jobs.get(0).getNextTask(), is(nextTaskAfterUpdate));
        assertThat(jobs.get(0).getNextTaskStartTime(), is(nextTaskStartTimeAfterUpdate));
        assertThat(jobs.get(0).getRetryAttemptsRemaining(), is(retryAttemptsRemaining));
    }

    @Test
    public void shouldUpdateNextTaskRetryDetails() {
        final UUID jobId = randomUUID();
        final String nextTask = "Next Task Before Update";
        final Integer retryAttemptsRemaining = 1;

        final ZonedDateTime nextTaskStartTimeBeforeUpdate = new UtcClock().now().minusHours(2).truncatedTo(MILLIS);
        final ZonedDateTime nextTaskStartTimeAfterUpdate = new UtcClock().now().truncatedTo(MILLIS);

        final Optional<UUID> workerId = of(randomUUID());
        final Job job1 = new Job(jobId, jobData(JOB_DATA_JSON), nextTask, nextTaskStartTimeBeforeUpdate, workerId, of(now()), 0, HIGH);

        jdbcRepository.insertJob(job1);
        jdbcRepository.updateNextTaskRetryDetails(jobId, toSqlTimestamp(nextTaskStartTimeAfterUpdate), retryAttemptsRemaining);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId.get()).toList();
        assertThat(jobs.size(), is(1));
        assertThat(jobs.get(0).getNextTask(), is(nextTask));
        assertThat(jobs.get(0).getNextTaskStartTime(), is(nextTaskStartTimeAfterUpdate));
        assertThat(jobs.get(0).getRetryAttemptsRemaining(), is(retryAttemptsRemaining));
    }

    @Test
    public void shouldLockJobsToWorker() throws SQLException {
        createJobs(10);
        final UUID workerId = randomUUID();

        jdbcRepository.lockJobsFor(workerId, HIGH, 4);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId).collect(toList());
        assertThat(jobs.size(), is(4));
    }

    @Test
    public void shouldFindLockedJobsToWorker() throws Exception {
        final UUID jobId = randomUUID();
        final UUID jobId2 = randomUUID();
        final UUID worker = randomUUID();

        final Job job = new Job(jobId, jobData(JOB_DATA_JSON), "nextTask", now(), of(worker), of(now()), 0, HIGH);
        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), empty(), empty(), 0, HIGH);

        jdbcRepository.insertJob(job);
        jdbcRepository.insertJob(job2);

        assertThat(jobsCount(), is(2));

        final List<Job> preTestJobs = jdbcRepository.findJobsLockedTo(worker).collect(toList());
        assertThat(preTestJobs.size(), is(1));

        jdbcRepository.lockJobsFor(worker, HIGH, 10);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(worker).collect(toList());

        assertThat(jobs.size(), is(2));

        assertThat(jobs.get(0).getWorkerId(), is(of(worker)));

        assertThat(jobs.get(1).getWorkerId(), is(of(worker)));
    }

    @Test
    public void shouldReleaseJob() {
        final UUID jobId1 = randomUUID();
        final Optional<UUID> workerId = of(randomUUID());

        final Job job1 = new Job(jobId1, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()), 0, HIGH);
        jdbcRepository.insertJob(job1);
        final UUID jobId2 = randomUUID();

        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()), 0, HIGH);
        jdbcRepository.insertJob(job2);
        jdbcRepository.releaseJob(jobId1);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId.get()).collect(toList());
        assertThat(jobs.size(), is(1));

        assertThat(jobs.get(0).getWorkerId(), is(workerId));
        assertThat(jobs.get(0).getJobId(), is(jobId2));

        final int jobsCount = jobsCount();
        assertThat(jobsCount, is(2));
    }

    @Test
    public void shouldDeleteJob() {

        final UUID jobId1 = randomUUID();
        final Optional<UUID> workerId = of(randomUUID());
        final Job job1 = new Job(jobId1, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()), 0, HIGH);

        jdbcRepository.insertJob(job1);

        final UUID jobId2 = randomUUID();
        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()), 0, HIGH);
        jdbcRepository.insertJob(job2);
        jdbcRepository.deleteJob(jobId1);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId.get()).collect(toList());
        assertThat(jobs.size(), is(1));
        assertThat(jobs.get(0).getWorkerId().get(), is(workerId.get()));
        assertThat(jobs.get(0).getJobId(), is(jobId2));

        final int jobsCount = jobsCount();
        assertThat(jobsCount, is(1));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenCreating() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;

        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.insertJob(mock(Job.class)));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenDeleting() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.deleteJob(randomUUID()));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenUpdatingJobData() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.updateJobData(randomUUID(), mock(JsonObject.class)));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenUpdatingNextTaskDetails() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.updateNextTaskDetails(randomUUID(), "string", mock(Timestamp.class), 1));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenUpdatingNextTaskRetryDetails() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.updateNextTaskRetryDetails(randomUUID(), mock(Timestamp.class), 1));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenLockingJobs() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.lockJobsFor(randomUUID(), HIGH, 2));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWheoFindingJobsLockedTo() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.findJobsLockedTo(randomUUID()));
    }

    @Test
    public void shouldThrowJdbcRepositoryExceptionWhenReleaseingJob() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        assertThrows(JdbcRepositoryException.class, () -> jdbcRepository.releaseJob(randomUUID()));
    }

    private Job getJobById(UUID jobId) throws SQLException {
        final PreparedStatementWrapper ps = new PreparedStatementWrapperFactory().preparedStatementWrapperOf(jobStoreDataSourceProvider.getJobStoreDataSource(), "select * from job where job_id = ?");
        ps.setObject(1, jobId);
        return new JdbcResultSetStreamer().streamOf(ps, jdbcRepository.mapAssignedJobFromResultSet()).findFirst().get();
    }

    private void createJobs(final int count) {
        int i = 0;
        while (i < count) {

            final Job job = new Job(randomUUID(), jobData(JOB_DATA_JSON), "nextTask", now(), empty(), empty(), 0, HIGH);
            jdbcRepository.insertJob(job);
            i++;

        }
    }

    private int jobsCount() {
        int jobsCount = 0;
        try {
            final PreparedStatementWrapper ps = jdbcRepository.preparedStatementWrapperFactory.preparedStatementWrapperOf(jobStoreDataSourceProvider.getJobStoreDataSource(), JOBS_COUNT);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jobsCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new JdbcRepositoryException("Exception while retrieving jobs count", e);
        }
        return jobsCount;
    }

    private JsonObject jobData(final String json) {
        return getJsonReaderFactory().createReader(new StringReader(json)).readObject();
    }
}
