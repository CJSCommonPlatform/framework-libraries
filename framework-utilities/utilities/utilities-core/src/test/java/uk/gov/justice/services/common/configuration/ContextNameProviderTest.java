package uk.gov.justice.services.common.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContextNameProviderTest {

    @Mock
    private ServiceContextNameProvider serviceContextNameProvider;

    @InjectMocks
    private ContextNameProvider contextNameProvider;

    @Test
    public void shouldLazilyParseOutTheContextNameFromTheAppName() throws Exception {

        final String appName = "people-command-api";

        when(serviceContextNameProvider.getServiceContextName()).thenReturn(appName);

        assertThat(contextNameProvider.getContextName(), is("people"));
        assertThat(contextNameProvider.getContextName(), is("people"));
        assertThat(contextNameProvider.getContextName(), is("people"));
        assertThat(contextNameProvider.getContextName(), is("people"));
        assertThat(contextNameProvider.getContextName(), is("people"));
        assertThat(contextNameProvider.getContextName(), is("people"));
        assertThat(contextNameProvider.getContextName(), is("people"));

        verify(serviceContextNameProvider, times(1)).getServiceContextName();
    }

    @Test
    public void shouldJustReturnTheAppNameIfTheAppNameContainsNoDashes() throws Exception {

        final String appName = "application";

        when(serviceContextNameProvider.getServiceContextName()).thenReturn(appName);

        assertThat(contextNameProvider.getContextName(), is(appName));
    }
}