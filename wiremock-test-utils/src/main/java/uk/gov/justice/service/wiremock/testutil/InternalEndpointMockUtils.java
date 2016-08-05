package uk.gov.justice.service.wiremock.testutil;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by yli on 02/08/2016.
 * Utility class for stubbing internal end point of health check service
 *
 */
public final class InternalEndpointMockUtils {

    /**
     * Utility class suppress instance instantiation
     */
    private InternalEndpointMockUtils() {

    }

    public static final void stubPingFor(final String serviceName) {

        String url = "/" + serviceName + "/internal/metrics/ping";

        /*
         * Stub for serving health check GET request
         * will return a "pong" string
         */
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse().withStatus(Response.Status.OK.getStatusCode())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                        .withBody("pong")));


        /*
          Stub for health check HEAD request
          will return http status 200 with no content in the response
         */
        stubFor(head(urlEqualTo(url))
                .willReturn(aResponse().withStatus(Response.Status.OK.getStatusCode())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                ));

    }
}
