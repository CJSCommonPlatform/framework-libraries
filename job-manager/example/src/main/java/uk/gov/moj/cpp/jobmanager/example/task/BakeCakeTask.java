package uk.gov.moj.cpp.jobmanager.example.task;

import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.CakeBakingTime;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("BAKE_CAKE")
public class BakeCakeTask implements ExecutableTask {

    private static final int THIRTY_SECONDS = 10;
    private static final int SIXTY_SECONDS = 20;
    private static final int THREE_MINUTES = 30;

    private static final String RETRY_DURATIONS_SECONDS = THIRTY_SECONDS + ", " + SIXTY_SECONDS + ", " + THREE_MINUTES;

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public ExecutionInfo execute(ExecutionInfo executionInfo) {

        final CakeBakingTime cakeBakingTime = jsonObjectConverter.convert(executionInfo.getJobData(), CakeBakingTime.class);

        if (cakeBakingTime.getStartTimeString() == null) {
            logger.info("Placing cake tin in oven [executionInfo {}]", executionInfo);
        }

        logger.info("Cake will bake for {} seconds", cakeBakingTime.getCookingTimeSeconds());
        logger.info("Checking whether cake is cooked...");

        final Duration cookingTime = Duration.of(cakeBakingTime.getCookingTimeSeconds(), SECONDS);
        final String startTimeStr = cakeBakingTime.getStartTimeString() == null ? now().format(ISO_DATE_TIME) : cakeBakingTime.getStartTimeString();
        final CakeBakingTime newCakeBakingTime = cakeBakingTime.getStartTimeString() != null ? cakeBakingTime :  new CakeBakingTime(cakeBakingTime.getCookingTimeSeconds(), startTimeStr);

        final LocalDateTime startDateTime = LocalDateTime.parse(startTimeStr, ISO_DATE_TIME);
        final LocalDateTime finishTime = startDateTime.plus(cookingTime);

        ExecutionInfo nextJob = null;
        if (now().isBefore(finishTime) ) {
            logger.info("Cake not cooked yet, went in oven at {}, will be baked at {}", startDateTime, finishTime);
            // Set Job.nextTaskStartTime to when the Cake will be baked, JobExecutor won't try and run this Task
            // again until this time
            nextJob = jobUtil.sameJob(newCakeBakingTime, finishTime.atZone(systemDefault()));
        }
        return nextJob;
    }

    @Override
    public Optional<List<Long>> getRetryDurationsInSecs() {
        return Optional.of(Arrays.stream(RETRY_DURATIONS_SECONDS.split(","))
                .map(Long::valueOf).collect(Collectors.toList()));
    }
}
