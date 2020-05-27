package uk.gov.moj.cpp.jobstore.api.task;

import uk.gov.moj.cpp.jobstore.persistence.Job;

import java.time.ZonedDateTime;

import javax.json.JsonObject;

public class ExecutionInfo {
    private final JsonObject jobData;
    private final String nextTask;
    private final ZonedDateTime nextTaskStartTime;
    private final ExecutionStatus executionStatus;

    public ExecutionInfo(final JsonObject jobData,
                         final String nextTask,
                         final ZonedDateTime nextTaskStartTime,
                         final ExecutionStatus executionStatus) {
        this.jobData = jobData;
        this.nextTask = nextTask;
        this.nextTaskStartTime = nextTaskStartTime;
        this.executionStatus = executionStatus;
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

        private Builder() {
        }

        public ExecutionInfo.Builder from(final ExecutionInfo executionInfo) {
            this.jobData = executionInfo.jobData;
            this.nextTask = executionInfo.nextTask;
            this.nextTaskStartTime = executionInfo.nextTaskStartTime;
            this.executionStatus = executionInfo.executionStatus;
            return this;
        }


        public ExecutionInfo build() {
            return new ExecutionInfo(jobData, nextTask, nextTaskStartTime, executionStatus);
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

        public Builder fromJob(final Job job) {
            this.executionStatus = ExecutionStatus.STARTED;
            this.jobData = job.getJobData();
            this.nextTask = job.getNextTask();
            this.nextTaskStartTime = job.getNextTaskStartTime();
            return this;
        }
    }
}
