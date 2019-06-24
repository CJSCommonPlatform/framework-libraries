package uk.gov.justice.services.file.alfresco;

import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
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
public class AlfrescoFileRemoverTest {

    @InjectMocks
    AlfrescoFileRemover fileRemover;

    @Mock
    AlfrescoRestClient restClient;

    private static final String DELETE_PATH = "path/";
    private static final String USER_ID = "userDelete321";

    @Before
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
        when(response.getStatus()).thenReturn(responseStatus.getStatusCode());

        //when
        fileRemover.remove(alfrescoId);

        //then
        verify(restClient).delete(DELETE_PATH + alfrescoId, APPLICATION_JSON_TYPE, headersWithUserId(USER_ID));
    }
}