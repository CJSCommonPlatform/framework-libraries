package uk.gov.justice.services.core.interceptor;

/**
 * Interface that all interceptors must implement.
 */
public interface Interceptor {

    /**
     * Process an interception with the given {@link InterceptorContext} and {@link
     * InterceptorChain}.
     *
     * @param interceptorContext the interceptor context
     * @param interceptorChain   the interceptor chain, call this with processNext after completion
     *                           of task
     * @return an interceptor context
     */
    InterceptorContext process(final InterceptorContext interceptorContext, final InterceptorChain interceptorChain);
}
