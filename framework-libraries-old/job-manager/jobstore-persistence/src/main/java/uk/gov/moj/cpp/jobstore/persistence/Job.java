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

    public Job(final UUID jobId,
               final JsonObject jobData,
               final String nextTask,
               final ZonedDateTime nextTaskStartTime,
               final Optional<UUID> workerId,
               final Optional<ZonedDateTime> workerLockTime) {
        this.jobId = jobId;
        this.workerId = workerId;
        this.workerLockTime = workerLockTime;
        this.jobData = jobData;
        this.nextTask = nextTask;
        this.nextTaskStartTime = nextTaskStartTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Job [");

        sb.append("jobId=").append(jobId)
                .append(", workerId=").append(workerId.orElse(null))
                .append(", workerLockTime=").append(workerLockTime.orElse(null))
                .append(", nextTask='").append(nextTask).append("'\\")
                .append(", nextTaskStartTime=").append(nextTaskStartTime)
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
    
    public static class Builder {

        private UUID jobId;
        private JsonObject jobData;
        private Optional<UUID> workerId;
        private Optional<ZonedDateTime> workerLockTime;
        private String nextTask;
        private ZonedDateTime nextTaskStartTime;

        private Builder(){}

        public Builder from(final Job job) {
            this.jobId = job.jobId;
            this.jobData = job.jobData;
            this.workerId = job.workerId;
            this.workerLockTime = job.workerLockTime;
            this.nextTask = job.nextTask;
            this.nextTaskStartTime = job.nextTaskStartTime;
            return this;
        }

        public Job build() {
            return new Job(jobId, jobData, nextTask, nextTaskStartTime, workerId, workerLockTime);
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
    }
}
