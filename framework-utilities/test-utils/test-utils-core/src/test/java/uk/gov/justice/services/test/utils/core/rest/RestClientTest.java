package uk.gov.justice.services.test.utils.core.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8089)
public class RestClientTest {

    private static final int PORT = 8089;
    private static final String URL = "http://localhost:" + PORT + "/test";
    private static final String CONTENT_TYPE_VALUE = "text/xml";
    private static final String REQUEST_BODY = "<request>body</request>";
    private static final String RESOURCE_PATH = "/test";
    private static final int OK_STATUS_CODE = OK.getStatusCode();
    private static final int NO_CONTENT_CODE = NO_CONTENT.getStatusCode();
    private static final String HEADER = "Header";
    private static final String HEADER_VALUE = "HeaderValue";

    private RestClient restClient;

    @BeforeEach
    public void setup() {
        restClient = new RestClient();
    }

    @Test
    public void shouldSendCommand() {

        stubFor(post(urlEqualTo(RESOURCE_PATH))
                .withHeader(CONTENT_TYPE, equalTo(CONTENT_TYPE_VALUE))
                .withRequestBody(matching(REQUEST_BODY))
                .willReturn(aResponse().withStatus(OK_STATUS_CODE)));

        final Response response = restClient.postCommand(URL, CONTENT_TYPE_VALUE, REQUEST_BODY);

        assertThat(response.getStatus(), CoreMatchers.equalTo(OK_STATUS_CODE));
    }

    @Test
    public void shouldSendCommandWithHeaders() {

        stubFor(post(urlEqualTo(RESOURCE_PATH))
                .withHeader(CONTENT_TYPE, equalTo(CONTENT_TYPE_VALUE))
                .withHeader(HEADER, equalTo(HEADER_VALUE))
                .withRequestBody(matching(REQUEST_BODY))
                .willReturn(aResponse().withStatus(OK_STATUS_CODE)));

        final Response response = restClient.postCommand(URL, CONTENT_TYPE_VALUE, REQUEST_BODY, getHeaders());

        assertThat(response.getStatus(), org.hamcrest.CoreMatchers.equalTo(OK_STATUS_CODE));
    }

    @Test
    public void shouldSendQuery() {

        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .withHeader(ACCEPT, equalTo(CONTENT_TYPE_VALUE))
                .willReturn(aResponse().withStatus(OK_STATUS_CODE)));

        final Response response = restClient.query(URL, CONTENT_TYPE_VALUE);

        assertThat(response.getStatus(), org.hamcrest.CoreMatchers.equalTo(OK_STATUS_CODE));
    }

    @Test
    public void shouldSendQueryWithHeaders() {

        stubFor(get(urlEqualTo(RESOURCE_PATH))
                .withHeader(ACCEPT, equalTo(CONTENT_TYPE_VALUE))
                .withHeader(HEADER, equalTo(HEADER_VALUE))
                .willReturn(aResponse().withStatus(OK_STATUS_CODE)));

        final Response response = restClient.query(URL, CONTENT_TYPE_VALUE, getHeaders());

        assertThat(response.getStatus(), org.hamcrest.CoreMatchers.equalTo(OK_STATUS_CODE));
    }

    @Test
    public void shouldSendDelete() {

        stubFor(delete(urlEqualTo(RESOURCE_PATH))
                .withHeader(CONTENT_TYPE, equalTo(CONTENT_TYPE_VALUE))
                .withHeader(HEADER, equalTo(HEADER_VALUE))
                .willReturn(aResponse().withStatus(NO_CONTENT_CODE)));

        final Response response = restClient.deleteCommand(URL, CONTENT_TYPE_VALUE, getHeaders());

        assertThat(response.getStatus(), org.hamcrest.CoreMatchers.equalTo(NO_CONTENT_CODE));
    }

    private MultivaluedMap<String, Object> getHeaders() {
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle(HEADER, HEADER_VALUE);
        return headers;
    }
}
