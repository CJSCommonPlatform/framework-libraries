package uk.gov.justice.services.components.event.listener.interceptors;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.event.buffer.api.EventBufferService;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventBufferServiceCallerTest {

    @Mock
    private EventBufferService eventBufferService;

    @InjectMocks
    private EventBufferServiceCaller eventBufferServiceCaller;

    @Test
    public void shouldCallEventBufferAndConvertReturnedJsonEnvelopesIntoSeparateInterceptorContexts() throws Exception {

        final JsonEnvelope inputEnvelope = mock(JsonEnvelope.class);
        final JsonEnvelope orderedEvent_1 = mock(JsonEnvelope.class);
        final JsonEnvelope orderedEvent_2 = mock(JsonEnvelope.class);

        final InterceptorContext inputInterceptorContext = mock(InterceptorContext.class);
        final InterceptorContext copiedInterceptorContext_1 = mock(InterceptorContext.class);
        final InterceptorContext copiedInterceptorContext_2 = mock(InterceptorContext.class);

        when(inputInterceptorContext.inputEnvelope()).thenReturn(inputEnvelope);
        when(eventBufferService.currentOrderedEventsWith(inputEnvelope, inputInterceptorContext.getComponentName())).thenReturn(Stream.of(orderedEvent_1, orderedEvent_2));
        when(inputInterceptorContext.copyWithInput(orderedEvent_1)).thenReturn(copiedInterceptorContext_1);
        when(inputInterceptorContext.copyWithInput(orderedEvent_2)).thenReturn(copiedInterceptorContext_2);

        final List<InterceptorContext> interceptorContexts = eventBufferServiceCaller
                .streamFromEventBufferFor(inputInterceptorContext)
                .collect(toList());

        assertThat(interceptorContexts.size(), is(2));
        assertThat(interceptorContexts, hasItem(copiedInterceptorContext_1));
        assertThat(interceptorContexts, hasItem(copiedInterceptorContext_2));
    }
}
