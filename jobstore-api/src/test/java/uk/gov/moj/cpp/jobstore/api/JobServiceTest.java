package uk.gov.moj.cpp.jobstore.api;

import static java.time.ZonedDateTime.now;
import static java.util.UUID.randomUUID;
import static javax.json.Json.createObjectBuilder;
import static javax.json.Json.createReader;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

    private static final String JOB_DATA_JSON = "{\"some\": \"json\"}";

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Captor
    private ArgumentCaptor<Job> jobArgumentCaptor;

    @Before
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
        final JobRequest mockJob = new JobRequest(jobId, jobData, startTask, startTime);


        jobService.createJob(mockJob);
        verify(jobRepository).createJob(jobArgumentCaptor.capture());


        assertThat(jobArgumentCaptor.getValue().getJobId(), is(jobId));
        assertThat(jobArgumentCaptor.getValue().getNextTask(), is(startTask));
        assertThat(jobArgumentCaptor.getValue().getNextTaskStartTime(), is(startTime));
        assertThat(jobArgumentCaptor.getValue().getJobData(), is(jobData));
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
        jobService.updateNextTaskDetails(jobId, input, now);
        verify(jobRepository).updateNextTaskDetails(jobId, input, toSqlTimestamp(now));
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
