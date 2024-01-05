package uk.gov.justice.services.fileservice.client;


import static java.lang.String.format;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.FileRetriever;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.FileStorer;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.repository.FileStore;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.json.JsonObject;

import org.slf4j.Logger;

/**
 * Implementation of the {@link FileStorer} and {@link FileRetriever}
 * interfaces, as the entry point into the file service.
 *
 * Currently this class is a simple wrapper around {@link FileStore}, kept
 * separate simply for isolation of responsibilities
 */
public class FileService implements FileStorer, FileRetriever {

    @Inject
    private FileStore fileStore;

    @Inject
    private FileStorePurgeConfiguration fileStorePurgeConfiguration;

    @Inject
    private UtcClock clock;

    @Inject
    private Logger logger;

    /**
     * Stores a new file in the database.
     *
     * @param metadata          The json metadata of the file to be stored as a {@link JsonObject}.
     * @param fileContentStream The {@link InputStream} of the file to be stored
     */
    @Override
    public UUID store(final JsonObject metadata, final InputStream fileContentStream) throws FileServiceException {
        return fileStore.store(metadata, new BufferedInputStream(fileContentStream));
    }

    /**
     * Finds the {@link FileReference} for the specified id, wrapped in an {@link Optional}
     * If no file found then {@code empty()} is returned.
     *
     * @param fileId the id of the required file
     * @return the {@link FileReference} or {@code empty()} if not found
     */
    @Override
    public Optional<FileReference> retrieve(final UUID fileId) throws FileServiceException {
        return fileStore.find(fileId);
    }

    /**
     * Retrieves a file's metadata
     *
     * @param fileId The id of the file who's metadata is to be retrieved
     * @return The metadata corresponding to the specified file id
     */
    @Override
    public Optional<JsonObject> retrieveMetadata(final UUID fileId) throws FileServiceException {
        return fileStore.retrieveMetadata(fileId);
    }

    /**
     * Deletes a file stored on the file store
     *
     * @param fileId The id of the file to be deleted.
     */
    @Override
    public void delete(final UUID fileId) throws FileServiceException {
        fileStore.markAsDeleted(fileId);
    }

    @Override
    public void purgeOldestSoftDeletedFiles() throws FileServiceException {

        final int purgeFilesOlderThanNumberOfDays = fileStorePurgeConfiguration.getPurgeFilesOlderThanNumberOfDays();
        final int maxNumberToDelete = fileStorePurgeConfiguration.getMaximumNumberOfFilesToPurge();
        final ZonedDateTime purgeDateTime = clock.now().minusDays(purgeFilesOlderThanNumberOfDays);

        logger.info(format("Purging a maximum of %d files from the file store deleted before %s", maxNumberToDelete, purgeDateTime));
        final int filesDeleted = fileStore.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete);

        logger.info(format("Purged %d files from the file store", filesDeleted));
    }
}
