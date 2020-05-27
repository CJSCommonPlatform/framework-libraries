package uk.gov.justice.services.core.interceptor.spi;

import uk.gov.justice.services.core.interceptor.InterceptorContext;
import uk.gov.justice.services.messaging.JsonEnvelope;

public class DummyInterceptorContextProvider implements InterceptorContextProvider {

    @Override
    public InterceptorContext interceptorContextWithInput(final JsonEnvelope inputEnvelope) {
        return null;
    }

}
