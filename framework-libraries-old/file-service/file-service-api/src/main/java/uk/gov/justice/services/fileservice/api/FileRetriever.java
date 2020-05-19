package uk.gov.justice.services.fileservice.api;


import uk.gov.justice.services.fileservice.domain.FileReference;

import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

/**
 * Entry point for retrieving files from the File Server. Inject This class into you code to use.
 */
public interface FileRetriever {

    /**
     * Retrieves the file corresponding to the specified file id,
     *
     * @param fileId The id of the file to be retrieved.
     * @return an {@link Optional} containing a {@link FileReference} to the requested file, or
     * {@code empty()} if none exists
     */
    Optional<FileReference> retrieve(final UUID fileId) throws FileServiceException;


    /**
     * Retrieves the metadata for the file corresponding to the specified file id.
     *
     * @param fileId The id of the file who's metadata is to be retrieved
     * @return an {@link Optional} containing the metadata of the requested file as a {@link JsonObject}
     * or {@code empty()} if none exists
     */
    Optional<JsonObject> retrieveMetadata(final UUID fileId) throws FileServiceException;

}
