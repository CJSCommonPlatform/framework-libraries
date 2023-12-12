package uk.gov.moj.cpp.jobstore.persistence;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

import javax.json.JsonObject;

public interface JobRepository {
    void insertJob(final Job notificationJob);

    void updateJobData(final UUID id, final JsonObject taskData);

    void updateNextTaskDetails(final UUID id, final String nextTask, final Timestamp nextTaskDate, final Integer retryAttemptsRemaining);

    void updateNextTaskRetryDetails(final UUID id, final Timestamp nextTaskStartTime, final Integer retryAttemptsRemaining);

    void lockJobsFor(final UUID workerId, final int jobCountToLock);

    Stream<Job> findJobsLockedTo(final UUID workerId);

    void deleteJob(final UUID jobId);

    void releaseJob(final UUID jobId);
}
