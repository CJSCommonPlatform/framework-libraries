package uk.gov.moj.cpp.task.execution;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

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
public class DefaultExecutionServiceTest {

    @Mock
    private JobService jobService;

    @Mock
    private TaskRegistry taskRegistry;

    @InjectMocks
    private DefaultExecutionService executionService;

    @Captor
    private ArgumentCaptor<Job> jobArgumentCaptor;


    @Test
    public void shouldInsertJobByInvokingJobService() {

        final JsonObject jobData = getJsonBuilderFactory().createObjectBuilder().add("testName", "testValue").build();
        final String startTask = "startTask";
        final ZonedDateTime startTime = ZonedDateTime.now();
        final ExecutionInfo mockJob = new ExecutionInfo(jobData, startTask, startTime, ExecutionStatus.STARTED, true, HIGH);

        when(taskRegistry.findRetryAttemptsRemainingFor(startTask)).thenReturn(1);

        executionService.executeWith(mockJob);
        verify(jobService).insertJob(jobArgumentCaptor.capture());


        Job jobToInsert = jobArgumentCaptor.getValue();
        assertThat(jobToInsert.getJobId(), any(UUID.class));
        assertThat(jobToInsert.getNextTask(), is(startTask));
        assertThat(jobToInsert.getNextTaskStartTime(), is(startTime));
        assertThat(jobToInsert.getJobData(), is(jobData));
        assertThat(jobToInsert.getRetryAttemptsRemaining(), is(1));
    }
}
