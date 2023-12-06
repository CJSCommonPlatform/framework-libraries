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

import java.util.List;
import java.util.Optional;
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
    public void shouldUpdateNextTaskDetailsForRetryViaJobServiceWhenExecutionStatusIsInProgressAndShouldBeRetriedAndRetryAttemptsRemainingGreaterThanZeroAndTaskHasRetryDurations() {
        final Integer retryAttemptsRemaining = 1;
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, retryAttemptsRemaining);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).withShouldRetry(true).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(Optional.of(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(sampleTask.getRetryDurationsInSecs()).thenReturn(Optional.of(List.of(1L, 2L, 4L)));
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateForRetry(jobId, retryAttemptsRemaining-1, nextTaskStartTime.plusSeconds(4));
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).updateNextTaskDetails(any(), any(), any(), any());
        verify(jobService, never()).updateJobTaskData(any(), any());
        verify(jobService, never()).deleteJob(any());
        verify(taskRegistry, never()).findRetryAttemptsRemainingFor(any());
    }

    @Test
    public void shouldUpdateNextTaskDetailsViaJobServiceWhenExecutionStatusIsInProgressAndShouldBeRetriedAndRetryAttemptsRemainingGreaterThanZeroAndTaskHasNoRetryDurations() {
        final Integer retryAttemptsRemaining = 1;
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, retryAttemptsRemaining);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).withShouldRetry(true).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(of(sampleTask));
        when(taskRegistry.findRetryAttemptsRemainingFor(eq("taskName"))).thenReturn(retryAttemptsRemaining);
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(sampleTask.getRetryDurationsInSecs()).thenReturn(Optional.empty());
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime, retryAttemptsRemaining);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).updateForRetry(any(), any(), any());
        verify(jobService, never()).deleteJob(any());
    }

    @Test
    public void shouldUpdateNextTaskDetailsViaJobServiceWhenExecutionStatusIsInProgressAndShouldBeRetriedAndRetryAttemptsRemainingAreZeroAndTaskHasRetryDurations() {
        final Integer retryAttemptsRemaining = 0;
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, retryAttemptsRemaining);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).withShouldRetry(true).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(of(sampleTask));
        when(taskRegistry.findRetryAttemptsRemainingFor(eq("taskName"))).thenReturn(retryAttemptsRemaining);
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime, retryAttemptsRemaining);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).updateForRetry(any(), any(), any());
        verify(jobService, never()).deleteJob(any());
    }

    @Test
    public void shouldUpdateNextTaskDetailsViaJobServiceWhenExecutionStatusIsInProgressAndShouldNotBeRetriedAndRetryAttemptsRemainingAreGreaterThanZeroAndTaskHasRetryDurations() {
        final Integer retryAttemptsRemaining = 1;
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, retryAttemptsRemaining);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(INPROGRESS).withShouldRetry(false).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(of(sampleTask));
        when(taskRegistry.findRetryAttemptsRemainingFor(eq("taskName"))).thenReturn(retryAttemptsRemaining);
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService).updateJobTaskData(jobId, jobData);
        verify(jobService).updateNextTaskDetails(jobId, "taskName", nextTaskStartTime, retryAttemptsRemaining);
        verify(jobService).releaseJob(jobId);
        verify(jobService, never()).updateForRetry(any(), any(), any());
        verify(jobService, never()).deleteJob(any());
    }

    @Test
    public void shouldDeleteJobViaJobServiceWhenExecutionStatusIsCompleted() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, 0);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(COMPLETED).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
        verify(jobService, never()).updateJobTaskData(any(), any());
        verify(jobService, never()).updateNextTaskDetails(any(), any(), any(), any());
        verify(jobService, never()).releaseJob(any());
        verify(jobService, never()).updateForRetry(any(), any(), any());
        verify(jobService).deleteJob(jobId);
        verify(taskRegistry, never()).findRetryAttemptsRemainingFor(any());
    }

    @Test
    public void shouldExecuteTaskWhenProvidedFromRegistryAndNextStartTimeIsEqualsNow() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, 0);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(COMPLETED).build();

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(nextTaskStartTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
    }

    @Test
    public void shouldExecuteTaskWhenProvidedFromRegistryAndNextStartTimeIsBeforeNow() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, 0);
        final ExecutionInfo responseExecutionInfo = executionInfo().fromJob(job).withExecutionStatus(COMPLETED).build();
        final ZonedDateTime currentTime = nextTaskStartTime.plusSeconds(10L);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(sampleTask.execute(any(ExecutionInfo.class))).thenReturn(responseExecutionInfo);
        when(clock.now()).thenReturn(currentTime);

        createJobExecutor(job).run();

        verify(sampleTask).execute(any(ExecutionInfo.class));
    }

    @Test
    public void shouldNotExecuteTaskWhenNotProvidedFromRegistry() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final Job job = job(jobId, jobData, null, 0);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());

        createJobExecutor(job).run();

        verify(sampleTask, never()).execute(any(ExecutionInfo.class));
        verify(jobService).releaseJob(jobId);
    }
    @Test
    public void shouldNotExecuteTaskIfNextTaskStartTimeIsAfterNow() {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final ZonedDateTime nextTaskStartTime = now();
        final Job job = job(jobId, jobData, nextTaskStartTime, 0);
        final ZonedDateTime currentTime = nextTaskStartTime.minusSeconds(10L);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(ofNullable(sampleTask));
        when(clock.now()).thenReturn(currentTime);

        createJobExecutor(job).run();

        verifyNoInteractions(sampleTask);
        verifyNoInteractions(jobService);
    }

    @Test
    public void shouldRollbackTransactionWhenExceptionThrown() throws SystemException, NotSupportedException {
        final UUID jobId = randomUUID();
        final JsonObject jobData = mock(JsonObject.class);
        final NotSupportedException notSupportedException = new NotSupportedException();
        final Job job = new Job(jobId, jobData, "taskName", new UtcClock().now(), empty(), empty(), 0);
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
        final Job job = new Job(jobId, jobData, "taskName", new UtcClock().now(), empty(), empty(), 0);
        final JobExecutor jobExecutor = createJobExecutor(job);

        when(taskRegistry.getTask(eq("taskName"))).thenReturn(empty());
        doThrow(notSupportedException).when(userTransaction).begin();
        doThrow(systemException).when(userTransaction).rollback();

        jobExecutor.run();

        verify(logger).error("Unexpected exception during transaction for Job {}, attempting rollback...{}", jobExecutor, notSupportedException);
        verify(logger).error("Unexpected exception during transaction rollback, rollback maybe incomplete {}", jobExecutor, systemException);
    }

    private JobExecutor createJobExecutor(final Job job) {
        return new JobExecutor(job, taskRegistry, jobService, userTransaction, clock, logger);
    }

    private Job job(final UUID jobId,
                    final JsonObject jobData,
                    final ZonedDateTime nextTaskStartTime,
                    final Integer retryAttemptsRemaining) {
        return Job.job()
                .withJobId(jobId)
                .withJobData(jobData)
                .withNextTaskStartTime(nextTaskStartTime)
                .withNextTask("taskName")
                .withRetryAttemptsRemaining(retryAttemptsRemaining)
                .build();
    }
}