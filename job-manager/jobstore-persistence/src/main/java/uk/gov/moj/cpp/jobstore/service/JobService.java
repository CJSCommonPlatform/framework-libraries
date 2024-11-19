package uk.gov.moj.cpp.jobstore.service;


import static java.util.stream.Stream.empty;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.justice.services.common.configuration.Value;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;
import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;
import uk.gov.moj.cpp.jobstore.persistence.Priority;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

@ApplicationScoped
public class JobService {

    @Inject
    private JobRepository jobRepository;

    @Inject
    private JobStoreConfiguration jobStoreConfiguration;

    public Stream<Job> getUnassignedJobsFor(final UUID workerId, final List<Priority> orderedPriorities) {

        final int workerJobCount = jobStoreConfiguration.getWorkerJobCount();
        final int maxInProgressJobCount = jobStoreConfiguration.getMaxInProgressJobCount();
        final Priority firstPriority = orderedPriorities.get(0);

        int rowsAffected = jobRepository.lockJobsFor(workerId, firstPriority, maxInProgressJobCount, workerJobCount);
        if (rowsAffected == 0) {
            final Priority secondPriority = orderedPriorities.get(1);
            rowsAffected = jobRepository.lockJobsFor(workerId, secondPriority, maxInProgressJobCount, workerJobCount);
            if (rowsAffected == 0) {
                final Priority thirdPriority = orderedPriorities.get(2);
                rowsAffected = jobRepository.lockJobsFor(workerId, thirdPriority, maxInProgressJobCount, workerJobCount);
            }
        }
        if (rowsAffected == 0) {
            return empty();
        }

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
