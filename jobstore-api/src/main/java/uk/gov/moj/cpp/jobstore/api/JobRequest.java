package uk.gov.moj.cpp.jobstore.api;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.json.JsonObject;

public class JobRequest {

    private final UUID jobId;
    private final JsonObject jobData;
    private final String startTask;
    private final ZonedDateTime startTime;

    public JobRequest(final UUID jobId,
                      final JsonObject jobData,
                      final String startTask,
                      final ZonedDateTime startTime) {
        this.jobId = jobId;
        this.jobData = jobData;
        this.startTask = startTask;
        this.startTime = startTime;
    }

    public UUID getJobId() {
        return jobId;
    }

    public JsonObject getJobData() {
        return jobData;
    }

    public String getStartTask() {
        return startTask;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }
}