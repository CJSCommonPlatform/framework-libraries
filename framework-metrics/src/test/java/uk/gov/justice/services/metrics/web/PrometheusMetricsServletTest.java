package uk.gov.justice.services.metrics.web;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrometheusMetricsServletTest {

    @Mock
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @InjectMocks
    private PrometheusMetricsServlet prometheusMetricsServlet;

    @Test
    void shouldReturnMetricsData() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter metricsJsonWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(metricsJsonWriter);
        when(response.getWriter()).thenReturn(writer);
        when(prometheusMeterRegistry.scrape()).thenReturn("metrics content");

        prometheusMetricsServlet.doGet(request, response);

        assertThat(metricsJsonWriter.toString(), is("metrics content"));
    }

    @Test
    void shouldReturn500StatusWhenMetricsWriteToResponseFails() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(prometheusMeterRegistry.scrape()).thenReturn("metrics content");
        doThrow(new IOException()).when(response).getWriter();

        prometheusMetricsServlet.doGet(request, response);

        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
    }
}