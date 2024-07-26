package uk.gov.moj.cpp.jobstore.service;


import uk.gov.justice.services.common.configuration.Value;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

@ApplicationScoped
public class JobService {

    @Inject
    @Value(key = "worker.job.count", defaultValue = "10")
    String jobCount;

    @Inject
    @Value(key = "max.inProgress.job.count", defaultValue = "20")
    String maxInProgressJobCount;

    @Inject
    JobRepository jobRepository;

    public Stream<Job> getUnassignedJobsFor(final UUID workerId) {

        jobRepository.lockJobsFor(workerId, parseInt(maxInProgressJobCount), parseInt(jobCount));

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
