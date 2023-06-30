package uk.gov.justice.services.healthcheck.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultIgnoredHealthcheckNamesProviderTest {

    @InjectMocks
    private DefaultIgnoredHealthcheckNamesProvider defaultIgnoredHealthcheckNamesProvider;

    @Test
    public void shouldReturnEmptyListByDefault() throws Exception {

        assertThat(defaultIgnoredHealthcheckNamesProvider.getNamesOfIgnoredHealthChecks().isEmpty(), is(true));
    }
}