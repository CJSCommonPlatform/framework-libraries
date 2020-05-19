package uk.gov.justice.services.components.event.listener.interceptors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import uk.gov.justice.services.core.interceptor.InterceptorChain;
import uk.gov.justice.services.core.interceptor.InterceptorContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class EventBufferInterceptorTest {

    @Mock
    private EventBufferServiceCaller eventBufferServiceCaller;

    @InjectMocks
    private EventBufferInterceptor eventBufferInterceptor;

    @Test
    public void shouldProcessTheInterceptorChainForEachEventOnTheEventBufferAndReturnTheNextContextForProcessing() throws Exception {

        final InterceptorContext interceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        final Stream<InterceptorContext> inputInterceptorContexts = Stream.of(
                mock(InterceptorContext.class),
                mock(InterceptorContext.class)
        );

        final List<InterceptorContext> resultInterceptorContexts = asList(
                mock(InterceptorContext.class),
                mock(InterceptorContext.class)
        );

        when(eventBufferServiceCaller.streamFromEventBufferFor(interceptorContext)).thenReturn(inputInterceptorContexts);

        when(interceptorChain.processNext(inputInterceptorContexts)).thenReturn(resultInterceptorContexts);

        assertThat(eventBufferInterceptor.process(interceptorContext, interceptorChain), is(resultInterceptorContexts.get(0)));
    }

    @Test
    public void shouldReturnTheCurrentInterceptorContextIfNoNextContextFound() throws Exception {

        final InterceptorContext interceptorContext = mock(InterceptorContext.class);
        final InterceptorChain interceptorChain = mock(InterceptorChain.class);

        final Stream<InterceptorContext> inputInterceptorContexts = Stream.of(
                mock(InterceptorContext.class),
                mock(InterceptorContext.class)
        );

        when(eventBufferServiceCaller.streamFromEventBufferFor(interceptorContext)).thenReturn(inputInterceptorContexts);

        when(interceptorChain.processNext(inputInterceptorContexts)).thenReturn(emptyList());

        assertThat(eventBufferInterceptor.process(interceptorContext, interceptorChain), is(interceptorContext));
    }
}
