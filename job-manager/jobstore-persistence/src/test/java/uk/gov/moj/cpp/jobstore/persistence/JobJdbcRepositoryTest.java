package uk.gov.moj.cpp.jobstore.persistence;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static javax.json.Json.createReader;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

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
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class JobJdbcRepositoryTest {

    private static final String LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML = "liquibase-files/jobstore-db-changelog.xml";
    private static final String JOBS_COUNT = "SELECT COUNT(*) FROM job";
    private static final String JOB_DATA_JSON = "{\"some\": \"json\"}";

    private final DataSource eventStoreDataSource = new PostgresDataSourceFactory().createJobStoreDataSource();
    private final JobJdbcRepository jdbcRepository = new JobJdbcRepository();

    @Before
    public void runLiquibase() throws Exception {
        try (final Connection connection = eventStoreDataSource.getConnection()) {
            new LiquibaseDatabaseBootstrapper().bootstrap(LIQUIBASE_JOB_STORE_DB_CHANGELOG_XML, connection);
        }
    }

    @Before
    public void createJdbcRepository() throws Exception {

        jdbcRepository.dataSource = eventStoreDataSource;
        jdbcRepository.logger = mock(Logger.class);
        jdbcRepository.preparedStatementWrapperFactory = new PreparedStatementWrapperFactory();
        jdbcRepository.jdbcResultSetStreamer = new JdbcResultSetStreamer();
        jdbcRepository.jobSqlProvider = new PostgresJobSqlProvider();
        checkIfReady();
    }

    private void checkIfReady() {
        final Poller poller = new Poller();

        poller.pollUntilFound(() -> {
            try {
                jdbcRepository.dataSource.getConnection().prepareStatement(JOBS_COUNT).execute();
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

        final Job job = new Job(randomUUID(), jobData(JOB_DATA_JSON), "nextTask", now(), empty(), empty());

        jdbcRepository.insertJob(job);

        final int jobsCount = jobsCount();
        assertThat(jobsCount, is(1));
    }

    @Test
    public void shouldAddEmailNotificationWithMandatoryAndOptionalData() {
        final UUID jobId1 = randomUUID();
        final UUID jobId2 = randomUUID();

        final Job job1 = new Job(jobId1, jobData(JOB_DATA_JSON), "nextTask", now(), of(randomUUID()), of(now()));

        jdbcRepository.insertJob(job1);

        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), of(randomUUID()), of(now()));
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
        final Job job1 = new Job(jobId, jobData(jobDataBeforeUpdate), "nextTask", now(), of(workerId), of(now()));

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

        final ZonedDateTime nextTaskStartTimeBeforeUpdate = new UtcClock().now().minusHours(2);
        final ZonedDateTime nextTaskStartTimeAfterUpdate = new UtcClock().now();

        final Optional<UUID> workerId = of(randomUUID());
        final Job job1 = new Job(jobId, jobData(JOB_DATA_JSON), nextTaskBeforeUpdate, nextTaskStartTimeBeforeUpdate, workerId, of(now()));

        jdbcRepository.insertJob(job1);
        jdbcRepository.updateNextTaskDetails(jobId, nextTaskAfterUpdate, toSqlTimestamp(nextTaskStartTimeAfterUpdate));

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId.get()).collect(Collectors.toList());
        assertThat(jobs.size(), is(1));
        assertThat(jobs.get(0).getNextTask(), is(nextTaskAfterUpdate));
        assertThat(jobs.get(0).getNextTaskStartTime(), is(nextTaskStartTimeAfterUpdate));
    }

    @Test
    public void shouldLockJobsToWorker() throws SQLException {
        createJobs(10);
        final UUID workerId = randomUUID();

        jdbcRepository.lockJobsFor(workerId, 4);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId).collect(toList());
        assertThat(jobs.size(), is(4));
    }

    @Test
    public void shouldFindLockedJobsToWorker() throws Exception {
        final UUID jobId = randomUUID();
        final UUID jobId2 = randomUUID();
        final UUID worker = randomUUID();

        final Job job = new Job(jobId, jobData(JOB_DATA_JSON), "nextTask", now(), of(worker), of(now()));
        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), empty(), empty());

        jdbcRepository.insertJob(job);
        jdbcRepository.insertJob(job2);

        assertThat(jobsCount(), is(2));

        final List<Job> preTestJobs = jdbcRepository.findJobsLockedTo(worker).collect(toList());
        assertThat(preTestJobs.size(), is(1));

        jdbcRepository.lockJobsFor(worker, 10);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(worker).collect(toList());

        assertThat(jobs.size(), is(2));

        assertThat(jobs.get(0).getWorkerId(), is(of(worker)));

        assertThat(jobs.get(1).getWorkerId(), is(of(worker)));
    }

    @Test
    public void shouldReleaseJob() {
        final UUID jobId1 = randomUUID();
        final Optional<UUID> workerId = of(randomUUID());

        final Job job1 = new Job(jobId1, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()));
        jdbcRepository.insertJob(job1);
        final UUID jobId2 = randomUUID();

        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()));
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
        final Job job1 = new Job(jobId1, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()));

        jdbcRepository.insertJob(job1);

        final UUID jobId2 = randomUUID();
        final Job job2 = new Job(jobId2, jobData(JOB_DATA_JSON), "nextTask", now(), workerId, of(now()));
        jdbcRepository.insertJob(job2);
        jdbcRepository.deleteJob(jobId1);

        final List<Job> jobs = jdbcRepository.findJobsLockedTo(workerId.get()).collect(toList());
        assertThat(jobs.size(), is(1));
        assertThat(jobs.get(0).getWorkerId().get(), is(workerId.get()));
        assertThat(jobs.get(0).getJobId(), is(jobId2));

        final int jobsCount = jobsCount();
        assertThat(jobsCount, is(1));
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWhenCreating() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.insertJob(mock(Job.class));
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWhenDeleting() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.deleteJob(randomUUID());
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWhenUpdatingJobData() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.updateJobData(randomUUID(), mock(JsonObject.class));
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWhenUpdatingNextTaskDetails() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.updateNextTaskDetails(randomUUID(), "string", mock(Timestamp.class));
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWhenLockingJobs() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.lockJobsFor(randomUUID(), 2);
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWheoFindingJobsLockedTo() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.findJobsLockedTo(randomUUID());
    }

    @Test(expected = JdbcRepositoryException.class)
    public void shouldThrowJdbcRepositoryExceptionWhenReleaseingJob() throws SQLException {
        final PreparedStatementWrapperFactory preparedStatementWrapperFactory = mock(PreparedStatementWrapperFactory.class);
        when(preparedStatementWrapperFactory.preparedStatementWrapperOf(any(), any())).thenThrow(SQLException.class);
        jdbcRepository.preparedStatementWrapperFactory = preparedStatementWrapperFactory;
        jdbcRepository.releaseJob(randomUUID());
    }

    private void createJobs(final int count) {
        int i = 0;
        while (i < count) {

            final Job job = new Job(randomUUID(), jobData(JOB_DATA_JSON), "nextTask", now(), empty(), empty());
            jdbcRepository.insertJob(job);
            i++;

        }
    }

    private int jobsCount() {
        int jobsCount = 0;
        try {
            final PreparedStatementWrapper ps = jdbcRepository.preparedStatementWrapperFactory.preparedStatementWrapperOf(jdbcRepository.dataSource, JOBS_COUNT);
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
        return createReader(new StringReader(json)).readObject();
    }
}
