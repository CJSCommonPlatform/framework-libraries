package uk.gov.moj.cpp.jobstore.service;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonReaderFactory;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;
import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;

import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    private static final String JOB_DATA_JSON = "{\"some\": \"json\"}";

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobStoreConfiguration jobStoreConfiguration;

    @InjectMocks
    private JobService jobService;

    @Captor
    private ArgumentCaptor<Job> jobArgumentCaptor;

    @Test
    public void shouldReturnNextUnassignedJobsForFirstPriority() {

        final UUID workerId = randomUUID();
        final int workerJobCount = 10;
        final List<Priority> priorities = List.of(MEDIUM, HIGH, LOW);

        when(jobStoreConfiguration.getWorkerJobCount()).thenReturn(workerJobCount);

        final List<Job> jobs = List.of(mock(Job.class), mock(Job.class), mock(Job.class));

        when(jobRepository.lockJobsFor(workerId, priorities.get(0), workerJobCount)).thenReturn(jobs.size());
        when(jobRepository.findJobsLockedTo(workerId)).thenReturn(jobs.stream());

        assertThat(jobService.getUnassignedJobsFor(workerId, priorities).count(), is(3L));
        verify(jobRepository).lockJobsFor(workerId, priorities.get(0), workerJobCount);
    }

    @Test
    public void shouldReturnNextUnassignedJobsForSecondPriorityIfNoJobsWithFirstPriorityFound() {

        final UUID workerId = randomUUID();
        final int workerJobCount = 10;
        final List<Priority> priorities = List.of(MEDIUM, HIGH, LOW);

        when(jobStoreConfiguration.getWorkerJobCount()).thenReturn(workerJobCount);

        final List<Job> jobs = List.of(mock(Job.class), mock(Job.class), mock(Job.class));

        when(jobRepository.lockJobsFor(workerId, priorities.get(0), workerJobCount)).thenReturn(0);
        when(jobRepository.lockJobsFor(workerId, priorities.get(1), workerJobCount)).thenReturn(jobs.size());
        when(jobRepository.findJobsLockedTo(workerId)).thenReturn(jobs.stream());

        assertThat(jobService.getUnassignedJobsFor(workerId, priorities).count(), is(3L));
        verify(jobRepository).lockJobsFor(workerId, priorities.get(1), workerJobCount);
    }

    @Test
    public void shouldReturnNextUnassignedJobsForThirdPriorityIfNoJobsWithFirstNorSecondPriorityFound() {

        final UUID workerId = randomUUID();
        final int workerJobCount = 10;
        final List<Priority> priorities = List.of(MEDIUM, HIGH, LOW);

        when(jobStoreConfiguration.getWorkerJobCount()).thenReturn(workerJobCount);

        final List<Job> jobs = List.of(mock(Job.class), mock(Job.class), mock(Job.class));

        when(jobRepository.lockJobsFor(workerId, priorities.get(0), workerJobCount)).thenReturn(0);
        when(jobRepository.lockJobsFor(workerId, priorities.get(1), workerJobCount)).thenReturn(0);
        when(jobRepository.lockJobsFor(workerId, priorities.get(2), workerJobCount)).thenReturn(jobs.size());
        when(jobRepository.findJobsLockedTo(workerId)).thenReturn(jobs.stream());

        assertThat(jobService.getUnassignedJobsFor(workerId, priorities).count(), is(3L));
        verify(jobRepository).lockJobsFor(workerId, priorities.get(2), workerJobCount);
    }

    @Test
    public void shouldReturnNOUnassignedJobsfNoJobsWithFirstSecondNorThirdePriorityFound() {

        final UUID workerId = randomUUID();
        final int workerJobCount = 10;
        final List<Priority> priorities = List.of(MEDIUM, HIGH, LOW);

        when(jobStoreConfiguration.getWorkerJobCount()).thenReturn(workerJobCount);
        when(jobRepository.lockJobsFor(workerId, priorities.get(0), workerJobCount)).thenReturn(0);
        when(jobRepository.lockJobsFor(workerId, priorities.get(1), workerJobCount)).thenReturn(0);
        when(jobRepository.lockJobsFor(workerId, priorities.get(2), workerJobCount)).thenReturn(0);

        assertThat(jobService.getUnassignedJobsFor(workerId, priorities).count(), is(0L));
        verifyNoMoreInteractions(jobRepository);
    }

    @Test
    public void shouldCreateNewJob() {

        final JsonObject jobData = getJsonBuilderFactory().createObjectBuilder().add("testName", "testValue").build();
        final UUID jobId = randomUUID();
        final String startTask = "startTask";
        final ZonedDateTime startTime = ZonedDateTime.now();
        final Integer retryAttemptsRemaining = 1;
        final Priority priority = HIGH;
        final Job mockJob = new Job(jobId, jobData, startTask, startTime, empty(), empty(), retryAttemptsRemaining, priority);


        jobService.insertJob(mockJob);
        verify(jobRepository).insertJob(jobArgumentCaptor.capture());


        final Job jobToInsert = jobArgumentCaptor.getValue();
        assertThat(jobToInsert.getJobId(), is(jobId));
        assertThat(jobToInsert.getNextTask(), is(startTask));
        assertThat(jobToInsert.getNextTaskStartTime(), is(startTime));
        assertThat(jobToInsert.getJobData(), is(jobData));
        assertThat(jobToInsert.getRetryAttemptsRemaining(), is(retryAttemptsRemaining));
        assertThat(jobToInsert.getPriority(), is(priority));
    }

    @Test
    public void shouldUpdateJobTaskData() {

        final UUID jobId = randomUUID();
        jobService.updateJobTaskData(jobId, jobData(JOB_DATA_JSON));
        verify(jobRepository).updateJobData(jobId, jobData(JOB_DATA_JSON));
    }

    @Test
    public void shouldUpdateNextTaskDetails() {

        final UUID jobId = randomUUID();
        final String input = "new next task";
        final ZonedDateTime now = now();
        final Integer retryAttemptsRemaining = 1;
        jobService.updateNextTaskDetails(jobId, input, now,retryAttemptsRemaining);
        verify(jobRepository).updateNextTaskDetails(jobId, input, toSqlTimestamp(now), retryAttemptsRemaining);
    }

    @Test
    public void shouldUpdateNextTaskRetryDetails() {
        final UUID jobId = randomUUID();
        final ZonedDateTime now = now();
        final Integer retryAttemptsRemaining = 1;

        jobService.updateNextTaskRetryDetails(jobId, now,retryAttemptsRemaining);

        verify(jobRepository).updateNextTaskRetryDetails(jobId,  toSqlTimestamp(now), retryAttemptsRemaining);
    }

    @Test
    public void shouldDeleteJob() {

        final UUID jobId = randomUUID();
        jobService.deleteJob(jobId);
        verify(jobRepository).deleteJob(jobId);
    }

    @Test
    public void shouldReleaseJob() {

        final UUID jobId = randomUUID();
        jobService.releaseJob(jobId);
        verify(jobRepository).releaseJob(jobId);
    }

    private JsonObject jobData(final String json) {
        return getJsonReaderFactory().createReader(new StringReader(json)).readObject();
    }

}