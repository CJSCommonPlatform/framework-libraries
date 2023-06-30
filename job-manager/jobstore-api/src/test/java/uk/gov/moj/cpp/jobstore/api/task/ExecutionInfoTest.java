package uk.gov.moj.cpp.jobstore.api.task;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.moj.cpp.jobstore.persistence.Job;

import java.time.ZonedDateTime;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ExecutionInfoTest {

    public static final String NEXT_TASK = "nextTask";
    private JsonObject jobData;
    private ZonedDateTime nextTaskStartTime;

    @BeforeEach
    public void setup() {
        jobData = Json.createObjectBuilder().build();
        nextTaskStartTime = new UtcClock().now();
    }


    @Test
    public void shouldPopulateBuilderFromPreviousExecutionInfo() {
        ExecutionInfo originalExecutionInfo = new ExecutionInfo(jobData,
                NEXT_TASK,
                nextTaskStartTime,
                ExecutionStatus.INPROGRESS);

        ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().from(originalExecutionInfo).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
        assertThat(copiedExecutionInfo.getExecutionStatus(), is(ExecutionStatus.INPROGRESS));
    }

    @Test
    public void shouldPopulateBuilderFromJob() {
        Job job = new Job(randomUUID(), jobData, NEXT_TASK, nextTaskStartTime, empty(), empty());

        ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().fromJob(job).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
        assertThat(copiedExecutionInfo.getExecutionStatus(), is(ExecutionStatus.STARTED));
    }

    @Test
    public void shouldSetTheJobdata() {
        ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withJobData(jobData).build();

        assertThat(copiedExecutionInfo.getJobData(), is(jobData));
    }

    @Test
    public void shouldSetTheNextTask() {
        ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withNextTask(NEXT_TASK).build();

        assertThat(copiedExecutionInfo.getNextTask(), is(NEXT_TASK));
    }

    @Test
    public void shouldSetTheNextTaskStartTime() {
        ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withNextTaskStartTime(nextTaskStartTime).build();

        assertThat(copiedExecutionInfo.getNextTaskStartTime(), is(nextTaskStartTime));
    }

    @Test
    public void shouldSetTheExecutionStatus() {
        ExecutionInfo copiedExecutionInfo = ExecutionInfo.executionInfo().withExecutionStatus(ExecutionStatus.STARTED).build();

        assertThat(copiedExecutionInfo.getExecutionStatus(), is(ExecutionStatus.STARTED));
    }
}