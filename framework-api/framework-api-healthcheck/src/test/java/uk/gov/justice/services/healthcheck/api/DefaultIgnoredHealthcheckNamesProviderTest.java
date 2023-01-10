package uk.gov.justice.services.healthcheck.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultIgnoredHealthcheckNamesProviderTest {

    @InjectMocks
    private DefaultIgnoredHealthcheckNamesProvider defaultIgnoredHealthcheckNamesProvider;

    @Test
    public void shouldReturnEmptyListByDefault() throws Exception {

        assertThat(defaultIgnoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks().isEmpty(), is(true));
    }
}