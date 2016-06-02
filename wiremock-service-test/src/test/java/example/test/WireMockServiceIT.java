package example.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configure;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.apache.cxf.jaxrs.client.WebClient.create;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * An example test war to show how to intereact with Jee deployed WireMock server war
 *
 */
public class WireMockServiceIT {

    private static final String RESPONSE = "Test Worked";
    private static final String PATH = "/test/two";
    private static final String BASE_URL = "http://localhost:8080";

    @Before
    public void setup() {
        configure();
        stubFor(get(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withStatus(ACCEPTED.getStatusCode())
                        .withBody(RESPONSE)));
    }

    @Test
    public void shouldStubWebService() {
        Response response = create(BASE_URL)
                .path(PATH)
                .get();
        assertThat(response.getStatus(), is(ACCEPTED.getStatusCode()));
        assertThat(response.readEntity(String.class), equalTo(RESPONSE));
    }

    @Test
    public void shouldResetAllRequests() {
        reset();
        Response response = create(BASE_URL)
                .path(PATH)
                .get();
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }

    @After
    public void tearDown() {
        reset();
    }
}
