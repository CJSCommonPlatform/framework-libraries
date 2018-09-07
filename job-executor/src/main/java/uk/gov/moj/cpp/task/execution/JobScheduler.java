package uk.gov.moj.cpp.task.execution;

import static java.lang.Long.parseLong;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.services.common.configuration.Value;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class JobScheduler {

    @Inject
    private Logger logger;


    @Resource(lookup="java:module/ModuleName")
    String moduleName;

    @Resource
    TimerService timerService;

    @Resource
    ManagedExecutorService executorService;

    @Inject
    JobService jobService;

    @Inject
    TaskRegistry taskRegistry;


    @Inject
    @Value(key = "jobstore.timer.start.wait.milliseconds", defaultValue = "20000")
    String timerStartWaitSeconds;

    @Inject
    @Value(key = "jobstore.timer.interval.milliseconds", defaultValue = "20000")
    String timerIntervalSeconds;

    @Inject
    UserTransaction userTransaction;

    private String timerName;

    @PostConstruct
    public void init() {
        cancelExistingTimer();
        createIntervalTimer();
    }

    private void createIntervalTimer() {
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo(timerName());

        logger.info("Creating timer [{}]", timerName);

        timerService.createIntervalTimer(parseLong(timerStartWaitSeconds), parseLong(timerIntervalSeconds), timerConfig);
    }

    private void cancelExistingTimer() {
        timerService.getAllTimers().stream().filter(t -> timerName().equals(t.getInfo())).forEach(Timer::cancel);
    }

    private String timerName() {
        if (timerName == null) {
            final String timerModulePrefix = moduleName != null ? moduleName : "local";
            timerName = timerModulePrefix + ".job-manager.job.timer";
        }

        return timerName;
    }


    @Timeout
    public void fetchUnassignedJobs() {

        final UUID workerId = randomUUID();

        logger.debug("Retrieving new work from jobstore for WorkerID [{}]", workerId);

        Stream<Job> unassignedJobs = null;

        try {
            userTransaction.begin();

            // Collect into List and forward to execute() method as a new Stream.
            // (as userTransaction.commit() will close the DB cursor/resultset)
            unassignedJobs = jobService.getUnassignedJobsFor(workerId);
            final List<Job> jobList = unassignedJobs.collect(toList());

            userTransaction.commit();

            execute(jobList.stream());

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {

            logger.error("Unexpected exception during transaction, attempting rollback...", e);

            try {
                if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                    userTransaction.rollback();
                    logger.info("Transaction rolled back successfully", e);
                }

            } catch(SystemException e1){
                logger.error("Unexpected exception during transaction rollback, rollback maybe incomplete", e1);
            }

        } finally {
            if (unassignedJobs != null) {
                unassignedJobs.close();
            }
        }
    }

    private void execute(Stream<Job> jobsToDo) {
        jobsToDo.forEach(job -> {
            logger.trace("Trigger task execution:");
            final JobExecutor task = new JobExecutor(job, taskRegistry, jobService, userTransaction);
            executorService.submit(task);
            logger.trace("Invocation of Task complete");
        });
    }
}
