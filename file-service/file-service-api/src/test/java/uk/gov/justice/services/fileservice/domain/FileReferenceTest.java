package uk.gov.justice.services.fileservice.domain;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

public class FileReferenceTest {

    @SuppressWarnings("EmptyTryBlock")
    @Test
    public void shouldAutoCloseTheFileInputStream() throws Exception {

        final UUID fileId = randomUUID();
        final JsonObject metadata = mock(JsonObject.class);
        final InputStream contentStream = mock(InputStream.class);

        try(final FileReference ignored = new FileReference(fileId, metadata, contentStream)) {
            // do stuff
        }

        verify(contentStream).close();
    }

    @SuppressWarnings("EmptyTryBlock")
    @Test
    public void shouldIgnoreExceptionsOnClose() throws Exception {

        final UUID fileId = randomUUID();
        final JsonObject metadata = mock(JsonObject.class);
        final InputStream contentStream = mock(InputStream.class);

        doThrow(new IOException()).when(contentStream).close();

        try(final FileReference ignored = new FileReference(fileId, metadata, contentStream)) {
            // do stuff
        }

        verify(contentStream).close();
    }
}
