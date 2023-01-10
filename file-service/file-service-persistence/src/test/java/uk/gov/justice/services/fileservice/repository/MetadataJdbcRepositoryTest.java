package uk.gov.justice.services.fileservice.repository;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.fileservice.repository.MetadataJdbcRepository.DELETE_SQL;
import static uk.gov.justice.services.fileservice.repository.MetadataJdbcRepository.FIND_BY_FILE_ID_SQL;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MetadataJdbcRepositoryTest {

    @Mock
    private MetadataSqlProvider metadataSqlProvider;

    @InjectMocks
    private MetadataJdbcRepository metadataJdbcRepository;

    @Test
    public void shouldInsertMetadata() throws Exception {

        final UUID fileId = randomUUID();
        final String insertSql = "INSERT INTO metadata(metadata, file_id) values (to_json(?::json), ?)";

        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        final int rowsUpdated = 1;

        when(connection.prepareStatement(insertSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsUpdated);

        metadataJdbcRepository.insert(fileId, metadata, connection);

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement
        );

        inOrder.verify(connection).prepareStatement(insertSql);
        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowAStorageExceptionIfInsertingMetadataFails() throws Exception {

        final SQLException sqlException = new SQLException("Ooops");

        final UUID fileId = randomUUID();
        final String insertSql = "INSERT INTO metadata(metadata, file_id) values (to_json(?::json), ?)";
        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(insertSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(sqlException);

        try {
            metadataJdbcRepository.insert(fileId, metadata, connection);
            fail();
        } catch (FileServiceException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to update metadata table. Sql: " + insertSql));
        }

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement
        );

        inOrder.verify(connection).prepareStatement(insertSql);

        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowADataIntegrityExceptionIfInsertingMetadataReturnsMoreThanOneRowAffected() throws Exception {

        final UUID fileId = randomUUID();
        final String insertSql = "INSERT INTO metadata(metadata, file_id) values (to_json(?::json), ?)";
        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final int rowsUpdated = 2;

        when(connection.prepareStatement(insertSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsUpdated);

        try {
            metadataJdbcRepository.insert(fileId, metadata, connection);
        } catch (final DataIntegrityException expected) {
            assertThat(expected.getMessage(), is("Updating metadata table affected 2 rows!"));
        }

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement
        );

        inOrder.verify(connection).prepareStatement(insertSql);

        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldUpdateMetadata() throws Exception {
        final UUID fileId = randomUUID();
        final String updateSql = "UPDATE metadata SET metadata = to_json(?::json) WHERE file_id = ?";

        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final int rowsUpdated = 1;

        when(connection.prepareStatement(updateSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsUpdated);

        metadataJdbcRepository.update(fileId, metadata, connection);

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement
        );

        inOrder.verify(connection).prepareStatement(updateSql);

        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowAStorageExceptionIfUpdatingMetadataFails() throws Exception {

        final SQLException sqlException = new SQLException("Ooops");

        final UUID fileId = randomUUID();
        final String updateSql = "UPDATE metadata SET metadata = to_json(?::json) WHERE file_id = ?";

        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(updateSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(sqlException);

        try {
            metadataJdbcRepository.update(fileId, metadata, connection);
            fail();
        } catch (final StorageException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to update metadata table. Sql: " + updateSql));
        }

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement
        );

        inOrder.verify(connection).prepareStatement(updateSql);

        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowADataIntegrityExceptionIfUpdatingMetadataReturnsMoreThanOneRowAffected() throws Exception {

        final UUID fileId = randomUUID();
        final String updateSql = "UPDATE metadata SET metadata = to_json(?::json) WHERE file_id = ?";

        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final int rowsUpdated = 2;

        when(connection.prepareStatement(updateSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsUpdated);

        try {
            metadataJdbcRepository.update(fileId, metadata, connection);
        } catch (final DataIntegrityException expected) {
            assertThat(expected.getMessage(), is("Updating metadata table affected 2 rows!"));
        }

        final InOrder inOrder = inOrder(
                connection,
                preparedStatement
        );

        inOrder.verify(connection).prepareStatement(updateSql);

        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldFindMetadataByFileId() throws Exception {

        final String json = "{\"some\":\"json\"}";

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(FIND_BY_FILE_ID_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn(json);

        final JsonObject metadata = metadataJdbcRepository
                .findByFileId(fileId, connection)
                .orElseThrow(() -> new AssertionError("Failed to find metadata"));

        assertThat(metadata.toString(), is(json));

        final InOrder inOrder = inOrder(connection, preparedStatement, resultSet);

        inOrder.verify(connection).prepareStatement(FIND_BY_FILE_ID_SQL);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getString(1);
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldReturnEmptyIfNoMetadataFoundForTheSpecifiedId() throws Exception {

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(FIND_BY_FILE_ID_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThat(metadataJdbcRepository.findByFileId(fileId, connection).isPresent(), is(false));

        final InOrder inOrder = inOrder(connection, preparedStatement, resultSet);

        inOrder.verify(connection).prepareStatement(FIND_BY_FILE_ID_SQL);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(resultSet, never()).getString(1);
        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowAStorageExceptionIfFindingAFileFails() throws Exception {

        final SQLException sqlException = new SQLException("Ooops");

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(FIND_BY_FILE_ID_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenThrow(sqlException);

        try {
            metadataJdbcRepository.findByFileId(fileId, connection);
            fail();
        } catch (StorageException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to find metadata. Sql: " + FIND_BY_FILE_ID_SQL));
        }

        final InOrder inOrder = inOrder(connection, preparedStatement, resultSet);

        inOrder.verify(connection).prepareStatement(FIND_BY_FILE_ID_SQL);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeQuery();
        inOrder.verify(resultSet).next();
        inOrder.verify(resultSet).getString(1);
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldDeleteMetadata() throws Exception {

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        final int rowsDeleted = 1;

        when(connection.prepareStatement(DELETE_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsDeleted);

        metadataJdbcRepository.delete(fileId, connection);

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(DELETE_SQL);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowAnSQLExceptionIfDeletingMetadataFails() throws Exception {

        final SQLException sqlException = new SQLException("Oopos");

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(DELETE_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(sqlException);

        try {
            metadataJdbcRepository.delete(fileId, connection);
        } catch (StorageException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to update metadata table. Sql: " + DELETE_SQL));
        }

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(DELETE_SQL);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowADataIntegrityExceptionIfDeletingMetadataDeletesMoreThanOneRow() throws Exception {

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        final int rowsDeleted = 2;

        when(connection.prepareStatement(DELETE_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsDeleted);

        try {
            metadataJdbcRepository.delete(fileId, connection);
            fail();
        } catch (DataIntegrityException expected) {
            assertThat(expected.getMessage(), is("Delete from metadata table affected 2 rows!"));
        }

        final InOrder inOrder = inOrder(connection, preparedStatement);

        inOrder.verify(connection).prepareStatement(DELETE_SQL);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }
}
