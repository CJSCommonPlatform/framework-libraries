package uk.gov.justice.services.components.event.listener.interceptors;


import uk.gov.justice.services.core.interceptor.Interceptor;
import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.event.buffer.api.EventBufferService;

import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

/**
 * @deprecated This class should no longer be used, the SubscriptionManager now handles event
 * buffering
 */
@Deprecated
public class EventBufferInterceptor implements Interceptor {

    private static final int FIRST_CONTEXT = 0;

    @Inject
    EventBufferServiceCaller eventBufferServiceCaller;

    @Override
    public InterceptorContext process(final InterceptorContext interceptorContext, final InterceptorChain interceptorChain) {

        final Stream<InterceptorContext> interceptorContexts = eventBufferServiceCaller.streamFromEventBufferFor(interceptorContext);
        final List<InterceptorContext> resultContexts = interceptorChain.processNext(interceptorContexts);

        if (resultContexts.isEmpty()) {
            return interceptorContext;
        }

        return resultContexts.get(FIRST_CONTEXT);
    }

}
