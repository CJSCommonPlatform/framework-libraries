package uk.gov.justice.services.fileservice.repository;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.io.InputStreamWrapper;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
    ContentJdbcRepository contentJdbcRepository;

    @Inject
    MetadataJdbcRepository metadataJdbcRepository;

    @Inject
    DataSourceProvider dataSourceProvider;

    @Inject
    MetadataUpdater metadataUpdater;

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
            throw new StorageException("Failed to insert file into database", e);
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

            if (!metadata.isPresent()) {
                close(connection);
                if (content.isPresent()) {
                    throw new DataIntegrityException("No metadata found for file id " + fileId + " but file content exists for that id");
                }
                return empty();
            }
            if (!content.isPresent()) {
                close(connection);
                throw new DataIntegrityException("No file content found for file id " + fileId + " but metadata exists for that id");
            }

            final FileContent fileContent = content.get();
            final InputStreamWrapper inputStreamWrapper = new InputStreamWrapper(
                    fileContent.getContent(),
                    connection);
            final Boolean deleted = fileContent.isDeleted();

            return of(new FileReference(
                    fileId,
                    metadata.get(),
                    inputStreamWrapper,
                    deleted));
        } catch (final SQLException | StorageException e) {
            close(connection);
            throw new StorageException("Failed to read File from the database", e);
        }
    }

    /**
     * Updates a file's metadata
     *
     * @param fileId   The id of the file who's metadata should be updated
     * @param metadata The metadata to be updated
     */
    @Transactional
    public void updateMetadata(final UUID fileId, final JsonObject metadata) throws FileServiceException {

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            metadataJdbcRepository.update(fileId, metadata, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to update metadata", e);
        }
    }

    /**
     * Deletes a file from the file store
     *
     * @param fileId The id of the file to be deleted
     */
    @Transactional
    public void delete(final UUID fileId) throws FileServiceException {

        try (final Connection connection = dataSourceProvider.getDatasource().getConnection()) {
            metadataJdbcRepository.delete(fileId, connection);
            contentJdbcRepository.delete(fileId, connection);
        } catch (final SQLException e) {
            throw new StorageException("Failed to delete file from the database", e);
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
