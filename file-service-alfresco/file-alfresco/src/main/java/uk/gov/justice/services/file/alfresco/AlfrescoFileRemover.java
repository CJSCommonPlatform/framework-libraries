package uk.gov.justice.services.file.alfresco;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static uk.gov.justice.services.file.alfresco.Headers.headersWithUserId;

import uk.gov.justice.services.common.configuration.GlobalValue;
import uk.gov.justice.services.file.api.FileOperationException;
import uk.gov.justice.services.file.api.remover.FileRemover;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AlfrescoFileRemover implements FileRemover {

    @Inject
    @GlobalValue(key = "alfrescoDeletePath", defaultValue = "/service/slingshot/doclib/action/file/node/workspace/SpacesStore/")
    String alfrescoDeletePath;

    @Inject
    @GlobalValue(key = "alfrescoDeleteUser")
    String alfrescoDeleteUser;

    @Inject
    AlfrescoRestClient restClient;

    @Override
    public void remove(final String fileId) {
        try {
            final Response response = restClient
                    .delete(format("%s%s", alfrescoDeletePath, fileId), APPLICATION_JSON_TYPE, headersWithUserId(alfrescoDeleteUser));
            if (response.getStatusInfo().getFamily().compareTo(Response.Status.Family.SUCCESSFUL) != 0) {
                throw new FileOperationException(format("Error while deleting the document. Code:%d, Reason:%s", response.getStatus(), response.getStatusInfo().getReasonPhrase()));
            }
        } catch (ProcessingException e) {
            throw new FileOperationException("Error deleting file from Alfresco", e);
        }
    }
}
