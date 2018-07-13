package uk.gov.moj.cpp.jobstore.api;


import static java.lang.Integer.parseInt;
import static java.util.Optional.empty;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.justice.services.common.configuration.Value;
import uk.gov.moj.cpp.jobstore.persistence.Job;
import uk.gov.moj.cpp.jobstore.persistence.JobRepository;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

@ApplicationScoped
public class JobService {

    @Inject
    @Value(key = "worker.job.count", defaultValue = "10")
    String jobCount;

    @Inject
    JobRepository jobRepository;

    public void lockJobsFor(final UUID jobId, final int count) {
        jobRepository.lockJobsFor(jobId, count);
    }

    public Stream<Job> getUnassignedJobsFor(final UUID workerId) {

        jobRepository.lockJobsFor(workerId, parseInt(jobCount));

        return jobRepository.findJobsLockedTo(workerId);
    }

    public void createJob(final JobRequest jobRequest) {
        jobRepository.createJob(new Job(jobRequest.getJobId(), jobRequest.getJobData(),
                jobRequest.getStartTask(), jobRequest.getStartTime(), empty(), empty(), empty()));
    }

    public void updateJobTaskData(final UUID jobId, final JsonObject data) {
        jobRepository.updateJobData(jobId, data);
    }

    public void updateNextTaskDetails(final UUID jobId, final String nextTask, final ZonedDateTime startTime) {
        jobRepository.updateNextTaskDetails(jobId, nextTask, toSqlTimestamp(startTime));
    }

    public void deleteJob(final UUID jobId) {
        jobRepository.deleteJob(jobId);
    }

    public void releaseJob(final UUID jobId) {
        jobRepository.releaseJob(jobId);
    }
}
