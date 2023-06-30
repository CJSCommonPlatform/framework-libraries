package uk.gov.justice.services.common.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

public class JndiBasedServiceContextNameProviderTest {

    private static final String APP_NAME = "App";

    private JndiBasedServiceContextNameProvider jndiBasedServiceContextNameProvider;

    @Test
    public void shouldReturnContextName() {
        jndiBasedServiceContextNameProvider = new JndiBasedServiceContextNameProvider();
        jndiBasedServiceContextNameProvider.appName = APP_NAME;

        final String serviceContextName = jndiBasedServiceContextNameProvider.getServiceContextName();

        assertEquals(serviceContextName, APP_NAME);
    }

    @Test
    public void shouldReturnContextNameFromConstructor() {
        jndiBasedServiceContextNameProvider = new JndiBasedServiceContextNameProvider(APP_NAME);

        final String serviceContextName = jndiBasedServiceContextNameProvider.getServiceContextName();

        assertEquals(serviceContextName, APP_NAME);
    }

    @Test
    public void shouldFailIfNameNotSet() throws Exception {
        jndiBasedServiceContextNameProvider = new JndiBasedServiceContextNameProvider();

        assertThat(jndiBasedServiceContextNameProvider.appName, is(nullValue()));

        try {
            jndiBasedServiceContextNameProvider.getServiceContextName();
            fail();
        } catch (final JndiNameNotFoundException expected) {
            assertThat(expected.getMessage(), is("No JNDI name specified for JNDI property 'java:app/AppName'"));
        }
    }
}