package uk.gov.justice.services.file.alfresco;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.file.alfresco.Headers.headersWithUserId;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AlfrescoFileSenderTest {

    @InjectMocks
    AlfrescoFileSender fileSender;

    @Mock
    AlfrescoRestClient restClient;

    private static final String UPLOAD_PATH = "/service/case/upload";
    private static final String USER_ID = "user1234";

    @Before
    public void beforeTest() {
        fileSender.alfrescoUploadPath = UPLOAD_PATH;
        fileSender.alfrescoUploadUser = USER_ID;
    }

    @Test
    public void shouldSendFileToAlfresco() throws Exception {
        //given
        final String alfrescoId = randomUUID().toString();
        final Response.Status responseStatus = Response.Status.OK;
        final Response response = mock(Response.class);

        when(restClient.post(any(), any(), any(), any()))
                .thenReturn(response);
        when(response.getStatusInfo()).thenReturn(responseStatus);
        when(response.readEntity(String.class)).thenReturn(alfrescoResponseOf(alfrescoId, TEXT_PLAIN));

        //when
        fileSender.send("file.txt", toInputStream("abcd1243"));

        //then
        verify(restClient).post(eq(UPLOAD_PATH), eq(MULTIPART_FORM_DATA_TYPE), eq(headersWithUserId(USER_ID)), any());
    }

    private String alfrescoResponseOf(final String fileId, final String fileMimeType) {
        return "{\n" +
                "  \"nodeRef\": \"workspace://SpacesStore/" + fileId + "\",\n" +
                "  \"fileName\": \"test.txt\",\n" +
                "  \"fileMimeType\": \"" + fileMimeType + "\",\n" +
                "  \"status\": {\n" +
                "    \"code\": \"200\",\n" +
                "    \"name\": \"OK\",\n" +
                "    \"description\": \"Success\"\n" +
                "  }\n" +
                "}";
    }
}