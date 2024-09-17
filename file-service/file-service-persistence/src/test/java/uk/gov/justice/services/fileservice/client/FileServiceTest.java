package uk.gov.justice.services.fileservice.client;

import static java.time.ZoneOffset.UTC;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.domain.FileReference;
import uk.gov.justice.services.fileservice.repository.FileStore;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.json.JsonObject;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileStore fileStore;

    @Mock
    private FileStorePurgeConfiguration fileStorePurgeConfiguration;

    @Mock
    private UtcClock clock;

    @Mock
    private Logger logger;

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

        verify(fileStore).markAsDeleted(fileId);
    }

    @Test
    public void shouldRetrieveMetadata() throws Exception {

        final UUID fileId = UUID.randomUUID();

        final Optional<JsonObject> metadata = of(mock(JsonObject.class));

        when(fileStore.retrieveMetadata(fileId)).thenReturn(metadata);

        assertThat(fileService.retrieveMetadata(fileId), is(metadata));
    }

    @Test
    public void shouldPurgeSoftDeletedFiles() throws Exception {

        final int purgeFilesOlderThanNumberOfDays = 28;
        final int maxNumberToDelete = 100;
        final ZonedDateTime now = ZonedDateTime.of(2023, 11, 29, 11, 0,0, 0, UTC);
        final ZonedDateTime purgeDateTime = ZonedDateTime.of(2023, 11, 1, 11, 0,0, 0, UTC);
        final int filesDeleted = 23;

        when(fileStorePurgeConfiguration.getPurgeFilesOlderThanNumberOfDays()).thenReturn(purgeFilesOlderThanNumberOfDays);
        when(fileStorePurgeConfiguration.getMaximumNumberOfFilesToPurge()).thenReturn(maxNumberToDelete);
        when(clock.now()).thenReturn(now);

        when(fileStore.purgeFilesOlderThan(purgeDateTime, maxNumberToDelete)).thenReturn(filesDeleted);

        fileService.purgeOldestSoftDeletedFiles();

        final InOrder inOrder = inOrder(logger, fileStore);

        inOrder.verify(logger).info("Purging a maximum of 100 files from the file store deleted before 2023-11-01T11:00Z");
        inOrder.verify(fileStore).purgeFilesOlderThan(purgeDateTime, maxNumberToDelete);
        inOrder.verify(logger).info("Purged 23 files from the file store");
    }
}
