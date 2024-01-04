package uk.gov.moj.cpp.jobstore.api.task;

import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.Priority;

import java.time.ZonedDateTime;

import javax.json.JsonObject;

public class ExecutionInfo {
    private final JsonObject jobData;
    private final String nextTask;
    private final ZonedDateTime nextTaskStartTime;
    private final ExecutionStatus executionStatus;
    private final boolean shouldRetry;
    private Priority priority;

    public ExecutionInfo(final JsonObject jobData,
                         final String nextTask,
                         final ZonedDateTime nextTaskStartTime,
                         final ExecutionStatus executionStatus,
                         final Priority priority) {
        this(jobData, nextTask, nextTaskStartTime, executionStatus, false, priority);
    }

    public ExecutionInfo(final JsonObject jobData,
                         final String nextTask,
                         final ZonedDateTime nextTaskStartTime,
                         final ExecutionStatus executionStatus,
                         boolean shouldRetry,
                         final Priority priority) {
        this.jobData = jobData;
        this.nextTask = nextTask;
        this.nextTaskStartTime = nextTaskStartTime;
        this.executionStatus = executionStatus;
        this.shouldRetry = shouldRetry;
        this.priority = priority;
    }

    public String getNextTask() {
        return nextTask;
    }

    public ZonedDateTime getNextTaskStartTime() {
        return nextTaskStartTime;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public boolean isShouldRetry() {
        return shouldRetry;
    }

    public static Builder executionInfo() {
        return new Builder();
    }

    public JsonObject getJobData() {
        return jobData;
    }

    public Priority getPriority() {
        return priority;
    }

    public static class Builder {

        private JsonObject jobData;
        private String nextTask;
        private ZonedDateTime nextTaskStartTime;
        private ExecutionStatus executionStatus;
        private boolean shouldRetry;
        private Priority priority;

        private Builder() {
        }

        public ExecutionInfo.Builder from(final ExecutionInfo executionInfo) {
            this.jobData = executionInfo.jobData;
            this.nextTask = executionInfo.nextTask;
            this.nextTaskStartTime = executionInfo.nextTaskStartTime;
            this.executionStatus = executionInfo.executionStatus;
            this.shouldRetry = executionInfo.shouldRetry;
            this.priority = executionInfo.priority;
            return this;
        }


        public ExecutionInfo build() {
            final boolean exhaustTaskDetailsNotConfigured = jobData == null || nextTask == null || nextTaskStartTime == null;

            if(shouldRetry && exhaustTaskDetailsNotConfigured) {
                throw new InvalidRetryExecutionInfoException("retry exhaust task details (jobData, nextTask, nextTaskStartTime) must not be null when shouldRetry is true");
            }

            return new ExecutionInfo(jobData, nextTask, nextTaskStartTime, executionStatus, shouldRetry, priority);
        }

        public Builder withJobData(final JsonObject jobData) {
            this.jobData = jobData;
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

        public Builder withExecutionStatus(final ExecutionStatus executionStatus) {
            this.executionStatus = executionStatus;
            return this;
        }

        public Builder withShouldRetry(final boolean shouldRetry) {
            this.shouldRetry = shouldRetry;
            return this;
        }

        public Builder withPriority(final Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder fromJob(final Job job) {
            this.executionStatus = ExecutionStatus.STARTED;
            this.jobData = job.getJobData();
            this.nextTask = job.getNextTask();
            this.nextTaskStartTime = job.getNextTaskStartTime();
            this.priority = job.getPriority();
            return this;
        }
    }
}
