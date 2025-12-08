package uk.gov.justice.services.core.interceptor.spi;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ServiceLoader;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InterceptorContextProviderTest {

    @Test
    @Order(1)
    public void shouldThrowExceptionIfThereIsNoCandidate() throws Exception {
        try (MockedStatic<ServiceLoader> serviceLoaderMockedStatic = mockStatic(ServiceLoader.class)) {
            final ServiceLoader serviceLoader = mock(ServiceLoader.class);
            serviceLoaderMockedStatic.when(() -> ServiceLoader.load(InterceptorContextProvider.class)).thenReturn(serviceLoader);
            when(serviceLoader.iterator()).thenReturn(emptyList().iterator());

            Assertions.assertThrows(InterceptorContextProviderNotFoundException.class, InterceptorContextProvider::provider);
        }
    }
    @Test
    public void shouldProvideInterceptorContextProvider() throws Exception {
        final InterceptorContextProvider interceptorContextProvider = InterceptorContextProvider.provider();

        assertThat(interceptorContextProvider, notNullValue());
        assertThat(interceptorContextProvider, instanceOf(DummyInterceptorContextProvider.class));
    }

    @Test
    public void shouldCacheInterceptorProvider() throws Exception {
        final InterceptorContextProvider interceptorContextProviderFirst = InterceptorContextProvider.provider();
        final InterceptorContextProvider interceptorContextProviderSecond = InterceptorContextProvider.provider();

        assertThat(interceptorContextProviderFirst, equalTo(interceptorContextProviderSecond));
    }



}
