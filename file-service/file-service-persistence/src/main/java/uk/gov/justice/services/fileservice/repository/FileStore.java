package uk.gov.justice.services.fileservice.repository;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.io.InputStreamWrapper;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
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
    private ContentJdbcRepository contentJdbcRepository;

    @Inject
    private MetadataJdbcRepository metadataJdbcRepository;

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

            contentJdbcRepository.insert(fileId, contentStream, connection);
            metadataJdbcRepository.insert(fileId, updatedMetadata, connection);

            return fileId;
        } catch (final SQLException e) {
            throw new StorageException("Failed to insert file into file-store", e);
        }
    }

    /**
     * Finds file content and metadata for a specified file id. If no content/metadata exists
     * for that id then {@link Optional}.empty() is returned.
     *
     * If no metadata is found for that id then {@code empty()} is returned
     *
     * @param fileId the id of the file
     * @return if not found: {@link Optional}.empty()
     */
    @Transactional
    public Optional<FileReference> find(final UUID fileId) throws FileServiceException {

        Connection connection = null;
        try {
            connection = dataSourceProvider.getDatasource().getConnection();
            final Optional<JsonObject> metadata = metadataJdbcRepository.findByFileId(fileId, connection);
            final Optional<FileContent> content = contentJdbcRepository.findByFileId(fileId, connection);

            if (metadata.isEmpty()) {
                close(connection);
                if (content.isPresent()) {
                    throw new DataIntegrityException(format("No metadata found for file id '%s' but file content exists for that id", fileId));
                }
                return empty();
            }
            if (content.isEmpty()) {
                close(connection);
                throw new DataIntegrityException(format("No file content found for file id '%s' but metadata exists for that id", fileId));
            }

            final FileContent fileContent = content.get();
            final InputStreamWrapper inputStreamWrapper = new InputStreamWrapper(
                    fileContent.getContent(),
                    connection);

            return of(new FileReference(
                    fileId,
                    metadata.get(),
                    inputStreamWrapper,
                    false));
        } catch (final SQLException | StorageException e) {
            close(connection);
            throw new StorageException("Failed to read file from the file-store database", e);
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
            metadataJdbcRepository.update(fileId, metadata, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to get database connection to file-store", e);
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
            final ZonedDateTime deletedAt = clock.now();
            contentJdbcRepository.markAsDeleted(fileId, deletedAt, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to delete file from the file-store", e);
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
            return metadataJdbcRepository.findByFileId(fileId, connection);
        } catch (final SQLException | StorageException e) {
            throw new StorageException("Failed to read metadata from the database", e);
        }
    }

    private void close(final Connection connection) {

        if(connection == null) {
            return;
        }
        
        try {
            connection.close();
        } catch (final SQLException ignored) {
        }
    }
}
