package uk.gov.moj.cpp.jobmanager.example.task;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.Ingredients;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.persistence.Job;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("GET_INGREDIENTS")
public class GetIngredientsTask implements ExecutableTask {

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public Job execute(Job job) {

        logger.info("Getting ingredients for cake [job {}]", job);

        final Ingredients ingredients = jsonObjectConverter.convert(job.getJobData(), Ingredients.class);

        logger.info("Got ingredients for cake [{}]", ingredients.getIngredientList());

        return jobUtil.nextJob(job);
    }
}
