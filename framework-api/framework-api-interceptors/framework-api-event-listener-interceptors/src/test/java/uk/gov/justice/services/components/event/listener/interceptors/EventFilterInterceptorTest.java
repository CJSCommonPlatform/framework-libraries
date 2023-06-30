package uk.gov.justice.services.components.event.listener.interceptors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.event.buffer.api.EventFilter;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventFilterInterceptorTest {

    @Mock
    private EventFilter filter;

    @InjectMocks
    private EventFilterInterceptor eventFilterInterceptor;

    @Test
    public void shouldCallProcessNextIfEventNameIsAcceptedByFilter() throws Exception {

        final String eventName = "event name";

        final InterceptorContext interceptorContext = mock(InterceptorContext.class);
        final InterceptorContext nextInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        final JsonEnvelope inputEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);

        when(interceptorContext.inputEnvelope()).thenReturn(inputEnvelope);
        when(inputEnvelope.metadata()).thenReturn(metadata);
        when(metadata.name()).thenReturn(eventName);
        when(filter.accepts(eventName)).thenReturn(true);
        when(interceptorChain.processNext(interceptorContext)).thenReturn(nextInterceptorContext);

        assertThat(eventFilterInterceptor.process(interceptorContext, interceptorChain), is(nextInterceptorContext));
    }

    @Test
    public void shouldNotCallProcessNextIfEventNameIsNotAcceptedByFilter() throws Exception {

        final String eventName = "event name";

        final InterceptorContext interceptorContext = mock(InterceptorContext.class);
        final InterceptorContext nextInterceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        final JsonEnvelope inputEnvelope = mock(JsonEnvelope.class);
        final Metadata metadata = mock(Metadata.class);

        when(interceptorContext.inputEnvelope()).thenReturn(inputEnvelope);
        when(inputEnvelope.metadata()).thenReturn(metadata);
        when(metadata.name()).thenReturn(eventName);
        when(filter.accepts(eventName)).thenReturn(false);

        assertThat(eventFilterInterceptor.process(interceptorContext, interceptorChain), is(interceptorContext));

        verify(interceptorChain, never()).processNext(interceptorContext);
    }
}
