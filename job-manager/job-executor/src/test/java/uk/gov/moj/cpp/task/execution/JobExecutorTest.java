package uk.gov.moj.cpp.task.execution;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class JobExecutorTest {

    @Mock
    private TaskRegistry taskRegistry;

    @Mock
    private JobService jobService;

    @Mock
    private SampleTask sampleTask;

    @Mock
    private UserTransaction userTransaction;

    @Mock
    private UtcClock clock;

    @Mock
    private Logger logger;


    @Test
    public void shouldUpdateJobViaJobServiceWhenExecutionStatusIsInProgress() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

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
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

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
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
    }

    @Test
    public void shouldNotInvokeExecuteOnExecuteTaskWhenNotProvidedFromRegistry() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final Job job = job(jobId, jobData, null);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());

        createJobExecutor(job).run();

        verify(sampleTask, never()).execute(any(ExecutionInfo.class));
        verify(jobService).releaseJob(jobId);
    }

    @Test
    public void shouldRollbackTransactionWhenExceptionThrown() throws SystemException, NotSupportedException {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final NotSupportedException notSupportedException = new NotSupportedException();
        final Job job = new Job(jobId, jobData, "taskName", new UtcClock().now(), empty(), empty());
        final JobExecutor jobExecutor = createJobExecutor(job);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());
        doThrow(notSupportedException).when(userTransaction).begin();

        jobExecutor.run();

        verify(logger).error("Unexpected exception during transaction for Job {}, attempting rollback...{}", jobExecutor, notSupportedException);
        verify(userTransaction).rollback();
    }

    @Test
    public void shouldLogErrorIfRollbackTransactionFails() throws SystemException, NotSupportedException {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final NotSupportedException notSupportedException = new NotSupportedException();
        final SystemException systemException = new SystemException();
        final Job job = new Job(jobId, jobData, "taskName", new UtcClock().now(), empty(), empty());
        final JobExecutor jobExecutor = createJobExecutor(job);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());
        doThrow(notSupportedException).when(userTransaction).begin();
        doThrow(systemException).when(userTransaction).rollback();

        jobExecutor.run();

        verify(logger).error("Unexpected exception during transaction for Job {}, attempting rollback...{}", jobExecutor, notSupportedException);
        verify(logger).error("Unexpected exception during transaction rollback, rollback maybe incomplete {}", jobExecutor, systemException);
    }

    @Test
    public void shouldUpdateJobViaJobServiceIfNextStartTimeIsBeforeNow() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).build();
        final ZonedDateTime currentTime = nextTaskStartTime.plusSeconds(10L);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(currentTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).deleteJob(any());
    }

    @Test
    public void shouldUpdateJobViaJobServiceIfNextStartTimeIsEqualToNow() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).build();
        final ZonedDateTime currentTime = nextTaskStartTime;

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(currentTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).deleteJob(any());
    }

    @Test
    public void shouldNotInvokeExecuteOnExecuteTaskIfNextStartTimeIsAfterNow() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime);
        final ZonedDateTime currentTime = nextTaskStartTime.minusSeconds(10L);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(clock.now()).thenReturn(currentTime);

        createJobExecutor(job).run();

        verifyNoInteractions(sampleTask);
        verifyNoInteractions(jobService);
    }

    private JobExecutor createJobExecutor(final Job job) {
        return new JobExecutor(job, taskRegistry, jobService, userTransaction, clock, logger);
    }

    private Job job(final UUID jobId,
                    final JsonObject jobData,
                    final ZonedDateTime nextTaskStartTime) {
        return Job.job()
                .withJobId(jobId)
                .withJobData(jobData)
                .withNextTaskStartTime(nextTaskStartTime)
                .withNextTask("taskName")
                .build();
    }
}