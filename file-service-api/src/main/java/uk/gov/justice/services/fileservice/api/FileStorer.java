package uk.gov.justice.services.fileservice.api;


import java.io.InputStream;
import java.util.UUID;

import javax.json.JsonObject;

/**
 * Entry point for Storing files on the File Server. Inject This class into you code to use.
 */
public interface FileStorer {

    /**
     * Stores a new file on the File Server
     *
     * @param metadata          A {@link JsonObject} of the file's metadata
     * @param fileContentStream an {@link InputStream} from the file to store
     * @return The id of the newly stored file.
     */
    UUID store(final JsonObject metadata, final InputStream fileContentStream) throws FileServiceException;

    /**
     * Updates the metadata of the file stored with the specifed id
     *
     * @param fileId   The id of the file who's metadata is to be updated
     * @param metadata The metadata to update.
     */
    void updateMetadata(final UUID fileId, final JsonObject metadata) throws FileServiceException;

    /**
     * Deletes a file from the file server
     *
     * @param fileId The id of the file to be deleted.
     */
    void delete(final UUID fileId) throws FileServiceException;
}
