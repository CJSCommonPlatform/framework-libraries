package uk.gov.justice.services.fileservice.repository;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.fileservice.api.DataIntegrityException;
import uk.gov.justice.services.fileservice.api.FileServiceException;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.io.InputStreamWrapper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FileStoreTest {

    @Mock
    private ContentJdbcRepository contentJdbcRepository;

    @Mock
    private MetadataJdbcRepository metadataJdbcRepository;

    @Mock
    private DataSourceProvider dataSourceProvider;

    @Mock
    private MetadataUpdater metadataUpdater;

    @Captor
    private ArgumentCaptor<UUID> fileIdCaptor_1;

    @Captor
    private ArgumentCaptor<UUID> fileIdCaptor_2;

    @InjectMocks
    private FileStore fileStore;

    @Test
    public void shouldInsertBothTheContentAndTheMetadata() throws Exception {

        final Connection connection = mock(Connection.class);
        final DataSource dataSource = mock(DataSource.class);
        final JsonObject metadata = mock(JsonObject.class);
        final JsonObject updatedMetadata = mock(JsonObject.class);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream)).thenReturn(updatedMetadata);

        final UUID fileId = fileStore.store(metadata, contentStream);

        final InOrder inOrder = inOrder(
                dataSourceProvider,
                contentJdbcRepository,
                metadataJdbcRepository,
                connection);

        inOrder.verify(dataSourceProvider).getDatasource();
        inOrder.verify(contentJdbcRepository).insert(fileIdCaptor_1.capture(), eq(contentStream), eq(connection));
        inOrder.verify(metadataJdbcRepository).insert(fileIdCaptor_2.capture(), eq(updatedMetadata), eq(connection));
        inOrder.verify(connection).close();

        assertThat(fileIdCaptor_1.getValue(), is(fileId));
        assertThat(fileIdCaptor_2.getValue(), is(fileId));
    }

    @Test
    public void shouldCloseTheConnectionWhenAnExceptionIsThrownOnInsert() throws Exception {

        final StorageException fileServiceException = new StorageException("Ooops");

        final Connection connection = mock(Connection.class);
        final DataSource dataSource = mock(DataSource.class);
        final JsonObject metadata = mock(JsonObject.class);
        final JsonObject updatedMetadata = mock(JsonObject.class);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream)).thenReturn(updatedMetadata);

        doThrow(fileServiceException).when(metadataJdbcRepository).insert(any(UUID.class), eq(updatedMetadata), eq(connection));

        try {
            fileStore.store(metadata, contentStream);
            fail();
        } catch (final StorageException ignored) {}

        verify(connection).close();
    }

    @Test
    public void shouldThrowStorageExceptionAIfStoringAFileFails() throws Exception {

        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);
        final JsonObject metadata = mock(JsonObject.class);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        try {
            fileStore.store(metadata, contentStream);
            fail();
        } catch (final FileServiceException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to insert file into database"));
        }
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldFindAFileReferenceByUsingTheContentAndMetadataRepositories() throws Exception {

        final UUID fileId = randomUUID();
        final Boolean deleted = true;

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);
        final InputStream contentStream = mock(InputStream.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(metadata));
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(new FileContent(contentStream, deleted)));

        final Optional<FileReference> fileReferenceOptional = fileStore.find(fileId);

        final FileReference fileReference = fileReferenceOptional
                .orElseThrow(() -> new AssertionError("Failed to find FileReference"));

        assertThat(fileReference.getFileId(), is(fileId));
        assertThat(fileReference.getMetadata(), is(metadata));
        assertThat(fileReference.isDeleted(), is(deleted));


        final InputStreamWrapper inputStreamWrapper = (InputStreamWrapper) fileReference.getContentStream();
        assertThat(inputStreamWrapper.getInputStream(), is(contentStream));
        assertThat(inputStreamWrapper.getConnection(), is(connection));


        final InOrder inOrder = inOrder(
                dataSourceProvider,
                metadataJdbcRepository,
                contentJdbcRepository,
                connection);

        inOrder.verify(dataSourceProvider).getDatasource();
        inOrder.verify(metadataJdbcRepository).findByFileId(fileId, connection);
        inOrder.verify(contentJdbcRepository).findByFileId(fileId, connection);
        inOrder.verify(connection, never()).close();
    }

    @Test
    public void shouldReturnEmptyIfNoContentOrMetadataFoundForTheSpecifiedFileId() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());

        assertThat(fileStore.find(fileId).isPresent(), is(false));
    }

    @Test
    public void shouldCloseTheConnectionIfNoMetadataFoundForTheSpecifiedFileId() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());

        fileStore.find(fileId);

        verify(connection).close();

    }


    @Test
    public void shouldThrowADataIntegrityExceptionIfMetadataFoundButNoContent() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(metadata));
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());

        try {
            fileStore.find(fileId);
            fail();
        } catch (final DataIntegrityException expected) {
            assertThat(expected.getMessage(), is("No file content found for file id " + fileId + " but metadata exists for that id"));
        }
    }

    @Test
    public void shouldCloseTheConnectionIfMetadataFoundButNoContent() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(metadata));
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());

        try {
            fileStore.find(fileId);
            fail();
        } catch (final DataIntegrityException expected) {

        }

        verify(connection).close();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldThrowADataIntegrityExceptionIfContentFoundButNoMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final InputStream contentStream = mock(InputStream.class);
        final Boolean deleted = true;

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(new FileContent(contentStream, deleted)));

        try {
            fileStore.find(fileId);
            fail();
        } catch (final DataIntegrityException expected) {
            assertThat(expected.getMessage(), is("No metadata found for file id " + fileId + " but file content exists for that id"));
        }
    }

    @Test
    public void shouldCloseTheConnectionIfContentFoundButNoMetadata() throws Exception {
        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final InputStream contentStream = mock(InputStream.class);
        final Boolean deleted = true;

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(empty());
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(new FileContent(contentStream, deleted)));

        try {
            fileStore.find(fileId);
            fail();
        } catch (final DataIntegrityException expected) {

        }

        verify(connection).close();


    }

    @Test
    public void shouldCloseTheConnectionWhenAnExceptionIsThrownOnFind() throws Exception {

        final StorageException storageException = new StorageException("Ooops");
        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(of(metadata));
        when(contentJdbcRepository.findByFileId(fileId, connection)).thenThrow(storageException);

        try {
            fileStore.find(fileId);
            fail();
        } catch (final StorageException ignored) {
        }

        verify(connection).close();
    }

    @Test
    public void shouldDoNothingWhenAnExceptionIsThrownOnFindAndConnectionIsNull() throws Exception {

        final StorageException storageException = new StorageException("Ooops");

        final DataSource dataSource = mock(DataSource.class);

        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(null);

        when(metadataJdbcRepository.findByFileId(any(UUID.class), any(Connection.class))).thenReturn(of(metadata));
        when(contentJdbcRepository.findByFileId(any(UUID.class), any(Connection.class))).thenThrow(storageException);

        try {
            fileStore.find(randomUUID());
            fail();
        } catch (final StorageException ignored) {
        }


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

        final InOrder inOrder = inOrder(
                dataSourceProvider,
                metadataJdbcRepository,
                connection);

        inOrder.verify(dataSourceProvider).getDatasource();
        inOrder.verify(metadataJdbcRepository).update(fileId, metadata, connection);
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldCloseTheConnectionWhenAnExceptionIsThrownOnUpdateMetadata() throws Exception {

        final StorageException storageException = new StorageException("Ooops");
        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        doThrow(storageException).when(metadataJdbcRepository).update(fileId, metadata, connection);

        try {
            fileStore.updateMetadata(fileId, metadata);
            fail();
        } catch (final StorageException ignored) {
        }

        verify(connection).close();
    }

    @Test
    public void shouldThrowStorageExceptionAIfUpdatingMetadataFails() throws Exception {

        final UUID fileId = randomUUID();

        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);
        final JsonObject metadata = mock(JsonObject.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        try {
            fileStore.updateMetadata(fileId, metadata);
            fail();
        } catch (final StorageException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to update metadata"));
        }
    }

    @Test
    public void shouldDeleteBothTheContentAndMetadata() throws Exception {

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        fileStore.delete(fileId);

        final InOrder inOrder = inOrder(
                dataSourceProvider,
                metadataJdbcRepository,
                contentJdbcRepository,
                connection);

        inOrder.verify(dataSourceProvider).getDatasource();
        inOrder.verify(metadataJdbcRepository).delete(fileId, connection);
        inOrder.verify(contentJdbcRepository).delete(fileId, connection);
        inOrder.verify(connection).close();
    }

    @Test
    public void shouldCloseTheConnectionWhenAnExceptionIsThrownOnDelete() throws Exception {

        final StorageException storageException = new StorageException("Ooops");

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);

        fileStore.delete(fileId);

        doThrow(storageException).when(contentJdbcRepository).delete(fileId, connection);

        verify(connection).close();
    }

    @Test
    public void shouldThrowAStorageExceptionIfDeletingMetadataFails() throws Exception {

        final UUID fileId = randomUUID();

        final SQLException sqlException = new SQLException("Ooops");

        final DataSource dataSource = mock(DataSource.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenThrow(sqlException);

        try {
            fileStore.delete(fileId);
            fail();
        } catch (final FileServiceException expected) {
            assertThat(expected.getCause(), is(sqlException));
            assertThat(expected.getMessage(), is("Failed to delete file from the database"));
        }
    }

    @Test
    public void shouldRetrieveMetadata() throws Exception {
        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);
        final Optional<JsonObject> metadata = of(mock(JsonObject.class));

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenReturn(metadata);

        assertThat(fileStore.retrieveMetadata(fileId), is(metadata));

        verify(connection).close();
    }

    @Test
    public void shouldCloseTheConnectionWhenAnExceptionIsThrownOnRetrieveMetadata() throws Exception {

        final StorageException storageException = new StorageException("Ooops");

        final UUID fileId = randomUUID();

        final DataSource dataSource = mock(DataSource.class);
        final Connection connection = mock(Connection.class);

        when(dataSourceProvider.getDatasource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        when(metadataJdbcRepository.findByFileId(fileId, connection)).thenThrow(storageException);

        try {
            fileStore.retrieveMetadata(fileId);
            fail();
        } catch (final FileServiceException ignored) {
        }

        verify(connection).close();
    }

    @Test
    public void shouldNotTryToCloseANullConnection() throws Exception {

        when(dataSourceProvider.getDatasource()).thenThrow(new StorageException("Ooops"));

        try {
            fileStore.find(randomUUID());
        } catch (final FileServiceException ignored) {
        }
    }
}
