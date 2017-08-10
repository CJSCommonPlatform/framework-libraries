package uk.gov.justice.services.file.alfresco.example;

import static com.jayway.jsonassert.JsonAssert.with;
import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.client.Entity.entity;
import static org.hamcrest.core.Is.is;

import uk.gov.justice.services.file.alfresco.example.util.TestProperties;

import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AlfrescoExampleIT {
    private static final String ADD_FILE_MEDIA_TYPE = "application/vnd.example.add-file+json";
    private static final String ADD_FILE_RESOURCE_URI = "http://localhost:8080/file-alfresco-example/command/files";
    private static final String ALRFRESCO_RECORDED_REQUESTS = "http://localhost:8080/alfresco-stub/recorded-requests";


    private static final TestProperties TEST_PROPERTIES = new TestProperties("test.properties");

    private Client client;


    @Before
    public void before() throws Exception {
        final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
        client = new ResteasyClientBuilder().httpEngine(new ApacheHttpClient4Engine(httpClient)).build();

    }

    @After
    public void cleanup() throws Exception {
        client.close();
    }


    @Test
    public void shouldUploadFileToAlfresco() {
        sendTo(ADD_FILE_RESOURCE_URI)
                .request()
                .post(entity(
                        jsonObject()
                                .add("fileName", "fileABC.txt")
                                .add("fileContent", "some content 123")
                                .build().toString(),
                        ADD_FILE_MEDIA_TYPE));

        with(recordedAlfrescoRequests())
                .assertThat("$[0].fileName", is("fileABC.txt"))
                .assertThat("$[0].fileContent", is("some content 123"))
                .assertThat("$[0].userId", is(TEST_PROPERTIES.value("alfresco.upload.user")));

    }


    private String recordedAlfrescoRequests() {
        final Response alrescoStubResponse = sendTo(ALRFRESCO_RECORDED_REQUESTS).request().get();
        return alrescoStubResponse.readEntity(String.class);
    }

    private JsonObjectBuilder jsonObject() {
        return createObjectBuilder();
    }

    private WebTarget sendTo(String url) {
        return client.target(url);
    }


}
