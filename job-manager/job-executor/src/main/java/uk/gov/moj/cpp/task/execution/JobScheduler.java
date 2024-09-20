package uk.gov.moj.cpp.task.execution;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.slf4j.LoggerFactory.getLogger;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;
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

    @Resource
    private TimerService timerService;

    @Resource
    private ManagedExecutorService executorService;

    @Inject
    private JobService jobService;

    @Inject
    private TaskRegistry taskRegistry;

    @Inject
    private JobStoreConfiguration jobStoreConfiguration;

    @Inject
    private JobStoreSchedulerPrioritySelector jobStoreSchedulerPrioritySelector;

    @Inject
    private UtcClock clock;

    @Inject
    private UserTransaction userTransaction;

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

        timerService.createIntervalTimer(
                jobStoreConfiguration.getTimerStartWaitMilliseconds(),
                jobStoreConfiguration.getTimerIntervalMilliseconds(),
                timerConfig);
    }

    private void cancelExistingTimer() {
        timerService.getAllTimers().stream().filter(t -> timerName().equals(t.getInfo())).forEach(Timer::cancel);
    }

    private String timerName() {
        if (timerName == null) {
            final String moduleName = jobStoreConfiguration.getModuleName();
            final String timerModulePrefix = moduleName != null ? moduleName : "local";
            timerName = timerModulePrefix + ".job-manager.job.timer";
        }

        return timerName;
    }

    @Timeout
    public void fetchUnassignedJobs() {

        final UUID workerId = randomUUID();
        final List<Priority> orderedPriorities = jobStoreSchedulerPrioritySelector.selectOrderedPriorities();

        if (logger.isDebugEnabled()) {
            logger.debug(format("Fetching new jobs from jobstore in priority order %s", orderedPriorities));
        }

        Stream<Job> unassignedJobs = null;

        try {
            userTransaction.begin();

            // Collect into List and forward to execute() method as a new Stream.
            // (as userTransaction.commit() will close the DB cursor/resultset)
            unassignedJobs = jobService.getUnassignedJobsFor(workerId, orderedPriorities);
            final List<Job> jobList = unassignedJobs.toList();

            userTransaction.commit();

            if (jobList.isEmpty()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("No new jobs found in jobstore");
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(format("Found %d %s priority job(s) to run from jobstore", jobList.size(), jobList.get(0).getPriority()));
                }

                execute(jobList.stream());
            }


        } catch (final NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {

            logger.error("Unexpected exception during transaction, attempting rollback...", e);

            try {
                if (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION) {
                    userTransaction.rollback();
                    logger.info("Transaction rolled back successfully", e);
                }

            } catch (final SystemException e1) {
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

            executorService.submit(new JobExecutor(
                    job,
                    taskRegistry,
                    jobService,
                    userTransaction,
                    clock,
                    getLogger(JobExecutor.class)));

            logger.trace("Invocation of Task complete");
        });
    }
}
