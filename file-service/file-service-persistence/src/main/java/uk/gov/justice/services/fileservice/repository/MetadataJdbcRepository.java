package uk.gov.justice.services.fileservice.repository;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.json.Json.createReader;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Class for handling inserts/updates/selects on the 'metadata' database table. This class is not
 * transactional. Each method takes a valid database connection and it is assumed that the transaction
 * would have already been started on that connection.
 *
 * NB. This class does not have a unit test, but is tested instead by FilePersistenceIntegrationTest
 */
public class MetadataJdbcRepository {

    public static final String FIND_BY_FILE_ID_SQL = "SELECT metadata FROM metadata WHERE file_id = ?";
    public static final String DELETE_SQL = "DELETE FROM metadata WHERE file_id = ?";

    @Inject
    protected MetadataSqlProvider metadataSqlProvider;

    /**
     * inserts the json metadata of a file into a new row
     *
     * @param fileId the id of the file to insert
     * @param metadata the json metadata of the file
     * @param connection a live database connection. Assumes any transtactions will have already been
     *                   started on this connection.
     */
    public void insert(final UUID fileId, final JsonObject metadata, final Connection connection) throws FileServiceException {

        insertOrUpdate(fileId, metadata, connection, metadataSqlProvider.getInsertSql());
    }

    /**
     * Finds the json metadata of a file, or {@code empty()} if none exists.
     *
     * @param fileId the file id of the metadata
     * @param connection a live database connection. Assumes any transtactions will have already been
     *                   started on this connection.
     * @return an {@link Optional} containing the file's metadata as a {@link JsonObject} or
     * {@code empty} if not found.
     */
    public Optional<JsonObject> findByFileId(final UUID fileId, final Connection connection) throws StorageException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_FILE_ID_SQL)) {
            preparedStatement.setObject(1, fileId);

            try(final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return of(toJsonObject(resultSet.getString(1)));
                }
                return empty();
            }
        } catch (final SQLException e) {
            throw new StorageException("Failed to find metadata. Sql: " + FIND_BY_FILE_ID_SQL, e);
        }
    }

    /**
     * Updates the json metadata of a file
     *
     * @param fileId the id of the file to update
     * @param metadata the json metadata of the file
     * @param connection a live database connection. Assumes any transtactions will have already been
     *                   started on this connection.
     */
    public void update(final UUID fileId, final JsonObject metadata, final Connection connection) throws FileServiceException {
        insertOrUpdate(fileId, metadata, connection, metadataSqlProvider.getUpdateSql());
    }

    private void insertOrUpdate(final UUID fileId, final JsonObject metadata, final Connection connection, final String sql) throws DataIntegrityException, StorageException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, metadata.toString());
            preparedStatement.setObject(2, fileId);

            final int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated != 1) {
                throw new DataIntegrityException("Updating metadata table affected " + rowsUpdated + " rows!");
            }
        } catch (final SQLException e) {
            throw new StorageException("Failed to update metadata table. Sql: " + sql, e);
        }
    }

    public void delete(final UUID fileId, final Connection connection) throws FileServiceException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, fileId);

            final int rowsUpdated = preparedStatement.executeUpdate();
            if(rowsUpdated != 1) {
                throw new DataIntegrityException("Delete from metadata table affected " + rowsUpdated + " rows!");
            }
        } catch (final SQLException e) {
            throw new StorageException("Failed to update metadata table. Sql: " + DELETE_SQL, e);
        }
    }

    private JsonObject toJsonObject(final String json) {
        try (final JsonReader reader = createReader(new StringReader(json));) {
            return reader.readObject();
        }
    }
}
