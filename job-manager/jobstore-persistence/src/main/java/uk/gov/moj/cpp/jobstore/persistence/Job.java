package uk.gov.moj.cpp.jobstore.persistence;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

public class Job {

    private final UUID jobId;
    private final Optional<UUID> workerId;
    private final Optional<ZonedDateTime> workerLockTime;
    private final String nextTask;
    private final ZonedDateTime nextTaskStartTime;
    private final JsonObject jobData;
    private final int retryAttemptsRemaining;
    private final Priority priority;

    public Job(final UUID jobId,
               final JsonObject jobData,
               final String nextTask,
               final ZonedDateTime nextTaskStartTime,
               final Optional<UUID> workerId,
               final Optional<ZonedDateTime> workerLockTime,
               final Integer retryAttemptsRemaining,
               final Priority priority) {
        this.jobId = jobId;
        this.workerId = workerId;
        this.workerLockTime = workerLockTime;
        this.jobData = jobData;
        this.nextTask = nextTask;
        this.nextTaskStartTime = nextTaskStartTime;
        this.retryAttemptsRemaining = retryAttemptsRemaining;
        this.priority = priority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Job [");

        sb.append("jobId=").append(jobId)
                .append(", workerId=").append(workerId.orElse(null))
                .append(", workerLockTime=").append(workerLockTime.orElse(null))
                .append(", nextTask='").append(nextTask).append("'\\")
                .append(", nextTaskStartTime=").append(nextTaskStartTime)
                .append(", retryAttemptsRemaining=").append(retryAttemptsRemaining)
                .append(", priority=").append(priority)
                .append("]");

        return sb.toString();
    }

    public UUID getJobId() {
        return jobId;
    }

    public JsonObject getJobData() {
        return jobData;
    }

    public Optional<UUID> getWorkerId() {
        return workerId;
    }

    public Optional<ZonedDateTime> getWorkerLockTime() {
        return workerLockTime;
    }

    public String getNextTask() {
        return nextTask;
    }

    public ZonedDateTime getNextTaskStartTime() {
        return nextTaskStartTime;
    }

    public static Builder job() {
        return new Builder();
    }

    public Integer getRetryAttemptsRemaining() {
        return retryAttemptsRemaining;
    }

    public Priority getPriority() {
        return priority;
    }

    public static class Builder {

        private UUID jobId;
        private JsonObject jobData;
        private Optional<UUID> workerId;
        private Optional<ZonedDateTime> workerLockTime;
        private String nextTask;
        private ZonedDateTime nextTaskStartTime;
        private Integer retryAttemptsRemaining;
        private Priority priority;

        private Builder(){}

        public Builder from(final Job job) {
            this.jobId = job.jobId;
            this.jobData = job.jobData;
            this.workerId = job.workerId;
            this.workerLockTime = job.workerLockTime;
            this.nextTask = job.nextTask;
            this.nextTaskStartTime = job.nextTaskStartTime;
            this.retryAttemptsRemaining = job.retryAttemptsRemaining;
            this.priority = job.priority;
            return this;
        }

        public Job build() {
            return new Job(jobId, jobData, nextTask, nextTaskStartTime, workerId, workerLockTime, retryAttemptsRemaining, priority);
        }

        public Builder withJobId(final UUID jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder withJobData(final JsonObject jobData) {
            this.jobData = jobData;
            return this;
        }

        public Builder withWorkerId(final Optional<UUID> workerId) {
            this.workerId = workerId;
            return this;
        }

        public Builder withWorkerLockTime(final Optional<ZonedDateTime> workerLockTime) {
            this.workerLockTime = workerLockTime;
            return this;
        }

        public Builder withNextTask(final String nextTask) {
            this.nextTask = nextTask;
            return this;
        }

        public Builder withNextTaskStartTime(final ZonedDateTime nextTaskStartTime) {
            this.nextTaskStartTime = nextTaskStartTime;
            return this;
        }

        public Builder withRetryAttemptsRemaining(final Integer retryAttemptsRemaining) {
            this.retryAttemptsRemaining = retryAttemptsRemaining;
            return this;
        }

        public Builder withPriority(final Priority priority) {
            this.priority = priority;
            return this;
        }
    }
}
