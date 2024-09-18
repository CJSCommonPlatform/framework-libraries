package uk.gov.justice.services.fileservice.repository;

import static java.util.Optional.empty;
import static java.util.Optional.of;
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

import javax.json.JsonObject;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileStoreTest {

    @Mock
    private FileStoreJdbcRepository fileStoreJdbcRepository;

    @Mock
    private DataSourceProvider dataSourceProvider;

    @Mock
    private MetadataUpdater metadataUpdater;

    @Mock
    private UtcClock clock;

    @InjectMocks
    private FileStore fileStore;

    @Test
    public void shouldStoreFileInTheDatabase() throws Exception {

        final JsonObject metadata = mock(JsonObject.class);
        final JsonObject updatedMetadata = mock(JsonObject.class);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);
        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream)).thenReturn(updatedMetadata);

        final UUID fileId = fileStore.store(metadata, contentStream);

        final InOrder inOrder = inOrder(fileStoreJdbcRepository, connection);

        inOrder.verify(fileStoreJdbcRepository).insert(fileId, contentStream, updatedMetadata, connection);
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldThrowExceptionIfGettingConnectionFailsWhenStoringFileInTheDatabase() throws Exception {

        final JsonObject metadata = mock(JsonObject.class);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);
        final DataSource dataSource = mock(DataSource.class);

        final SQLException sqlException = new SQLException("Ooops");

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        final FileServiceException fileServiceException = assertThrows(FileServiceException.class, () -> fileStore.store(metadata, contentStream));

        assertThat(fileServiceException.getCause(), is(sqlException));
        assertThat(fileServiceException.getMessage(), is("Failed to get database connection"));
    }

    @Test
    public void shouldFindFileInTheDatabase() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final Optional<FileReference> fileReference = of(mock(FileReference.class));

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(fileStoreJdbcRepository.findByFileId(fileId, connection)).thenReturn(fileReference);

        assertThat(fileStore.find(fileId), is(fileReference));

        verify(fileStoreJdbcRepository).findByFileId(fileId, connection);

        verify(connection, never()).close();
    }

    @Test
    public void shouldThrowExceptionIfGettingConnectionFailsWhenFindingFilesInTheDatabase() throws Exception {

        final UUID fileId = randomUUID();
        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(StorageException.class, () -> fileStore.find(fileId));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to get database connection"));
    }

    @Test
    public void shouldUpdateMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        fileStore.updateMetadata(fileId, metadata);

        verify(fileStoreJdbcRepository).updateMetadata(fileId, metadata, connection);

        verify(connection).close();
    }

    @Test
    public void shouldThrowExceptionIfGettingConnectionFailsWhenUpdatingMetadata() throws Exception {

        final UUID fileId = randomUUID();
        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStore.updateMetadata(fileId, metadata));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to get database connection"));
    }

    @Test
    public void shouldMarkFileAsDeleted() throws Exception {

        final UUID fileId = randomUUID();
        final ZonedDateTime deletedAt = new UtcClock().now();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(clock.now()).thenReturn(deletedAt);

        fileStore.markAsDeleted(fileId);

        verify(fileStoreJdbcRepository).markAsDeleted(fileId, deletedAt, connection);

        verify(connection).close();
    }

    @Test
    public void shouldThrowExceptionIfGettingConnectionFailsWhenMarkingFileAsDeleted() throws Exception {

        final UUID fileId = randomUUID();
        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStore.markAsDeleted(fileId));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to get database connection"));
    }

    @Test
    public void shouldRetrieveMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final FileReference fileReference = mock(FileReference.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(fileStoreJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(fileReference));
        when(fileReference.getMetadata()).thenReturn(metadata);

        final Optional<JsonObject> metadataOptional = fileStore.retrieveMetadata(fileId);

        if (metadataOptional.isPresent()) {
            assertThat(metadataOptional.get(), is(metadata));
        } else {
            fail();
        }

        verify(connection).close();
    }

    @Test
    public void shouldReturnEmptyIfNoFileExistsWhenRetrievingMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(fileStoreJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());

        assertThat(fileStore.retrieveMetadata(fileId), is(empty()));

        verify(connection).close();
    }

    @Test
    public void shouldThrowExceptionIfGettingConnectionFailsWhenRetrievingMetadata() throws Exception {

        final UUID fileId = randomUUID();
        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStore.retrieveMetadata(fileId));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to get database connection"));
    }

    @Test
    public void shouldPurgeFilesOlderThanSpecifiedDate() throws Exception {

        final ZonedDateTime purgeDateTime = new UtcClock().now().minusDays(28);
        final int maxNumberToDelete = 23;
        final int rowsAffected = 11;

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(fileStoreJdbcRepository.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete, connection)).thenReturn(rowsAffected);

        assertThat(fileStore.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete), is(rowsAffected));
    }

    @Test
    public void shouldThrowExceptionIfGettingConnectionFailsWhenPurgingFilesOlderThanSpecifiedDate() throws Exception {

        final ZonedDateTime purgeDateTime = new UtcClock().now().minusDays(28);
        final int maxNumberToDelete = 23;
        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        final StorageException storageException = assertThrows(
                StorageException.class,
                () -> fileStore.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete));

        assertThat(storageException.getCause(), is(sqlException));
        assertThat(storageException.getMessage(), is("Failed to get database connection"));
    }
}
