package uk.gov.moj.cpp.jobstore.persistence;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.justice.services.common.util.UtcClock;

import java.time.ZonedDateTime;

import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JobTest {

    public static final String NEXT_TASK = "nextTask";
    private JsonObject jobData;
    private ZonedDateTime nextTaskStartTime;

    @BeforeEach
    public void setup() {
        jobData = getJsonBuilderFactory().createObjectBuilder().build();
        nextTaskStartTime = new UtcClock().now();
    }


    @Test
    public void shouldBuildNewJobFromExistingOne() {
        final Priority high = HIGH;
        final int retryAttemptsRemaining = 1;
        final Job originalJob = new Job(
                randomUUID(),
                jobData,
                NEXT_TASK,
                nextTaskStartTime,
                empty(),
                empty(),
                retryAttemptsRemaining,
                high);

        final Job copiedExecutionInfo = Job.job().from(originalJob).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
        assertThat(copiedExecutionInfo.getRetryAttemptsRemaining(), is(retryAttemptsRemaining));
        assertThat(copiedExecutionInfo.getPriority(), is(high));
    }

}