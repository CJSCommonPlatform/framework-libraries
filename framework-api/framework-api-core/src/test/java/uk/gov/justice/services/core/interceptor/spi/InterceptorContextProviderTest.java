package uk.gov.justice.services.core.interceptor.spi;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.junit.Test;

public class InterceptorContextProviderTest {

    @Test
    public void shouldProvideInterceptorContextProvider() throws Exception {
        final InterceptorContextProvider interceptorContextProvider = InterceptorContextProvider.provider();

        assertThat(interceptorContextProvider, notNullValue());
        assertThat(interceptorContextProvider, instanceOf(DummyInterceptorContextProvider.class));
    }

}
