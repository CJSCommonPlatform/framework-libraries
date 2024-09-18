package uk.gov.moj.cpp.jobstore.service;


import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;
import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

@ApplicationScoped
public class JobService {

    @Inject
    private JobRepository jobRepository;

    @Inject
    private JobStoreConfiguration jobStoreConfiguration;

    public Stream<Job> getUnassignedJobsFor(final UUID workerId, final Priority priority) {

        final int workerJobCount = jobStoreConfiguration.getWorkerJobCount();
        jobRepository.lockJobsFor(workerId, priority, workerJobCount);

        return jobRepository.findJobsLockedTo(workerId);
    }

    public void insertJob(final Job job) {
        jobRepository.insertJob(job);
    }

    public void updateJobTaskData(final UUID jobId, final JsonObject data) {
        jobRepository.updateJobData(jobId, data);
    }

    public void updateNextTaskDetails(final UUID jobId, final String nextTask, final ZonedDateTime startTime, final Integer retryAttemptsRemaining) {
        jobRepository.updateNextTaskDetails(jobId, nextTask, toSqlTimestamp(startTime), retryAttemptsRemaining);
    }

    public void deleteJob(final UUID jobId) {
        jobRepository.deleteJob(jobId);
    }

    public void releaseJob(final UUID jobId) {
        jobRepository.releaseJob(jobId);
    }

    public void updateNextTaskRetryDetails(final UUID jobId, final ZonedDateTime startTime, final Integer retryAttemptsRemaining) {
        jobRepository.updateNextTaskRetryDetails(jobId, toSqlTimestamp(startTime), retryAttemptsRemaining);
    }
}
