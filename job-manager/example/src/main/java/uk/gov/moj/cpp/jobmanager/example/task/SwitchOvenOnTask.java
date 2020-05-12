package uk.gov.moj.cpp.jobmanager.example.task;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.OvenSettings;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("SWITCH_OVEN_ON")
public class SwitchOvenOnTask implements ExecutableTask {

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public ExecutionInfo execute(ExecutionInfo job) {

        logger.info("Switching on oven [job {}]", job);

        final OvenSettings ovenSetting = jsonObjectConverter.convert(job.getJobData(), OvenSettings.class);

        logger.info("Oven swtched on to temperarature {} degreesC, using steam function ? = {}, shelf no {} ready for cake tin", ovenSetting.getDegreesCelsius(),
                                                                                                                                 ovenSetting.isUseSteamFunction(),
                                                                                                                                 ovenSetting.getShelfNumber());
        return jobUtil.nextJob(job);
    }
}
