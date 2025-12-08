package uk.gov.moj.cpp.jobstore.api.task;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.messaging.JsonObjects.getJsonBuilderFactory;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.persistence.Job;

import java.time.ZonedDateTime;

import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ExecutionInfoTest {

    public static final String NEXT_TASK = "nextTask";
    private JsonObject jobData;
    private ZonedDateTime nextTaskStartTime;

    @BeforeEach
    public void setup() {
        jobData = getJsonBuilderFactory().createObjectBuilder().build();
        nextTaskStartTime = new UtcClock().now();
    }


    @Test
    public void shouldPopulateBuilderFromPreviousExecutionInfo() {
        final ExecutionInfo originalExecutionInfo = new ExecutionInfo(jobData,
                NEXT_TASK,
                nextTaskStartTime,
                ExecutionStatus.INPROGRESS,
                HIGH);

        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().from(originalExecutionInfo).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
        assertThat(copiedExecutionInfo.getExecutionStatus(), is(ExecutionStatus.INPROGRESS));
    }

    @Test
    public void shouldPopulateBuilderFromJob() {
        final Job job = new Job(randomUUID(), jobData, NEXT_TASK, nextTaskStartTime, empty(), empty(), 0, HIGH);

        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().fromJob(job).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
        assertThat(copiedExecutionInfo.getExecutionStatus(), is(ExecutionStatus.STARTED));
    }

    @Test
    public void shouldSetTheJobdata() {
        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withJobData(jobData).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
    }

    @Test
    public void shouldSetTheNextTask() {
        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withNextTask(NEXT_TASK).build();

        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
    }

    @Test
    public void shouldSetTheNextTaskStartTime() {
        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withNextTaskStartTime(nextTaskStartTime).build();

        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
    }

    @Test
    public void shouldSetTheExecutionStatus() {
        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withExecutionStatus(ExecutionStatus.STARTED).build();

        assertThat(copiedExecutionInfo.getExecutionStatus(), is(ExecutionStatus.STARTED));
    }

    @Test
    public void shouldBuildWhenShouldRetryIsTrueAndExhaustTaskDetailsSupplied() {
        final ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withShouldRetry(true)
                .withNextTask("last-task")
                .withNextTaskStartTime(ZonedDateTime.now())
                .withJobData(jobData)
                .build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
    }

    @Test
    public void shouldThrowExceptionWhenRetryExhaustTaskJobDataIsNullAndShouldRetryIsTrue() {
        final InvalidRetryExecutionInfoException e = assertThrows(InvalidRetryExecutionInfoException.class, () -> ExecutionInfo.executionInfo().withShouldRetry(true)
                .withNextTask("last-task")
                .withNextTaskStartTime(ZonedDateTime.now())
                .withJobData(null)
                .build());

        assertThat(e.getMessage(), is("retry exhaust task details (jobData, nextTask, nextTaskStartTime) must not be null when shouldRetry is true"));
    }

    @Test
    public void shouldThrowExceptionWhenRetryExhaustTaskStartTimeIsNullAndShouldRetryIsTrue() {
        final InvalidRetryExecutionInfoException e = assertThrows(InvalidRetryExecutionInfoException.class, () -> ExecutionInfo.executionInfo().withShouldRetry(true)
                .withNextTask("last-task")
                .withNextTaskStartTime(null)
                .withJobData(jobData)
                .build());

        assertThat(e.getMessage(), is("retry exhaust task details (jobData, nextTask, nextTaskStartTime) must not be null when shouldRetry is true"));
    }

    @Test
    public void shouldThrowExceptionWhenRetryExhaustTaskNameIsNullAndShouldRetryIsTrue() {
        final InvalidRetryExecutionInfoException e = assertThrows(InvalidRetryExecutionInfoException.class, () -> ExecutionInfo.executionInfo().withShouldRetry(true)
                .withNextTask(null)
                .withNextTaskStartTime(ZonedDateTime.now())
                .withJobData(jobData)
                .build());

        assertThat(e.getMessage(), is("retry exhaust task details (jobData, nextTask, nextTaskStartTime) must not be null when shouldRetry is true"));
    }
}