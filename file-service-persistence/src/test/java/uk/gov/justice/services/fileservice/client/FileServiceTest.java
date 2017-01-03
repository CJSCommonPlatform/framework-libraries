package uk.gov.justice.services.fileservice.client;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.repository.FileStore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    @Mock
    private FileStore fileStore;

    @InjectMocks
    private FileService fileService;

    @Test
    public void shouldStoreAFile() throws Exception {

        final JsonObject metadata = mock(JsonObject.class);
        final InputStream content = new ByteArrayInputStream("the file content".getBytes());

        fileService.store(metadata, content);

        verify(fileStore).store(metadata, content);
    }

    @Test
    public void shouldRetrieveAFile() throws Exception {

        final UUID fileId = UUID.randomUUID();

        final Optional<FileReference> retrievableFile = of(mock(FileReference.class));

        when(fileStore.find(fileId)).thenReturn(retrievableFile);

        assertThat(fileService.retrieve(fileId), is(retrievableFile));
    }

    @Test
    public void shouldUpdateMetadata() throws Exception {

        final UUID fileId = UUID.randomUUID();
        final JsonObject metadata = mock(JsonObject.class);

        fileService.updateMetadata(fileId, metadata);

        verify(fileStore).updateMetadata(fileId, metadata);
    }

    @Test
    public void shouldDeleteAFile() throws Exception {

        final UUID fileId = UUID.randomUUID();

        fileService.delete(fileId);

        verify(fileStore).delete(fileId);
    }

    @Test
    public void shouldRetrieveMetadata() throws Exception {

        final UUID fileId = UUID.randomUUID();

        final Optional<JsonObject> metadata = of(mock(JsonObject.class));

        when(fileStore.retrieveMetadata(fileId)).thenReturn(metadata);

        assertThat(fileService.retrieveMetadata(fileId), is(metadata));
    }
}
