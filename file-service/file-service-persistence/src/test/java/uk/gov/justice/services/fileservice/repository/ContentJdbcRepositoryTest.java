package uk.gov.justice.services.fileservice.repository;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.StorageException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContentJdbcRepositoryTest {

    @InjectMocks
    private ContentJdbcRepository contentJdbcRepository;

    @Test
    public void shouldInsertFileContentIntoTheDatabase() throws Exception {

        final UUID fileId = randomUUID();
        final String sql = "INSERT INTO content(file_id, content, deleted) VALUES(?, ?, ?)";

        final int rowsAffected = 1;

        final InputStream contentStream = mock(InputStream.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        contentJdbcRepository.insert(fileId, contentStream, connection);

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).setBinaryStream(2, contentStream);
        inOrder.verify(preparedStatement).setBoolean(3, false);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowADataIntegrityExceptionIfTheInsertAffectsMoreThanOneRow() throws Exception {

        final UUID fileId = randomUUID();
        final int rowsAffected = 2;
        final String sql = "INSERT INTO content(file_id, content, deleted) VALUES(?, ?, ?)";

        final InputStream contentStream = mock(InputStream.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        final DataIntegrityException expected = assertThrows(
                DataIntegrityException.class,
                () -> contentJdbcRepository.insert(fileId, contentStream, connection));

        assertThat(expected.getMessage(), is("Insert into content table affected 2 rows!"));

        verify(preparedStatement).close();
        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowAStorageExceptionIfAnyOfTheInsertSqlStatementsFail() throws Exception {

        final UUID fileId = randomUUID();
        final SQLException sqlException = new SQLException("Ooops");
        final String sql = "INSERT INTO content(file_id, content, deleted) VALUES(?, ?, ?)";

        final InputStream contentStream = mock(InputStream.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(sqlException);

        try {
            contentJdbcRepository.insert(fileId, contentStream, connection);
            fail();
        } catch (final StorageException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to insert file into database"));
        }

        verify(preparedStatement).close();
        verify(connection, never()).close();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldFindFileContentByFileId() throws Exception {

        final UUID fileId = randomUUID();
        final String sql = "SELECT content FROM content WHERE file_id = ?";

        final InputStream contentStream = mock(InputStream.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBinaryStream(1)).thenReturn(contentStream);

        final FileContent fileContent = contentJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find content"));

        assertThat(fileContent.getContent(), is(contentStream));

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement,
                resultSet,
                preparedStatement,
                resultSet);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getBinaryStream(1);
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldReturnEmptyIfNoFileFound() throws Exception {

        final UUID fileId = randomUUID();
        final String sql = "SELECT content FROM content WHERE file_id = ?";

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThat(contentJdbcRepository.findByFileId(fileId, connection), is(empty()));

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement,
                resultSet,
                preparedStatement,
                resultSet);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowStorageExceptionIfAnyOfTheSqlStatementsFail() throws Exception {

        final SQLException sqlException = new SQLException("Ooops");
        final String sql = "SELECT content FROM content WHERE file_id = ?";

        final UUID fileId = randomUUID();

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBinaryStream(1)).thenThrow(sqlException);

        final StorageException expected = assertThrows(StorageException.class, () -> contentJdbcRepository.findByFileId(fileId, connection));
        assertThat(expected.getCause(), is(sqlException));
        assertThat(expected.getMessage(), is("Failed to read metadata using file id " + fileId));

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement,
                resultSet,
                preparedStatement,
                resultSet);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldDeleteAFileById() throws Exception {

        final UUID fileId = randomUUID();
        final String sql = "DELETE FROM content WHERE file_id = ?";
        final int rowsAffected = 1;

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        contentJdbcRepository.delete(fileId, connection);

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowDataIntegrityExceptionIfDeletingAFileAffectsMoreThanOneRow() throws Exception {

        final UUID fileId = randomUUID();
        final String sql = "DELETE FROM content WHERE file_id = ?";
        final int rowsAffected = 2;

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        try {
            contentJdbcRepository.delete(fileId, connection);
            fail();
        } catch (final DataIntegrityException expected) {
            assertThat(expected.getMessage(), is("Delete from content table affected 2 rows!"));
        }

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowStorageExceptionIfDeletingAFileThrowsAnSqlException() throws Exception {

        final UUID fileId = randomUUID();
        final String sql = "DELETE FROM content WHERE file_id = ?";
        final SQLException sqlException = new SQLException("Ooops");

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(sqlException);

        try {
            contentJdbcRepository.delete(fileId, connection);
            fail();
        } catch (final StorageException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to delete file from database with id " + fileId));
        }

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(sql);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }
}
