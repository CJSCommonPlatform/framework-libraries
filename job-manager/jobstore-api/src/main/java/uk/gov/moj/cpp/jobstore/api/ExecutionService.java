package uk.gov.moj.cpp.jobstore.api;


import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;

import uk.gov.moj.cpp.jobstore.api.task.ExecutionInfo;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.service.JobService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExecutionService {

    @Inject
    JobService jobService;

    public void executeWith(final ExecutionInfo executionInfo) {
        jobService.insertJob(new Job(randomUUID(), executionInfo.getJobData(),
                executionInfo.getNextTask(), executionInfo.getNextTaskStartTime(), empty(), empty()));
    }
}
