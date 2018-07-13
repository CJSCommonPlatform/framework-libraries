package uk.gov.moj.cpp.jobmanager.example.task;


import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.Instruction;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.persistence.Job;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

@ApplicationScoped
@Task("FILL_CAKE_TIN")
public class FillCakeTinTask implements ExecutableTask {

    @Inject
    private Logger logger;

    @Inject
    private JsonObjectToObjectConverter jsonObjectConverter;

    @Inject
    private JobUtil jobUtil;

    @Override
    public Job execute(Job job) {

        logger.info("Getting instructions for cake [job {}]", job);

        final Instruction instruction = jsonObjectConverter.convert(job.getJobData(), Instruction.class);

        logger.info("Filling cake tin according to instructions [{}]", instruction.getInstructionList());


        return jobUtil.nextJob(job);
    }
}
