package uk.gov.justice.services.core.interceptor.spi;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;

import java.util.Iterator;
import java.util.ServiceLoader;

public interface InterceptorContextProvider {

    static InterceptorContextProvider provider() {
        final ServiceLoader<InterceptorContextProvider> loader = ServiceLoader.load(InterceptorContextProvider.class);
        final Iterator<InterceptorContextProvider> iterator = loader.iterator();

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new InterceptorContextProviderNotFoundException("No InterceptorContextProvider implementation found");
        }
    }

    public abstract InterceptorContext interceptorContextWithInput(final JsonEnvelope inputEnvelope);


}
