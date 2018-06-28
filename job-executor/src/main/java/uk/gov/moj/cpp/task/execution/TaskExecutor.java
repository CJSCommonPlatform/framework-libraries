package uk.gov.moj.cpp.task.execution;


import static java.lang.String.format;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.TEMPORARY_FAILURE;

import uk.gov.moj.cpp.jobstore.api.JobService;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.util.Optional;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskExecutor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutor.class);

    private final Job job;

    private final TaskRegistry taskRegistry;

    private final JobService jobService;

    private final UserTransaction userTransaction;


    public TaskExecutor(final Job jobData, final TaskRegistry taskRegistry, final JobService jobService, UserTransaction userTransaction) {
        this.job = jobData;
        this.taskRegistry = taskRegistry;
        this.jobService = jobService;
        this.userTransaction = userTransaction;
    }

    @Override
    public void run() {
        final String taskName = job.getNextTask();
        LOGGER.info("Invoking {} task: ", taskName);
        final Optional<ExecutableTask> task = taskRegistry.getTask(taskName);

        try {

            userTransaction.begin();

            if (task.isPresent()) {

                final Job responseJob = task.get().execute(job);

                responseJob.getJobStatus().ifPresent(status -> {
                    if (status == TEMPORARY_FAILURE) {
                        jobService.updateJobTaskData(responseJob.getJobId(), responseJob.getJobData());
                        jobService.updateNextTaskDetails(responseJob.getJobId(), responseJob.getNextTask(), responseJob.getNextTaskStartTime());
                        jobService.releaseJob(responseJob.getJobId());
                    }
                    else {
                        jobService.deleteJob(responseJob.getJobId());
                    }
                });

            } else {
                LOGGER.error(format("No task registered to process this job %s", this));
                jobService.releaseJob(job.getJobId());
            }

            userTransaction.commit();

        } catch (Exception e) {
            LOGGER.error(format("Unexpected exception during committing transaction for Job %s, attempting rollback...%s", this,  e));

            try {
                userTransaction.rollback();
                LOGGER.info("Transaction rolled back successfully", e);
            } catch (SystemException e1) {
                LOGGER.error(format("Unexpected exception during transaction rollback, rollback maybe incomplete %s", this), e1);
            }
        }

    }



    @Override
    public String toString() {
        return new StringBuilder("TaskExecutor[ ")
                .append("job=").append(job)
                .append("]").toString();
    }
}