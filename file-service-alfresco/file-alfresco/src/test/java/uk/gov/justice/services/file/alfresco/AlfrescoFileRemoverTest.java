package uk.gov.justice.services.file.alfresco;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.file.alfresco.Headers.headersWithUserId;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AlfrescoFileRemoverTest {

    @InjectMocks
    private AlfrescoFileRemover fileRemover;

    @Mock
    private AlfrescoRestClient restClient;

    private static final String DELETE_PATH = "path/";
    private static final String USER_ID = "userDelete321";

    @BeforeEach
    public void beforeTest() {
        fileRemover.alfrescoDeletePath = DELETE_PATH;
        fileRemover.alfrescoDeleteUser = USER_ID;
    }

    @Test
    public void shouldInvokeDeleteFromAlfresco() throws Exception {
        //given
        final String alfrescoId = randomUUID().toString();
        final Response.Status responseStatus = Response.Status.OK;
        final Response response = mock(Response.class);
        when(restClient.delete(contains(alfrescoId), any(), any()))
                .thenReturn(response);
        when(response.getStatusInfo()).thenReturn(responseStatus);

        //when
        fileRemover.remove(alfrescoId);

        //then
        verify(restClient).delete(DELETE_PATH + alfrescoId, APPLICATION_JSON_TYPE, headersWithUserId(USER_ID));
    }
}