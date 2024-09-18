package uk.gov.justice.services.fileservice.repository;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.json.Json.createReader;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.io.InputStreamWrapper;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;
import javax.json.JsonReader;

public class FileStoreJdbcRepository {

    public static final String INSERT_SQL = "INSERT INTO content (file_id, content, metadata) VALUES (?, ?, (to_json(?::json)))";
    public static final String SELECT_SQL = "SELECT deleted_at, metadata, content FROM content WHERE file_id = ?";
    public static final String MARK_AS_DELETED_SQL = "UPDATE content SET deleted_at = ? WHERE file_id = ?";
    public static final String UPDATE_METADATA_SQL = "UPDATE content SET metadata = ? WHERE file_id = ?";

    /**
     * Gnarly query that basically means 'delete a maximum of [n] files
     * marked as deleted before [some-date] making sure the oldest are
     * deleted first'
     */
    public static final String DELETE_FILES_OLDER_THAN_WITH_LIMIT = """
        DELETE FROM content 
        WHERE file_id = any (array(
            SELECT file_id FROM content WHERE deleted_at < ? ORDER BY deleted_at LIMIT ?
        ))""";

    public void insert(
            final UUID fileId,
            final InputStream content,
            final JsonObject metadata,
            final Connection connection) throws FileServiceException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setObject(1, fileId);
            preparedStatement.setBinaryStream(2, content);
            preparedStatement.setString(3, metadata.toString());
            final int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataIntegrityException("Insert into content table affected " + rowsAffected + " rows!");
            }

        } catch (final SQLException e) {
            throw new StorageException("Failed to insert file into database", e);
        }
    }

    public Optional<FileReference> findByFileId(final UUID fileId, final Connection connection) throws FileServiceException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL)) {

            preparedStatement.setObject(1, fileId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final Timestamp deletedAt = resultSet.getTimestamp(1);

                    if (deletedAt == null) {
                        final String metadataJson = resultSet.getString(2);
                        final InputStream contentStream = resultSet.getBinaryStream(3);
                        final JsonObject metadata = toJsonObject(metadataJson);

                        final InputStreamWrapper inputStreamWrapper = new InputStreamWrapper(
                                contentStream,
                                connection);

                        final FileReference fileReference = new FileReference(
                                fileId,
                                metadata,
                                inputStreamWrapper);

                        return of(fileReference);
                    }
                }
            }
            
        } catch (final SQLException e) {
            throw new StorageException(format("Failed to read content of file with file id %s", fileId), e);
        }

        return empty();
    }

    public void markAsDeleted(final UUID fileId, final ZonedDateTime deletedAt, final Connection connection) throws FileServiceException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(MARK_AS_DELETED_SQL)) {

            preparedStatement.setTimestamp(1, toSqlTimestamp(deletedAt));
            preparedStatement.setObject(2, fileId);

            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new StorageException(format("Failed to soft delete content of file with fileId '%s' and deletedAt '%s'", fileId, deletedAt), e);
        }
    }

    public void updateMetadata(final UUID fileId, final JsonObject metadata, final Connection connection) throws FileServiceException {

        try(final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_METADATA_SQL)) {
            preparedStatement.setObject(1, metadata);
            preparedStatement.setObject(1, fileId);

            preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new StorageException(format("Failed to update metadata of file with fileId '%s'", fileId), e);
        }
    }

    public int purgeFilesOlderThan(final ZonedDateTime purgeDateTime, final int maxNumberToDelete, final Connection connection) throws FileServiceException {

        try(final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FILES_OLDER_THAN_WITH_LIMIT)) {

            preparedStatement.setTimestamp(1, toSqlTimestamp(purgeDateTime));
            preparedStatement.setInt(2, maxNumberToDelete);

            return preparedStatement.executeUpdate();

        } catch (final SQLException e) {
            throw new StorageException(format("Failed to delete files in file store older than '%s'", purgeDateTime), e);
        }
    }

    private JsonObject toJsonObject(final String json) {
        try (final JsonReader reader = createReader(new StringReader(json));) {
            return reader.readObject();
        }
    }
}
