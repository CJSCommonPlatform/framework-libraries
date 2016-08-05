package uk.gov.justice.service.wiremock.testutil;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.apache.cxf.jaxrs.client.WebClient.create;


/**
 * Created by yli on 04/08/2016.
 */
public class InternalEndpointMockUtilsIT {

    private static final String HOST = System.getProperty("INTEGRATION_HOST_KEY", "localhost");
    private static final String SERVICE_NAME = "test-command-api";

    @Rule
    public WireMockRule wm = new WireMockRule(WireMockConfiguration.wireMockConfig());

    @Before
    public void setUp() {

        reset();
        InternalEndpointMockUtils.stubPingFor(SERVICE_NAME);
    }


    private static final String PONG = "pong";
    private static final String BASE_URL = "http://localhost:8080";


    @Test
    public void shouldStubPingForGetRequest() {
        Response response = buildWebClient(SERVICE_NAME)
                .get();
        verifyStatusEquals(response, OK);
        assertThat(response.readEntity(String.class), equalTo(PONG));
    }

    @Test
    public void shouldStubPingForHeadRequest() {
        Response response = buildWebClient(SERVICE_NAME)
                .head();
        verifyStatusEquals(response, OK);
        assertThat(response.readEntity(String.class), equalTo(""));
    }


    @Test
    public void shouldOnlyStubForGivingService() {
        WebClient client = buildWebClient("aTestService");

        verifyStatusEquals(client.get(), NOT_FOUND);
        verifyStatusEquals(client.head(), NOT_FOUND);
    }

    @Test
    public void shouldResetAllRequests() {

        reset();

        WebClient client = buildWebClient(SERVICE_NAME);

        verifyStatusEquals(client.get(), NOT_FOUND);
        verifyStatusEquals(client.head(), NOT_FOUND);
    }

    private WebClient buildWebClient(String serviceName) {
        return create(BASE_URL).path("/" + serviceName + "/internal/metrics/ping");
    }

    private void verifyStatusEquals(final Response response, final Response.Status status) {
        assertThat(response.getStatus(), is(status.getStatusCode()));
    }

    @After
    public void tearDown() {

        reset();
    }

}