package uk.gov.justice.services.components.event.listener.interceptors;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.event.buffer.api.EventBufferService;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.stream.Stream;

import javax.inject.Inject;

/**
 * @deprecated This class should no longer be used, the SubscriptionManager now handles event
 * buffering
 */
public class EventBufferServiceCaller {

    @Inject
    EventBufferService eventBufferService;

    public Stream<InterceptorContext> streamFromEventBufferFor(final InterceptorContext interceptorContext) {

        final JsonEnvelope inputEnvelope = interceptorContext.inputEnvelope();

        return eventBufferService.currentOrderedEventsWith(inputEnvelope, interceptorContext.getComponentName())
                .map(interceptorContext::copyWithInput);
    }
}
