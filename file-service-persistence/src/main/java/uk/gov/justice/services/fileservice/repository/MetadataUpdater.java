package uk.gov.justice.services.fileservice.repository;

import static uk.gov.justice.services.messaging.JsonObjects.createObjectBuilder;

import uk.gov.justice.services.common.converter.ZonedDateTimes;
import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.fileservice.api.StorageException;
import uk.gov.justice.services.utilities.file.ContentTypeDetector;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.MediaType;

/**
 * Adds default required fields to the metadata json. Currently these fields are:
 *
 * mediaType: deduced from the input stream if not already present in the json
 * currentTime: current utc time when the file stored
 */
public class MetadataUpdater {

    @Inject
    ContentTypeDetector contentTypeDetector;

    @Inject
    UtcClock utcClock;

    /**
     * Adds mediaType and currentTime to the metadata json.
     *
     * If mediaType is already present in the json then not changed.
     * If no mediaType found in the json then the mediaType is deduced from the InputStream.
     * If no mediaType can be deduced AND none found prevously in the json then the default of
     * "application/octet-stream" is used
     *
     *
     * @param metadata the metadata json to be updated
     * @param contentStream the file content stream, used to deduce the mediaType
     * @return the updated json
     * @throws StorageException if reading the {@link BufferedInputStream} throws an {@link IOException}
     */
    public JsonObject addMediaTypeAndCreatedTime(final JsonObject metadata, final BufferedInputStream contentStream) throws StorageException {

        final JsonObjectBuilder objectBuilder = createObjectBuilder(metadata)
                .add("createdAt", ZonedDateTimes.toString(utcClock.now()));

        if (! metadata.containsKey("mediaType")) {
            objectBuilder.add("mediaType", getMediaTypeFrom(contentStream));
        }

        return objectBuilder.build();
    }

    private String getMediaTypeFrom(final BufferedInputStream contentStream) throws StorageException {
        try {
            final MediaType mediaType = contentTypeDetector.detectContentTypeOf(contentStream);
            return mediaType.getType() + "/" + mediaType.getSubtype();
        } catch (final IOException e) {
            throw new StorageException("Failed to get media type of file from InputStream", e);
        }
    }
}
