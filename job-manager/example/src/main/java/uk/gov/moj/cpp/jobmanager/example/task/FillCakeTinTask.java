package uk.gov.moj.cpp.jobmanager.example.task;


import uk.gov.justice.services.common.converter.JsonObjectToObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.task.data.Instruction;
import uk.gov.moj.cpp.jobstore.api.annotation.Task;
import uk.gov.moj.cpp.jobstore.api.task.ExecutableTask;
import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;

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
    public ExecutionInfo execute(final ExecutionInfo executionInfo) {

        logger.info("Getting instructions for cake [executionInfo {}]", executionInfo);

        final Instruction instruction = jsonObjectConverter.convert(executionInfo.getJobData(), Instruction.class);

        logger.info("Filling cake tin according to instructions [{}]", instruction.getInstructionList());


        return jobUtil.nextJob(executionInfo);
    }
}
