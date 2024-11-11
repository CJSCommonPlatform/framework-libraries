package uk.gov.justice.services.fileservice.repository;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.io.InputStreamWrapper;

import java.io.BufferedInputStream;
import java.io.InputStream;
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
    private ContentJdbcRepository contentJdbcRepository;

    @Mock
    private MetadataJdbcRepository metadataJdbcRepository;

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

        final InOrder inOrder = inOrder(metadataJdbcRepository, contentJdbcRepository, connection);

        inOrder.verify(contentJdbcRepository).insert(fileId, contentStream, connection);
        inOrder.verify(metadataJdbcRepository).insert(fileId, updatedMetadata, connection);
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
        assertThat(fileServiceException.getMessage(), is("Failed to insert file into file-store"));
    }

    @Test
    public void shouldFindFileInTheDatabase() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final InputStream inputStream = mock(InputStream.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        final Optional<JsonObject> metadata = of(mock(JsonObject.class));
        final FileContent fileContent = mock(FileContent.class);
        final Optional<FileContent> fileContentOptional = of(fileContent);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(metadata);
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(fileContentOptional);
        when(fileContent.getContent()).thenReturn(inputStream);

        final Optional<FileReference> fileReference = fileStore.find(fileId);

        assertThat(fileReference.isPresent(), is(true));
        assertThat(fileReference.get().getFileId(), is(fileId));

        final InputStream contentStream = fileReference.get().getContentStream();
        assertThat(contentStream, is(instanceOf(InputStreamWrapper.class)));
        final InputStreamWrapper inputStreamWrapper = (InputStreamWrapper) contentStream;

        assertThat(getValueOfField(inputStreamWrapper, "inputStream", InputStream.class), is(inputStream));
        assertThat(getValueOfField(inputStreamWrapper, "connection", Connection.class), is(connection));

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
        assertThat(storageException.getMessage(), is("Failed to read file from the file-store database"));
    }

    @Test
    public void shouldThrowDataIntegrityExceptionIfMetadataFoundButNoContentExists() throws Exception {

        final UUID fileId = fromString("ba2439e3-7fe2-4f7b-83a7-4a3ddef83cc1");

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final Optional<JsonObject> metadata = of(mock(JsonObject.class));
        final Optional<FileContent> content = empty();

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(metadata);
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(content);

        final DataIntegrityException dataIntegrityException = assertThrows(DataIntegrityException.class, () -> fileStore.find(fileId));

        assertThat(dataIntegrityException.getMessage(), is("No file content found for file id 'ba2439e3-7fe2-4f7b-83a7-4a3ddef83cc1' but metadata exists for that id"));
    }

    @Test
    public void shouldThrowDataIntegrityExceptionIfContentFoundButNoMetadataExists() throws Exception {

        final UUID fileId = fromString("ba2439e3-7fe2-4f7b-83a7-4a3ddef83cc1");

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final Optional<JsonObject> metadata = empty();
        final Optional<FileContent> content = of(mock(FileContent.class));


        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(metadata);
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(content);

        final DataIntegrityException dataIntegrityException = assertThrows(DataIntegrityException.class, () -> fileStore.find(fileId));

        assertThat(dataIntegrityException.getMessage(), is("No metadata found for file id 'ba2439e3-7fe2-4f7b-83a7-4a3ddef83cc1' but file content exists for that id"));
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

        verify(metadataJdbcRepository).update(fileId, metadata, connection);

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
        assertThat(storageException.getMessage(), is("Failed to get database connection to file-store"));
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

        verify(contentJdbcRepository).markAsDeleted(fileId, deletedAt, connection);

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
        assertThat(storageException.getMessage(), is("Failed to delete file from the file-store"));
    }

    @Test
    public void shouldRetrieveMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(metadata));

        assertThat(fileStore.retrieveMetadata(fileId), is(of(metadata)));
        verify(connection).close();
    }

    @Test
    public void shouldReturnEmptyIfNoFileExistsWhenRetrievingMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());

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
        assertThat(storageException.getMessage(), is("Failed to read metadata from the database"));
    }
}
