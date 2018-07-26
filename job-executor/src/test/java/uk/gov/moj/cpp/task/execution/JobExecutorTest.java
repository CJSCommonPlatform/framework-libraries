package uk.gov.moj.cpp.task.execution;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo.executionInfo;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.COMPLETED;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.INPROGRESS;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.SampleTask;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.json.JsonObject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class JobExecutorTest {

    @Mock
    private TaskRegistry taskRegistry;
    @Mock
    private JobService jobService;
    @Mock
    private SampleTask sampleTask;
    @Mock
    private UserTransaction userTransaction;

    @Test
    public void shouldUpdateJobViaJobServiceWhenExecutionStatusIsInProgress() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);

        final JobExecutor jobExecutor = new JobExecutor(job, taskRegistry, jobService, userTransaction);

        jobExecutor.run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).deleteJob(any());
    }


    @Test
    public void shouldDeleteJobViaJobServiceWhenExecutionStatusIsCompleted() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(COMPLETED).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);

        final JobExecutor jobExecutor = new JobExecutor(job, taskRegistry, jobService, userTransaction);

        jobExecutor.run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService, never()).updateJobTaskData(any(), any());
        verify(jobService, never()).updateNextTaskDetails(any(), any(), any());
        verify(jobService, never()).releaseJob(any());
        verify(jobService).deleteJob(jobId);

    }

    @Test
    public void shouldInvokeExecuteOnExecuteTaskWhenProvidedFromRegistry() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(COMPLETED).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);

        final JobExecutor jobExecutor = new JobExecutor(job, taskRegistry, jobService, userTransaction);
        jobExecutor.run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
    }

    @Test
    public void shouldNotInvokeExecuteOnExecuteTaskWhenNotProvidedFromRegistry() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final Job job = job(jobId, jobData, null);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());

        final JobExecutor jobExecutor = new JobExecutor(job, taskRegistry, jobService, userTransaction);
        jobExecutor.run();

        verify(sampleTask, never()).execute(any(ExecutionInfo.class));
        verify(jobService).releaseJob(jobId);
    }

    @Test
    public void shouldRollbackTransactionWhenExceptionThrown() throws SystemException, NotSupportedException {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final Job job = new Job(jobId, jobData, "taskName", new UtcClock().now(), empty(), empty());

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());
        doThrow(new NotSupportedException()).when(userTransaction).begin();

        final JobExecutor jobExecutor = new JobExecutor(job, taskRegistry, jobService, userTransaction);
        jobExecutor.run();

        verify(userTransaction).rollback();
    }

    private Job job(final UUID jobId,
                    final JsonObject jobData,
                    final ZonedDateTime nextTaskStartTime) {
        return Job.job()
                .withJobId(jobId)
                .withJobData(jobData)
                .withNextTaskStartTime(nextTaskStartTime)
                .withNextTask("taskName").build();
    }
}