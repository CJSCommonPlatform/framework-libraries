package uk.gov.moj.cpp.jobmanager.example.task;

import static uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow.CAKE_MADE;
import static uk.gov.moj.cpp.jobstore.persistence.Job.job;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.COMPLETED;
import static uk.gov.moj.cpp.jobstore.persistence.JobStatus.NEXT_STEP;

import uk.gov.justice.services.common.converter.ObjectToJsonObjectConverter;
import uk.gov.moj.cpp.jobmanager.example.MakeCakeWorkflow;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobStatus;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.inject.Inject;

public class JobUtil {

    @Inject
    ObjectToJsonObjectConverter objectConverter;


    public Job nextJob(final Job lastJob) {

        final MakeCakeWorkflow nextStep = MakeCakeWorkflow.nextTask(MakeCakeWorkflow.valueOf(lastJob.getNextTask()));

        final JobStatus nextJobStatus = MakeCakeWorkflow.valueOf(lastJob.getNextTask()) == CAKE_MADE ? COMPLETED : NEXT_STEP;

        return job().from(lastJob)
                .withJobData(objectConverter.convert(nextStep.getTaskData()))
                .withNextTask(nextStep.toString())
                .withNextTaskStartTime(ZonedDateTime.now())
                .withJobStatus(Optional.of(nextJobStatus))
                .build();


    }

    public Job sameJob(final Job lastJob, final JobStatus jobStatus, final Object jobData, final ZonedDateTime nextTaskStartTime) {

        return job().withJobId(lastJob.getJobId())
                .withJobData(objectConverter.convert(jobData))
                .withNextTask(lastJob.getNextTask())
                .withNextTaskStartTime(nextTaskStartTime)
                .withJobStatus(Optional.of(jobStatus))
                .build();

    }
}
