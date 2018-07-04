package uk.gov.moj.cpp.jobmanager.example.task;

import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.TEMPORARY_FAILURE;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.CakeBakingTime;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.persistence.Job;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("BAKE_CAKE")
public class BakeCakeTask implements ExecutableTask {

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public Job execute(Job job) {

        final CakeBakingTime cakeBakingTime = jsonObjectConverter.convert(job.getJobData(), CakeBakingTime.class);

        if (cakeBakingTime.getStartTimeString() == null) {
            logger.info("Placing cake tin in oven [job {}]", job);
        }

        logger.info("Cake will bake for {} seconds", cakeBakingTime.getCookingTimeSeconds());
        logger.info("Checking whether cake is cooked...");

        final Duration cookingTime = Duration.of(cakeBakingTime.getCookingTimeSeconds(), SECONDS);
        final String startTimeStr = cakeBakingTime.getStartTimeString() == null ? now().format(ISO_DATE_TIME) : cakeBakingTime.getStartTimeString();
        final CakeBakingTime newCakeBakingTime = cakeBakingTime.getStartTimeString() != null ? cakeBakingTime :  new CakeBakingTime(cakeBakingTime.getCookingTimeSeconds(), startTimeStr);

        final LocalDateTime startDateTime = LocalDateTime.parse(startTimeStr, ISO_DATE_TIME);
        final LocalDateTime finishTime = startDateTime.plus(cookingTime);

        Job nextJob = null;
        if (now().isBefore(finishTime) ) {
            logger.info("Cake not cooked yet, went in oven at {}, will be baked at {}", startDateTime, finishTime);
            // Set Job.nextTaskStartTime to when the Cake will be baked, JobExecutor won't try and run this Task
            // again until this time
            nextJob = jobUtil.sameJob(job, TEMPORARY_FAILURE, newCakeBakingTime, finishTime.atZone(systemDefault()));
        }
        else {
            logger.info("Cake is cooked !");
            nextJob = jobUtil.nextJob(job);
        }

        return nextJob;
    }
}
