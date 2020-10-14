package uk.gov.justice.services.fileservice.repository;

import static com.jayway.jsonassert.JsonAssert.with;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.of;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.utilities.file.ContentTypeDetector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;

import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MetadataUpdaterTest {

    @Mock
    private ContentTypeDetector contentTypeDetector;

    @Mock
    private UtcClock utcClock;

    @InjectMocks
    private MetadataUpdater metadataUpdater;

    @Test
    public void shouldAddTheMediaTypeAndCurrentTimeInTheCorrectFormat() throws Exception {

        final MediaType mediaType = new MediaType("image", "jpeg");
        final ZonedDateTime dateTime = of(2017, 2, 23, 16, 45, 0, 0, UTC);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);

        final String existingValue = "someValue";
        final JsonObject metadata = createObjectBuilder()
                .add("someField", existingValue)
                .build();

        when(utcClock.now()).thenReturn(dateTime);
        when(contentTypeDetector.detectContentTypeOf(contentStream)).thenReturn(mediaType);

        final JsonObject updatedMetadata = metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream);
        final String json = updatedMetadata.toString();

        with(json)
                .assertThat("$.someField", is(existingValue))
                .assertThat("$.mediaType", is("image/jpeg"))
                .assertThat("$.createdAt", is("2017-02-23T16:45:00.000Z"))
        ;
    }

    @Test
    public void shouldNotAddTheMediaTypeIfAlreadyPresentInTheJson() throws Exception {

        final ZonedDateTime dateTime = of(2017, 5, 11, 16, 45, 0, 0, UTC);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);

        final String existingValue = "someValue";
        final JsonObject metadata = createObjectBuilder()
                .add("someField", existingValue)
                .add("mediaType", "image/jpeg")
                .build();

        when(utcClock.now()).thenReturn(dateTime);

        final JsonObject updatedMetadata = metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream);
        final String json = updatedMetadata.toString();

        with(json)
                .assertThat("$.someField", is(existingValue))
                .assertThat("$.mediaType", is("image/jpeg"))
                .assertThat("$.createdAt", is("2017-05-11T16:45:00.000Z"))
        ;

        verifyZeroInteractions(contentTypeDetector);
    }

    @Test
    public void shouldThrowABadRequestExceptionIfGettingTheContentTypeThrowsAnIOException() throws Exception {

        final IOException ioException = new IOException("Ooops");

        final ZonedDateTime dateTime = of(2017, 5, 11, 16, 45, 0, 0, UTC);
        final BufferedInputStream contentStream = mock(BufferedInputStream.class);

        final String existingValue = "someValue";
        final JsonObject metadata = createObjectBuilder()
                .add("someField", existingValue)
                .build();

        when(utcClock.now()).thenReturn(dateTime);
        when(contentTypeDetector.detectContentTypeOf(contentStream)).thenThrow(ioException);

        try {
            metadataUpdater.addMediaTypeAndCreatedTime(metadata, contentStream);
            fail();
        } catch (final StorageException expected) {
            assertThat(expected.getCause(), is(ioException));
            assertThat(expected.getMessage(), is("Failed to get media type of file from InputStream"));
        }
    }
}
