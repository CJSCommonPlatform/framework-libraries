package uk.gov.moj.cpp.jobstore.service;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static javax.json.Json.createObjectBuilder;
import static javax.json.Json.createReader;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;

import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
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

    @InjectMocks
    private JobService jobService;

    @Captor
    private ArgumentCaptor<Job> jobArgumentCaptor;

    @BeforeEach
    public void setup() {
        jobService.jobCount = "10";
    }

    @Test
    public void shouldReturnNextUnassignedJobs() {

        final UUID workerId = randomUUID();
        when(jobRepository.findJobsLockedTo(workerId)).thenReturn(mockJobs());

        final Stream<Job> jobs = jobService.getUnassignedJobsFor(workerId);

        assertThat(jobs.count(), is(3L));
        verify(jobRepository).lockJobsFor(workerId, 10);
    }

    @Test
    public void shouldCreateNewJob() {

        final JsonObject jobData = createObjectBuilder().add("testName", "testValue").build();
        final UUID jobId = randomUUID();
        final String startTask = "startTask";
        final ZonedDateTime startTime = ZonedDateTime.now();
        final Integer retryAttemptsRemaining = 1;
        final Integer priority = 1;
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

    private Stream<Job> mockJobs() {

        return Stream.of(mock(Job.class),
                mock(Job.class), mock(Job.class));
    }

    private JsonObject jobData(final String json) {
        return createReader(new StringReader(json)).readObject();
    }

}