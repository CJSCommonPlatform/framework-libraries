package uk.gov.justice.services.core.interceptor.spi;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

public interface InterceptorContextProvider  {
    AtomicReference<InterceptorContextProvider> cachedProvider = new AtomicReference<>();

    static InterceptorContextProvider provider() {
        if (cachedProvider.get() != null) {
            return cachedProvider.get();
        }
        final ServiceLoader<InterceptorContextProvider> loader = ServiceLoader.load(InterceptorContextProvider.class);
        final Iterator<InterceptorContextProvider> iterator = loader.iterator();

        if (iterator.hasNext()) {
            final InterceptorContextProvider interceptorContextProvider = iterator.next();
            cachedProvider.set(interceptorContextProvider);
            return interceptorContextProvider;
        } else {
            throw new InterceptorContextProviderNotFoundException("No InterceptorContextProvider implementation found");
        }
    }

    public abstract InterceptorContext interceptorContextWithInput(final JsonEnvelope inputEnvelope);


}
