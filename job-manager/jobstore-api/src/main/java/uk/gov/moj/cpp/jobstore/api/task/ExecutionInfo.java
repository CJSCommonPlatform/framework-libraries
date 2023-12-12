package uk.gov.moj.cpp.jobstore.api.task;

import uk.gov.moj.cpp.jobstore.persistence.Job;

import java.time.ZonedDateTime;

import javax.json.JsonObject;

public class ExecutionInfo {
    private final JsonObject jobData;
    private final String nextTask;
    private final ZonedDateTime nextTaskStartTime;
    private final ExecutionStatus executionStatus;
    private final boolean shouldRetry;

    public ExecutionInfo(final JsonObject jobData,
                         final String nextTask,
                         final ZonedDateTime nextTaskStartTime,
                         final ExecutionStatus executionStatus) {
        this(jobData, nextTask, nextTaskStartTime, executionStatus, false);
    }

    public ExecutionInfo(final JsonObject jobData,
                         final String nextTask,
                         final ZonedDateTime nextTaskStartTime,
                         final ExecutionStatus executionStatus,
                         boolean shouldRetry) {
        this.jobData = jobData;
        this.nextTask = nextTask;
        this.nextTaskStartTime = nextTaskStartTime;
        this.executionStatus = executionStatus;
        this.shouldRetry = shouldRetry;
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

    public static class Builder {

        private JsonObject jobData;
        private String nextTask;
        private ZonedDateTime nextTaskStartTime;
        private ExecutionStatus executionStatus;
        private boolean shouldRetry;

        private Builder() {
        }

        public ExecutionInfo.Builder from(final ExecutionInfo executionInfo) {
            this.jobData = executionInfo.jobData;
            this.nextTask = executionInfo.nextTask;
            this.nextTaskStartTime = executionInfo.nextTaskStartTime;
            this.executionStatus = executionInfo.executionStatus;
            this.shouldRetry = executionInfo.shouldRetry;
            return this;
        }


        public ExecutionInfo build() {
            final boolean exhaustTaskDetailsNotConfigured = jobData == null || nextTask == null || nextTaskStartTime == null;

            if(shouldRetry && exhaustTaskDetailsNotConfigured) {
                throw new InvalidRetryExecutionInfoException("retry exhaust task details (jobData, nextTask, nextTaskStartTime) must not be null when shouldRetry is true");
            }

            return new ExecutionInfo(jobData, nextTask, nextTaskStartTime, executionStatus, shouldRetry);
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

        public Builder fromJob(final Job job) {
            this.executionStatus = ExecutionStatus.STARTED;
            this.jobData = job.getJobData();
            this.nextTask = job.getNextTask();
            this.nextTaskStartTime = job.getNextTaskStartTime();
            return this;
        }
    }
}
