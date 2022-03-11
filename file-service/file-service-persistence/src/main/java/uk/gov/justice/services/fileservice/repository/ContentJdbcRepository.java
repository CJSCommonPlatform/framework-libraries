package uk.gov.justice.services.fileservice.repository;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * Class for handling inserts/updates/selects on the 'content' database table. This class is not
 * transactional. Each method takes a valid database connection and it is assumed that the
 * transaction would have already been started on that connection.
 *
 * There no update method on this class. This is because we should never update content. Delete and
 * recreate if you need to change the file content.
 *
 * NB. This class does not have a unit test, but is tested instead by
 * FilePersistenceIntegrationTest
 */
public class ContentJdbcRepository {

    public static final String SQL_FIND_BY_FILE_ID = "SELECT content FROM content WHERE file_id = ?";
    public static final String SQL_INSERT_CONTENT = "INSERT INTO content(file_id, content, deleted) VALUES(?, ?, ?)";
    public static final String SQL_DELETE_CONTENT = "DELETE FROM content WHERE file_id = ?";

    /**
     * Inserts the content into the content table as an array of bytes[]
     *
     * @param fileId     the file id of the content
     * @param content    an InputStream to the file content
     * @param connection the database connection. It is assumed that a transaction has previously
     *                   been started on this connection.
     */
    public void insert(
            final UUID fileId,
            final InputStream content,
            final Connection connection) throws FileServiceException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CONTENT)) {

            preparedStatement.setObject(1, fileId);
            preparedStatement.setBinaryStream(2, content);
            preparedStatement.setBoolean(3, false);
            final int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataIntegrityException("Insert into content table affected " + rowsAffected + " rows!");
            }

        } catch (final SQLException e) {
            throw new StorageException("Failed to insert file into database", e);
        }
    }

    /**
     * Finds the file content for the specified file id, returned as a java {@link Optional}. If no
     * content found for that id then {@code empty()} is returned instead.
     *
     * @param fileId     the file id of the content
     * @param connection a live database connection
     * @return the file content as an array of bytes wrapped in a java {@link Optional}
     * @throws FileServiceException if the read failed and so the current transaction should be
     *                              rolled back.
     */
    public Optional<FileContent> findByFileId(
            final UUID fileId,
            final Connection connection) throws FileServiceException {

        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_FILE_ID)) {
            preparedStatement.setObject(1, fileId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final InputStream contentStream = resultSet.getBinaryStream(1);
                    return of(new FileContent(contentStream));
                }
            }
        } catch (final SQLException e) {
            throw new StorageException(format("Failed to read metadata using file id %s", fileId), e);
        }

        return empty();
    }

    public void delete(final UUID fileId, final Connection connection) throws FileServiceException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_CONTENT)) {

            preparedStatement.setObject(1, fileId);
            final int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataIntegrityException("Delete from content table affected " + rowsAffected + " rows!");
            }

        } catch (final SQLException e) {
            throw new StorageException("Failed to delete file from database with id " + fileId, e);
        }
    }
}
