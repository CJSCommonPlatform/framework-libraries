package uk.gov.moj.cpp.task.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.HIGH;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.LOW;
import static uk.gov.moj.cpp.jobstore.persistence.Priority.MEDIUM;

import uk.gov.moj.cpp.jobstore.persistence.JobStoreConfiguration;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultJobStoreSchedulerPrioritySelectorTest {

    @Mock
    private RandomPercentageProvider randomPercentageProvider;

    @Mock
    private JobStoreConfiguration jobStoreConfiguration;

    @InjectMocks
    private DefaultJobStoreSchedulerPrioritySelector defaultJobStoreSchedulerPrioritySelector;

    @Test
    public void shouldSelectHighPriorityIfRandomPercentageAboveOrEqualToHighPriorityThreshold() throws Exception {

        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(69, 70, 71);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(HIGH, MEDIUM, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(HIGH, MEDIUM, LOW)));
    }

    @Test
    public void shouldSelectLowPriorityIfRandomPercentageBelowHighPriorityThreshold() throws Exception {

        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(9, 10, 11);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(LOW, HIGH, MEDIUM)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
    }

    @Test
    public void shouldReturnMediumPriorityIfRandomPercentageBetweenTheHighAndLowThresholds() throws Exception {
        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(9, 10, 11, 69, 70, 71);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(LOW, HIGH, MEDIUM)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(HIGH, MEDIUM, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(HIGH, MEDIUM, LOW)));
    }
}