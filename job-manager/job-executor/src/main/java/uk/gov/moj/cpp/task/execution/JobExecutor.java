package uk.gov.moj.cpp.task.execution;

import static uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo.executionInfo;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.COMPLETED;
import static uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus.INPROGRESS;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

public class JobExecutor implements Runnable {

    private final Job job;
    private final TaskRegistry taskRegistry;
    private final JobService jobService;
    private final UserTransaction userTransaction;
    private final UtcClock clock;
    private final Logger logger;

    public JobExecutor(final Job jobData,
                       final TaskRegistry taskRegistry,
                       final JobService jobService,
                       final UserTransaction userTransaction,
                       final UtcClock clock,
                       final Logger logger) {
        this.job = jobData;
        this.taskRegistry = taskRegistry;
        this.jobService = jobService;
        this.userTransaction = userTransaction;
        this.clock = clock;
        this.logger = logger;
    }

    @Override
    @SuppressWarnings("squid:S3457")
    public void run() {
        final String taskName = job.getNextTask();
        logger.info("Invoking {} task: ", taskName);
        final Optional<ExecutableTask> task = taskRegistry.getTask(taskName);

        try {
            userTransaction.begin();

            if (task.isPresent()) {

                final ExecutionInfo executionInfo = executionInfo().fromJob(job).build();

                if (isStartTimeOfTask(executionInfo)) {
                    executeTask(task.get(), executionInfo);
                }

            } else {
                logger.error("No task registered to process this job {}", job.getJobId());
                jobService.releaseJob(job.getJobId());
            }

            userTransaction.commit();

        } catch (final NotSupportedException |
                SystemException |
                RollbackException |
                HeuristicMixedException |
                HeuristicRollbackException e) {

            logger.error("Unexpected exception during transaction for Job {}, attempting rollback...{}", this, e);

            try {
                userTransaction.rollback();
                logger.info("Transaction rolled back successfully", e);
            } catch (final SystemException e1) {
                logger.error("Unexpected exception during transaction rollback, rollback maybe incomplete {}", this, e1);
            }
        }
    }

    @Override
    public String toString() {
        return "JobExecutor[ " +
                "job=" + job +
                "]";
    }

    private boolean isStartTimeOfTask(final ExecutionInfo executionInfo) {
        final ZonedDateTime nextTaskStartTime = executionInfo.getNextTaskStartTime();
        final ZonedDateTime now = clock.now();

        return nextTaskStartTime.isBefore(now) || nextTaskStartTime.isEqual(now);
    }

    private void executeTask(final ExecutableTask task, final ExecutionInfo executionInfo) {
        final ExecutionInfo executionResponse = task.execute(executionInfo);

        if (executionResponse.getExecutionStatus().equals(INPROGRESS)) {
            if (canRetry(task, executionResponse)) {
                performRetry(task);
            } else {
                final Integer retryAttemptsRemaining = taskRegistry.findRetryAttemptsRemainingFor(executionResponse.getNextTask());
                jobService.updateJobTaskData(job.getJobId(), executionResponse.getJobData());
                jobService.updateNextTaskDetails(job.getJobId(), executionResponse.getNextTask(), executionResponse.getNextTaskStartTime(), retryAttemptsRemaining);
                jobService.releaseJob(job.getJobId());
            }
        } else if (executionResponse.getExecutionStatus().equals(COMPLETED)) {
            jobService.deleteJob(job.getJobId());
        }
    }

    private boolean canRetry(final ExecutableTask task, final ExecutionInfo taskResponse) {
        final boolean shouldRetryTask = taskResponse.isShouldRetry();
        final Integer retryAttemptsRemaining = job.getRetryAttemptsRemaining();
        final boolean taskHasRetryDurationsConfigured = task.getRetryDurationsInSecs().isPresent();

        logger.info("Checking if task is retryable, jobID:{}, executionInfo.shouldRetry:{}, retryAttemptsRemaining:{}, has task configured with retryDurationsInSecs:{}",
                job.getJobId(), shouldRetryTask, retryAttemptsRemaining, taskHasRetryDurationsConfigured);

        return shouldRetryTask
                && retryAttemptsRemaining > 0
                && taskHasRetryDurationsConfigured;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void performRetry(final ExecutableTask currentTask) {
        final List<Long> retryDurations = currentTask.getRetryDurationsInSecs().get();
        final Integer retryAttemptsRemaining = job.getRetryAttemptsRemaining();
        final Long retryDurationInSecs = retryDurations.get(retryDurations.size() - retryAttemptsRemaining);
        final ZonedDateTime exhaustTaskStartTime = clock.now().plusSeconds(retryDurationInSecs);

        logger.info("Updating task retryDetails to performRetry, jobID: {}, retryAttemptsRemaining: {}, taskToExecuteOnRetriesExhaust: {}, exhaustTaskStartTime: {}",
                job.getJobId(), job.getRetryAttemptsRemaining(), job.getNextTask(), exhaustTaskStartTime);

        jobService.updateNextTaskRetryDetails(job.getJobId(), exhaustTaskStartTime, retryAttemptsRemaining-1);
        jobService.releaseJob(job.getJobId());
    }
}