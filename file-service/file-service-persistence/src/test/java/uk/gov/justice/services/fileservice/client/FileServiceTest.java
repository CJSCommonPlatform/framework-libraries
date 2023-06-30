package uk.gov.justice.services.fileservice.client;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.repository.FileStore;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileStore fileStore;

    @InjectMocks
    private FileService fileService;

    @Captor
    private ArgumentCaptor<BufferedInputStream>  streamCaptor;

    @Test
    public void shouldStoreAFile() throws Exception {

        final JsonObject metadata = mock(JsonObject.class);
        final String content = "the file content";
        final InputStream contentStream = new ByteArrayInputStream(content.getBytes());

        fileService.store(metadata, contentStream);

        verify(fileStore).store(eq(metadata), streamCaptor.capture());

        final BufferedInputStream bufferedInputStream = streamCaptor.getValue();

        assertThat(IOUtils.toString(bufferedInputStream), is(content));
    }

    @Test
    public void shouldRetrieveAFile() throws Exception {

        final UUID fileId = UUID.randomUUID();

        final Optional<FileReference> retrievableFile = of(mock(FileReference.class));

        when(fileStore.find(fileId)).thenReturn(retrievableFile);

        assertThat(fileService.retrieve(fileId), is(retrievableFile));
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
