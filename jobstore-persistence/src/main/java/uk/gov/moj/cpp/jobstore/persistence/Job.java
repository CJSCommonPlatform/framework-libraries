package uk.gov.moj.cpp.jobstore.persistence;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

public class Job {

    private final UUID jobId;
    private final JsonObject jobData;
    private final Optional<UUID> workerId;
    private final Optional<ZonedDateTime> workerLockTime;
    private final String nextTask;
    private final ZonedDateTime nextTaskStartTime;
    private final Optional<JobStatus> jobStatus;

    public Job(final UUID jobId,
               final JsonObject jobData,
               final String nextTask,
               final ZonedDateTime nextTaskStartTime,
               final Optional<UUID> workerId,
               final Optional<ZonedDateTime> workerLockTime,
               final Optional<JobStatus> jobStatus) {
        this.jobId = jobId;
        this.workerId = workerId;
        this.workerLockTime = workerLockTime;
        this.jobData = jobData;
        this.nextTask = nextTask;
        this.nextTaskStartTime = nextTaskStartTime;
        this.jobStatus = jobStatus;
    }

    public Job(final Job job, final Optional<JobStatus> jobStatus) {
        this(job.getJobId(), job.getJobData(), job.getNextTask(), job.getNextTaskStartTime(), job.getWorkerId(), job.getWorkerLockTime(), jobStatus);
    }

    public Job(final Job job, final JsonObject jobData) {
        this(job, jobData, job.getNextTaskStartTime(), job.getJobStatus());
    }

    public Job(final Job job, final ZonedDateTime nextTaskStartTime) {
        this(job, job.getJobData(), nextTaskStartTime, job.getJobStatus());
    }

    public Job(final Job job, final JsonObject jobData, final ZonedDateTime nextTaskStartTime, final Optional<JobStatus> jobStatus) {
        this(job.getJobId(), jobData, job.getNextTask(), nextTaskStartTime, job.getWorkerId(), job.getWorkerLockTime(), jobStatus);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Job [");

        sb.append("jobId=").append(jobId)
                .append(", workerId=").append(workerId.get())
                .append(", workerLockTime=").append(workerLockTime.get())
                .append(", nextTask='").append(nextTask).append("'\\")
                .append(", nextTaskStartTime=").append(nextTaskStartTime)
                .append(", jobStatus=").append(jobStatus.get())
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

    public Optional<JobStatus> getJobStatus() {
        return jobStatus;
    }
}
