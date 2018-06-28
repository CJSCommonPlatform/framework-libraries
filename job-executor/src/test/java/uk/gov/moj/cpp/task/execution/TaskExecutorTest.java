package uk.gov.moj.cpp.task.execution;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.PERMANENT_FAILURE;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.SUCCESSFUL;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.TEMPORARY_FAILURE;

import uk.gov.moj.cpp.jobstore.api.JobService;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.task.extension.SampleTask;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.json.JsonObject;
import javax.transaction.UserTransaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class TaskExecutorTest {


    @Mock
    private Job job;
    @Mock
    private TaskRegistry taskRegistry;
    @Mock
    private JobService jobService;
    @Mock
    private SampleTask sampleTask;
    @Mock
    private UserTransaction userTransaction;



    @Test
    public void shouldUpdateJobViaJobServiceWhenJobStatusIsTemporaryFailure() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();

        when(job.getNextTask()).thenReturn("taskName");
        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(job)).thenReturn(job);
        when(job.getJobStatus()).thenReturn(of(TEMPORARY_FAILURE));
        when(job.getJobId()).thenReturn(jobId);
        when(job.getJobData()).thenReturn(jobData);
        when(job.getNextTask()).thenReturn("taskName");
        when(job.getNextTaskStartTime()).thenReturn(nextTaskStartTime);

        TaskExecutor taskExecutor = new TaskExecutor(job, taskRegistry, jobService, userTransaction);
        taskExecutor.run();

        verify(sampleTask).execute(eq(job));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).deleteJob(any());

    }

    @Test
    public void shouldDeleteJobViaJobServiceWhenJobStatusIsSuccessful() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();

        when(job.getNextTask()).thenReturn("taskName");
        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(job)).thenReturn(job);
        when(job.getJobStatus()).thenReturn(of(SUCCESSFUL));
        when(job.getJobId()).thenReturn(jobId);

        TaskExecutor taskExecutor = new TaskExecutor(job, taskRegistry, jobService, userTransaction);
        taskExecutor.run();

        verify(sampleTask).execute(eq(job));
        verify(jobService, never()).updateJobTaskData(any(), any());
        verify(jobService, never()).updateNextTaskDetails(any(), any(), any());
        verify(jobService, never()).releaseJob(any());
        verify(jobService).deleteJob(jobId);

    }


    @Test
    public void shouldDeleteJobViaJobServiceWhenJobStatusIsPermanentFailure() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();

        when(job.getNextTask()).thenReturn("taskName");
        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(job)).thenReturn(job);
        when(job.getJobStatus()).thenReturn(of(PERMANENT_FAILURE));
        when(job.getJobId()).thenReturn(jobId);

        TaskExecutor taskExecutor = new TaskExecutor(job, taskRegistry, jobService, userTransaction);
        taskExecutor.run();

        verify(sampleTask).execute(eq(job));
        verify(jobService, never()).updateJobTaskData(any(), any());
        verify(jobService, never()).updateNextTaskDetails(any(), any(), any());
        verify(jobService, never()).releaseJob(any());
        verify(jobService).deleteJob(jobId);

    }



    @Test
    public void shouldInvokeExecuteOnExecuteTaskWhenProvidedFromRegistry() {

        when(job.getNextTask()).thenReturn("taskName");
        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(job)).thenReturn(job);
        when(job.getJobStatus()).thenReturn(empty());
        TaskExecutor taskExecutor = new TaskExecutor(job, taskRegistry, jobService, userTransaction);
        taskExecutor.run();

        verify(sampleTask).execute(eq(job));
    }

    @Test
    public void shouldNotInvokeExecuteOnExecuteTaskWhenNotProvidedFromRegistry() {
        final UUID jobId = randomUUID();
        when(job.getJobId()).thenReturn(jobId);
        when(job.getNextTask()).thenReturn("taskName");
        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());
        TaskExecutor taskExecutor = new TaskExecutor(job, taskRegistry, jobService, userTransaction);
        taskExecutor.run();

        verify(sampleTask, never()).execute(eq(job));
        verify(jobService).releaseJob(jobId);
    }
}