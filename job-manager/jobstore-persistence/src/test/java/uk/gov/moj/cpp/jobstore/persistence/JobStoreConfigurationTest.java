package uk.gov.moj.cpp.jobstore.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;

public class JobStoreConfigurationTest {

    private JobStoreConfiguration jobStoreConfiguration = new JobStoreConfiguration();

    @Test
    public void shouldGetTheWaitTimeInMillisecondsBeforeTheJobStoreSchedulerBeanStarts() throws Exception {

        setField(jobStoreConfiguration, "timerStartWaitMilliseconds", "10000");
        assertThat(jobStoreConfiguration.getTimerStartWaitMilliseconds(), is(10000L));
    }

    @Test
    public void shouldGetTheTimeIntervalInMillisecondsBetweenJobStoreSchedulerBeanRuns() throws Exception {

        setField(jobStoreConfiguration, "timerIntervalMilliseconds", "2000");
        assertThat(jobStoreConfiguration.getTimerIntervalMilliseconds(), is(2000L));
    }

    @Test
    public void shouldGetTheJobPriorityPercentageHigh() throws Exception {

        setField(jobStoreConfiguration, "jobPriorityPercentageHigh", "70");
        assertThat(jobStoreConfiguration.getJobPriorityPercentageHigh(), is(70));
    }

    @Test
    public void shouldGetTheJobPriorityPercentageLow() throws Exception {

        setField(jobStoreConfiguration, "jobPriorityPercentageLow", "10");
        assertThat(jobStoreConfiguration.getJobPriorityPercentageLow(), is(10));
    }

    @Test
    public void shouldGetTheModuleName() throws Exception {

        setField(jobStoreConfiguration, "moduleName", "fred-bloggs");
        assertThat(jobStoreConfiguration.getModuleName(), is("fred-bloggs"));
    }

    @Test
    public void shouldGetMaxInProgressJobCount() throws Exception {

        setField(jobStoreConfiguration, "maxInProgressJobCount", "10");
        assertThat(jobStoreConfiguration.getMaxInProgressJobCount(), is(10));
    }
}