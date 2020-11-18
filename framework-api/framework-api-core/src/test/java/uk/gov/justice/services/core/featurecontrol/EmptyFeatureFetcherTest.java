package uk.gov.justice.services.core.featurecontrol;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmptyFeatureFetcherTest {

    @InjectMocks
    private EmptyFeatureFetcher emptyFeatureFetcher;

    @Test
    public void shouldReturnAnEmptyListOfFeatures() throws Exception {

        assertThat(emptyFeatureFetcher.fetchFeatures().isEmpty(), is(true));
    }
}