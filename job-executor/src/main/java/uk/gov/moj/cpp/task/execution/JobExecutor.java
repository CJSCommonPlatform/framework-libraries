package uk.gov.moj.cpp.task.execution;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;

import uk.gov.justice.services.common.configuration.Value;
import uk.gov.moj.cpp.jobstore.api.JobService;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.task.extension.TaskRegistry;

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
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class JobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);


    @Resource(lookup="java:module/ModuleName")
    String moduleName;

    @Resource
    private TimerService timerService;

    @Resource
    private ManagedExecutorService executorService;

    @Inject
    private JobService jobService;

    @Inject
    private TaskRegistry taskRegistry;


    @Inject
    @Value(key = "jobstore.timer.start.wait.seconds", defaultValue = "20000")
    String timerStartWaitSeconds;

    @Inject
    @Value(key = "jobstore.timer.interval.seconds", defaultValue = "20000")
    String timerIntervalSeconds;

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

        LOGGER.info(format("Creating timer [%s]", timerName()));

        timerService.createIntervalTimer(parseLong(timerStartWaitSeconds), parseLong(timerIntervalSeconds), timerConfig);
    }

    private void cancelExistingTimer() {
        timerService.getAllTimers().stream().filter(t -> timerName().equals(t.getInfo())).forEach(Timer::cancel);
    }

    private String timerName() {
        if (timerName == null) {

            String timerModulePrefix = moduleName != null ? moduleName : "local";
            timerName = timerModulePrefix + ".job-manager.job.timer";
        }

        return timerName;
    }


    @Timeout
    public void fetchUnassignedTasks() {

        final UUID workerId = randomUUID();

        LOGGER.info(format("Retrieving new work from jobstore for WorkerID [%s]", workerId));

        Stream<Job> unassignedJobs = null;

        try {
            userTransaction.begin();
            unassignedJobs = jobService.getUnassignedJobsFor(workerId);
            userTransaction.commit();

            execute(unassignedJobs);

        } catch (Exception e) {
            LOGGER.error("Unexpected exception during committing transaction, attempting rollback...", e);

            try {
                userTransaction.rollback();
                LOGGER.info("Transaction rolled back successfully", e);
            } catch (SystemException e1) {
                LOGGER.error("Unexpected exception during transaction rollback, rollback maybe incomplete", e1);
            }
        } finally {
            if (unassignedJobs != null) {
                unassignedJobs.close();
            }
        }


    }

    private void execute(Stream<Job> jobsToDo) {
        LOGGER.info("Trigger task execution:");
        jobsToDo.forEach(job -> {
            final TaskExecutor task = new TaskExecutor(job, taskRegistry, jobService, userTransaction);
            executorService.submit(task);
        });
        LOGGER.info("Invocation of Task complete");
    }
}