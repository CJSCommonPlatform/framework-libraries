package uk.gov.justice.services.fileservice.repository;

import static java.time.ZoneOffset.UTC;
import static java.util.Optional.empty;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.common.converter.ZonedDateTimes.toSqlTimestamp;
import static uk.gov.justice.services.fileservice.repository.FileStoreJdbcRepository.DELETE_FILES_OLDER_THAN_WITH_LIMIT;
import static uk.gov.justice.services.fileservice.repository.FileStoreJdbcRepository.INSERT_SQL;
import static uk.gov.justice.services.fileservice.repository.FileStoreJdbcRepository.MARK_AS_DELETED_SQL;
import static uk.gov.justice.services.fileservice.repository.FileStoreJdbcRepository.SELECT_SQL;
import static uk.gov.justice.services.fileservice.repository.FileStoreJdbcRepository.UPDATE_METADATA_SQL;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.io.InputStreamWrapper;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileStoreJdbcRepositoryTest {

    @InjectMocks
    private FileStoreJdbcRepository fileStoreJdbcRepository;

    @Test
    public void shouldInsertIntoTheFileStore() throws Exception {
        final UUID fileId = randomUUID();
        final int rowsAffected = 1;
        final String metadataJson = """
                {"some": "json"}
                """;

        final InputStream content = mock(InputStream.class);
        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(INSERT_SQL)).thenReturn(preparedStatement);
        when(metadata.toString()).thenReturn(metadataJson);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        fileStoreJdbcRepository.insert(fileId, content, metadata, connection);

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).setBinaryStream(2, content);
        inOrder.verify(preparedStatement).setString(3, metadataJson);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowDataIntegrityExceptionIfInsertAffectsMoreThanOneRow() throws Exception {

        final UUID fileId = randomUUID();
        final int rowsAffected = 2;
        final String metadataJson = """
                {"some": "json"}
                """;

        final InputStream content = mock(InputStream.class);
        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(INSERT_SQL)).thenReturn(preparedStatement);
        when(metadata.toString()).thenReturn(metadataJson);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        final DataIntegrityException dataIntegrityException = assertThrows(
                DataIntegrityException.class,
                () -> fileStoreJdbcRepository.insert(fileId, content, metadata, connection));

        assertThat(dataIntegrityException.getMessage(), is("Insert into content table affected 2 rows!"));

        verify(preparedStatement).close();
        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowStorageExceptionIfInsertIntoTheFileStoreFails() throws Exception {

        final UUID fileId = randomUUID();
        final SQLException sqlException = new SQLException("Ooops");

        final String metadataJson = """
                {"some": "json"}
                """;

        final InputStream content = mock(InputStream.class);
        final JsonObject metadata = mock(JsonObject.class);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(INSERT_SQL)).thenReturn(preparedStatement);
        when(metadata.toString()).thenReturn(metadataJson);
        when(preparedStatement.executeUpdate()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStoreJdbcRepository.insert(fileId, content, metadata, connection));

        assertThat(storageException.getMessage(), is("Failed to insert file into database"));
        assertThat(storageException.getCause(), is(sqlException));

        verify(preparedStatement).close();
        verify(connection, never()).close();
    }

    @Test
    public void shouldFindByFileIdAndKeepSqlConnectionOpen() throws Exception {
        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);
        final InputStream contentStream = mock(InputStream.class);
        final String metadataJson = """
                {"some":"json"}
                """;

        when(connection.prepareStatement(SELECT_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getTimestamp(1)).thenReturn(null);
        when(resultSet.getString(2)).thenReturn(metadataJson);
        when(resultSet.getBinaryStream(3)).thenReturn(contentStream);

        final Optional<FileReference> fileReferenceOptional = fileStoreJdbcRepository.findByFileId(fileId, connection);

        if (fileReferenceOptional.isPresent()) {
            final FileReference fileReference = fileReferenceOptional.get();
            assertThat(fileReference.getFileId(), is(fileId));
            assertThat(fileReference.getMetadata().toString(), is(metadataJson.trim()));
            assertThat(fileReference.isDeleted(), is(false));
            final InputStream fileReferenceStream = fileReference.getContentStream();
            assertThat(fileReferenceStream, is(instanceOf(InputStreamWrapper.class)));

            final InputStreamWrapper inputStreamWrapper = (InputStreamWrapper) fileReferenceStream;

            assertThat(inputStreamWrapper.getInputStream(), is(contentStream));
            assertThat(inputStreamWrapper.getConnection(), is(connection));

        } else {
            fail();
        }

        final InOrder inOrder = inOrder(preparedStatement, resultSet);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldReturnEmptyIfNoFileFound() throws Exception {

        final UUID fileId = randomUUID();
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(SELECT_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        final Optional<FileReference> fileReference = fileStoreJdbcRepository.findByFileId(fileId, connection);

        assertThat(fileReference, is(empty()));

        verify(preparedStatement).setObject(1, fileId);
        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection, never()).close();
    }

    @Test
    public void shouldReturnEmptyIfFileHasDeletedAtDate() throws Exception {

        final UUID fileId = randomUUID();
        final ZonedDateTime deletedAt = new UtcClock().now().minusDays(2);
        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);
        final ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(SELECT_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getTimestamp(1)).thenReturn(toSqlTimestamp(deletedAt));

        final Optional<FileReference> fileReferenceOptional = fileStoreJdbcRepository.findByFileId(fileId, connection);

        assertThat(fileReferenceOptional, is(empty()));

        final InOrder inOrder = inOrder(preparedStatement, resultSet);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(resultSet).close();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowExceptionIfFindingFileFails() throws Exception {

        final UUID fileId = fromString("02599d1a-6302-42eb-9fbd-7e5f1811416c");
        final SQLException sqlException = new SQLException("Ooops");

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(SELECT_SQL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(StorageException.class, () -> fileStoreJdbcRepository.findByFileId(fileId, connection));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to read content of file with file id 02599d1a-6302-42eb-9fbd-7e5f1811416c"));

        final InOrder inOrder = inOrder(preparedStatement, connection);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldSoftDeleteFilesByMarkingWithDeletedAtDate() throws Exception {

        final UUID fileId = randomUUID();
        final ZonedDateTime deletedAt = new UtcClock().now().minusDays(2);

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(MARK_AS_DELETED_SQL)).thenReturn(preparedStatement);

        fileStoreJdbcRepository.markAsDeleted(fileId, deletedAt, connection);

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setTimestamp(1, toSqlTimestamp(deletedAt));
        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowExceptionIfMarkingFileAsDeletedFails() throws Exception {
        final UUID fileId = fromString("b07a24d1-acf7-4ba6-8b57-9c5a7d766ae2");
        final ZonedDateTime deletedAt = ZonedDateTime.of(2024, 1, 3, 11, 45, 23, 0, UTC);
        final SQLException sqlException = new SQLException("Ooops");

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(MARK_AS_DELETED_SQL)).thenReturn(preparedStatement);
        doThrow(sqlException).when(preparedStatement).setObject(2, fileId);

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStoreJdbcRepository.markAsDeleted(fileId, deletedAt, connection));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to soft delete content of file with fileId 'b07a24d1-acf7-4ba6-8b57-9c5a7d766ae2' and deletedAt '2024-01-03T11:45:23Z'"));

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setTimestamp(1, toSqlTimestamp(deletedAt));
        inOrder.verify(preparedStatement).setObject(2, fileId);
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldUpdateMetadata() throws Exception {

        final UUID fileId = randomUUID();
        final JsonObject metadata = createObjectBuilder()
                .add("property1", "value1")
                .add("property2", "value2")
                .build();

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(UPDATE_METADATA_SQL)).thenReturn(preparedStatement);

        fileStoreJdbcRepository.updateMetadata(fileId, metadata, connection);

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setObject(1, metadata);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowExceptionIfUpdatingMetadataFails() throws Exception {

        final UUID fileId = fromString("f8a04d21-5a3c-4cc4-8e1b-2e0313381699");
        final JsonObject metadata = createObjectBuilder()
                .add("property1", "value1")
                .add("property2", "value2")
                .build();
        final SQLException sqlException = new SQLException("Ooops");

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(UPDATE_METADATA_SQL)).thenReturn(preparedStatement);
        doThrow(sqlException).when(preparedStatement).executeUpdate();

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStoreJdbcRepository.updateMetadata(fileId, metadata, connection));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to update metadata of file with fileId 'f8a04d21-5a3c-4cc4-8e1b-2e0313381699'"));

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setObject(1, metadata);
        inOrder.verify(preparedStatement).setObject(1, fileId);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

        verify(connection, never()).close();
    }

    @Test
    public void shouldPurgeFilesOlderThanDate() throws Exception {

        final ZonedDateTime purgeDateTime = new UtcClock().now();
        final int maxNumberToDelete = 23;
        final int rowsAffected = 11;

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(DELETE_FILES_OLDER_THAN_WITH_LIMIT)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(rowsAffected);

        assertThat(fileStoreJdbcRepository.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete, connection), is(rowsAffected));

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setTimestamp(1, toSqlTimestamp(purgeDateTime));
        inOrder.verify(preparedStatement).setInt(2, maxNumberToDelete);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();
    }

    @Test
    public void shouldThrowExceptionIfPurgingFilesFails() throws Exception {

        final ZonedDateTime purgeDateTime = ZonedDateTime.of(2024, 1, 3, 11, 45, 23, 0, UTC);
        final SQLException sqlException = new SQLException("Ooops");
        final int maxNumberToDelete = 23;

        final Connection connection = mock(Connection.class);
        final PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(DELETE_FILES_OLDER_THAN_WITH_LIMIT)).thenReturn(preparedStatement);
        doThrow(sqlException).when(preparedStatement).executeUpdate();

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStoreJdbcRepository.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete, connection));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to delete files in file store older than '2024-01-03T11:45:23Z'"));

        final InOrder inOrder = inOrder(preparedStatement);

        inOrder.verify(preparedStatement).setTimestamp(1, toSqlTimestamp(purgeDateTime));
        inOrder.verify(preparedStatement).setInt(2, maxNumberToDelete);
        inOrder.verify(preparedStatement).executeUpdate();
        inOrder.verify(preparedStatement).close();

    }
}