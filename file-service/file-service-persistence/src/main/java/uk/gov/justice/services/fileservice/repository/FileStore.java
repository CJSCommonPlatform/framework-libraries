package uk.gov.justice.services.fileservice.repository;

import static java.util.UUID.randomUUID;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;


/**
 * Stores/finds file content and metadata in the database.
 *
 * This class is the transaction boundary to the data access classes.
 */
public class FileStore {

    @Inject
    private FileStoreJdbcRepository fileStoreJdbcRepository;

    @Inject
    private DataSourceProvider dataSourceProvider;

    @Inject
    private MetadataUpdater metadataUpdater;

    @Inject
    private UtcClock clock;

    /**
     * Stores file content and metadata in the database.
     *
     * @param metadata      the file metadata json
     * @param contentStream InputStream from the file content
     */
    @Transactional
    public UUID store(
            final JsonObject metadata,
            final BufferedInputStream contentStream) throws FileServiceException {

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            final UUID fileId = randomUUID();

            final JsonObject updatedMetadata = metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream);

            fileStoreJdbcRepository.insert(fileId, contentStream, updatedMetadata, connection);

            return fileId;
        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection", e);
        }
    }

    /**
     * Finds file content and metadata for a specified file id. If no content/metadata exists
     * for that id then {@link Optional}.empty() is returned.
     *
     * If no metadata is found for that id then {@code empty()} is returned
     *
     * Please note: the {@link FileReference} contains a live database connection so
     * this method should always be called in a try/finally block
     *
     * @param fileId the id of the file
     * @return if not found: {@link Optional}.empty()
     */
    @Transactional
    public Optional<FileReference> find(final UUID fileId) throws FileServiceException {

        try {
            final Connection connection = dataSourceProvider.getDatasource().getConnection();
            return fileStoreJdbcRepository.findByFileId(fileId, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection", e);
        }
    }

    /**
     * Updates a file's metadata
     *
     * @param fileId   The id of the file whose metadata should be updated
     * @param metadata The metadata to be updated
     */
    @Transactional
    public void updateMetadata(final UUID fileId, final JsonObject metadata) throws FileServiceException {

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            fileStoreJdbcRepository.updateMetadata(fileId, metadata, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection", e);
        }
    }

    /**
     * Deletes a file from the file store
     *
     * @param fileId The id of the file to be deleted
     */
    @Transactional
    public void markAsDeleted(final UUID fileId) throws FileServiceException {

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            fileStoreJdbcRepository.markAsDeleted(fileId, clock.now(), connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection", e);
        }
    }

    /**
     * Retrieves metadata for a specified file
     *
     * @param fileId the id of the file who's metadata should be updated
     * @return an {@link Optional} containing the files metadata, or if not found {@code empty()}
     */
    @Transactional
    public Optional<JsonObject> retrieveMetadata(final UUID fileId) throws FileServiceException {
        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            final Optional<FileReference> fileReference = fileStoreJdbcRepository.findByFileId(fileId, connection);
            return fileReference.map(FileReference::getMetadata);

        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection", e);
        }
    }

    @Transactional
    public int purgeFilesOlderThan(final ZonedDateTime purgeDateTime, final int maxNumberToDelete) throws FileServiceException{

        try(final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            return fileStoreJdbcRepository.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection", e);
        }
    }
}
