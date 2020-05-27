package uk.gov.justice.service.wiremock.testutil;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;

/**
 * Utility class for stubbing internal endpoints for health checks.
 */
public final class InternalEndpointMockUtils {

    /**
     * Utility class suppress instance instantiation.
     */
    private InternalEndpointMockUtils() {
    }

    public static void stubPingFor(final String serviceName) {

        final String url = String.format("/%s/internal/metrics/ping", serviceName);

        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse().withStatus(OK.getStatusCode())
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("pong")));

        stubFor(head(urlEqualTo(url))
                .willReturn(aResponse().withStatus(OK.getStatusCode())
                        .withHeader("Content-Type", APPLICATION_JSON)));
    }
}
