package uk.gov.moj.cpp.jobstore.api;

import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;

import java.time.ZonedDateTime;
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
public class ExecutionServiceTest {

    private static final String JOB_DATA_JSON = "{\"some\": \"json\"}";

    @Mock
    private JobService jobService;

    @InjectMocks
    private ExecutionService executionService;

    @Captor
    private ArgumentCaptor<Job> jobArgumentCaptor;


    @Test
    public void shouldExecute() {

        final JsonObject jobData = createObjectBuilder().add("testName", "testValue").build();
        final String startTask = "startTask";
        final ZonedDateTime startTime = ZonedDateTime.now();
        final ExecutionInfo mockJob = new ExecutionInfo(jobData, startTask, startTime, ExecutionStatus.STARTED);


        executionService.executeWith(mockJob);
        verify(jobService).insertJob(jobArgumentCaptor.capture());


        assertThat(jobArgumentCaptor.getValue().getJobId(), any(UUID.class));
        assertThat(jobArgumentCaptor.getValue().getNextTask(), is(startTask));
        assertThat(jobArgumentCaptor.getValue().getNextTaskStartTime(), is(startTime));
        assertThat(jobArgumentCaptor.getValue().getJobData(), is(jobData));
    }
}
