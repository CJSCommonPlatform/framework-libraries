package uk.gov.justice.services.file.alfresco;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.apache.openejb.util.NetworkUtil.getNextAvailablePort;

import uk.gov.justice.services.file.api.remover.FileRemover;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


public class AlfrescoFileRemoverIT {

    private static final int PORT = getNextAvailablePort();
    private static final String USER_ID = "user12348";
    private static final String WEB_CONTEXT = "/alfresco";
    private static final String DELETE_PATH = "/service/slingshot/doclib/action/file/node/workspace/SpacesStore/";
    private static final String BASE_PATH = "http://localhost:%d/%s";

    private static FileRemover fileRemover;

    @Rule
    public WireMockRule wireMock = new WireMockRule(PORT);

    @BeforeClass
    public static void beforeClass() {
        fileRemover = alfrescoFileRemoverWith(basePathWithPort(PORT));

    }

    @Test
    public void shouldDeleteFileFromAlfresco() throws Exception {
        final String alfrescoId = randomUUID().toString();
        final String deleteUrl = format("%s%s%s", WEB_CONTEXT, DELETE_PATH, alfrescoId);
        stubFor(delete(urlEqualTo(deleteUrl))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(alfrescoDeleteResponseOf(alfrescoId, "not_used.txt"))));

        fileRemover.remove(alfrescoId);

        verify(deleteRequestedFor(urlEqualTo(deleteUrl))
                .withHeader("cppuid", equalTo(USER_ID)));
    }

    private static FileRemover alfrescoFileRemoverWith(final String basePath) {
        AlfrescoFileRemover fileRemover = new AlfrescoFileRemover();
        fileRemover.alfrescoDeletePath = DELETE_PATH;
        fileRemover.alfrescoDeleteUser = USER_ID;
        fileRemover.restClient = new AlfrescoRestClient();
        fileRemover.restClient.alfrescoBaseUri = basePath;
        fileRemover.restClient.proxyType = "none";
        return fileRemover;
    }

    private static String basePathWithPort(final int port) {
        return format(BASE_PATH, port, WEB_CONTEXT);
    }

    private String alfrescoDeleteResponseOf(final String fileId, final String filename) {
        return "{\n" +
                "    \"totalResults\": 1,\n" +
                "    \"overallSuccess\": true,\n" +
                "    \"successCount\": 1,\n" +
                "    \"failureCount\": 0,\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"action\": \"deleteFile\",\n" +
                "            \"id\": \"" + filename + "\",\n" +
                "            \"nodeRef\": \"workspace://SpacesStore/" + fileId + "\",\n" +
                "            \"success\": true\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

}