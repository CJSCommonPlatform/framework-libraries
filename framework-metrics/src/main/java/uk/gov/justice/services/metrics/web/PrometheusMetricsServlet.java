package uk.gov.justice.services.metrics.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(
        name = "promethuesServlet",
        urlPatterns = "/internal/metrics/prometheus"
)
public class PrometheusMetricsServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(PrometheusMetricsServlet.class);

    @Inject
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @Override
    public void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse response) throws IOException {
        final String metrics = prometheusMeterRegistry.scrape();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try (final PrintWriter out = response.getWriter()) {
            out.print(metrics);
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Error while writing metrics to response", e);
        }
    }
}
