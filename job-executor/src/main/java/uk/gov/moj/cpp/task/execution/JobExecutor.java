package uk.gov.moj.cpp.task.execution;

import static uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo.executionInfo;

import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionStatus;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.util.Optional;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

    private final Job job;

    private final TaskRegistry taskRegistry;

    private final JobService jobService;

    private final UserTransaction userTransaction;

    public JobExecutor(final Job jobData,
                       final TaskRegistry taskRegistry,
                       final JobService jobService,
                       final UserTransaction userTransaction) {
        this.job = jobData;
        this.taskRegistry = taskRegistry;
        this.jobService = jobService;
        this.userTransaction = userTransaction;
    }

    @Override
    @SuppressWarnings("squid:S3457")
    public void run() {
        final String taskName = job.getNextTask();
        LOGGER.info("Invoking {} task: ", taskName);
        final Optional<ExecutableTask> task = taskRegistry.getTask(taskName);

        try {
            userTransaction.begin();

            if (task.isPresent()) {
                final ExecutionInfo executionInfo = executionInfo().fromJob(job).build();
                final ExecutionInfo responseJob = task.get().execute(executionInfo);

                if (responseJob.getExecutionStatus().equals(ExecutionStatus.INPROGRESS)) {
                    jobService.updateJobTaskData(job.getJobId(), responseJob.getJobData());
                    jobService.updateNextTaskDetails(job.getJobId(), responseJob.getNextTask(), responseJob.getNextTaskStartTime());
                    jobService.releaseJob(job.getJobId());
                } else if (responseJob.getExecutionStatus().equals(ExecutionStatus.COMPLETED)) {
                    jobService.deleteJob(job.getJobId());
                }
            }else {
                LOGGER.error("No task registered to process this job {}", job.getJobId());
                jobService.releaseJob(job.getJobId());
            }

            userTransaction.commit();

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {

            LOGGER.error("Unexpected exception during transaction for Job {}, attempting rollback...{}", this, e);

            try {
                userTransaction.rollback();
                LOGGER.info("Transaction rolled back successfully", e);
            } catch (SystemException e1) {
                LOGGER.error("Unexpected exception during transaction rollback, rollback maybe incomplete {}", this, e1);
            }
        }
    }
    @Override
    public String toString() {
        return new StringBuilder("JobExecutor[ ")
                .append("job=").append(job)
                .append("]").toString();
    }
}