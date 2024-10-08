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
    public void shouldGetPrioritiesAccordingToPriorityThresholds() throws Exception {

        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(0, 10, 20, 30, 40, 50, 60, 70, 80, 90);

        // 10% of the time we should get LOW priority
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(LOW));

        // 20% of the time we should get MEDIUM priority
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(MEDIUM));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(MEDIUM));

        // 70% of the time we should get HIGH priority
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities().get(0), is(HIGH));
    }

    @Test
    public void shouldSelectHighPriorityIfRandomPercentageAboveOrEqualToHighPriorityThreshold() throws Exception {

        when(jobStoreConfiguration.getJobPriorityPercentageHigh()).thenReturn(70);
        when(jobStoreConfiguration.getJobPriorityPercentageLow()).thenReturn(10);
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(29, 30, 31);

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
        when(randomPercentageProvider.getRandomPercentage()).thenReturn(9, 10, 11, 29, 30, 31);

        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(LOW, HIGH, MEDIUM)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(MEDIUM, HIGH, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(HIGH, MEDIUM, LOW)));
        assertThat(defaultJobStoreSchedulerPrioritySelector.selectOrderedPriorities(), is(List.of(HIGH, MEDIUM, LOW)));
    }
}