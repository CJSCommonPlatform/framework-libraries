package uk.gov.moj.cpp.jobmanager.example.task;

import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.SliceCake;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("CAKE_MADE")
public class SliceAndEatCakeTask implements ExecutableTask {

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public ExecutionInfo execute(ExecutionInfo job) {

        logger.info("Cake has been made, time to serve ! [job {}]", job);

        final SliceCake sliceCake = jsonObjectConverter.convert(job.getJobData(), SliceCake.class);

        logger.info("Sliced cake into {} slices, please help yourself !", sliceCake.getNumberOfSlices());

        return jobUtil.nextJob(job);
    }
}
