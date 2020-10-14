package uk.gov.justice.services.core.interceptor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

public class InterceptorChainEntryTest {

    @Test
    public void shouldConstructPriorityInterceptorType() throws Exception {
        final InterceptorChainEntry interceptorChainEntry = new InterceptorChainEntry(1, Interceptor.class);
        assertThat(interceptorChainEntry.getPriority(), is(1));
        assertThat(interceptorChainEntry.getInterceptorType(), notNullValue());
    }
}
