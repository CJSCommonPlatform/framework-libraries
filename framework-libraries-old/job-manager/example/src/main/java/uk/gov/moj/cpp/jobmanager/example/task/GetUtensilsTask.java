package uk.gov.moj.cpp.jobmanager.example.task;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.Utensils;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("GET_UTENSILS")
public class GetUtensilsTask implements ExecutableTask {

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public ExecutionInfo execute(final ExecutionInfo executionInfo) {

        logger.info("Getting utensils for cake [executionInfo {}]", executionInfo);

        final Utensils utensils = jsonObjectConverter.convert(executionInfo.getJobData(), Utensils.class);

        logger.info("Retrieved utensils and placed on kitchen surfaces [{}]", utensils.getUtensilsList());


        return jobUtil.nextJob(executionInfo);
    }
}
