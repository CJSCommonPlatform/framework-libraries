package uk.gov.justice.services.common.file;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Optional;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

public class ContentTypeDetector {

    public Optional<String> detectContentTypeOf(final BufferedInputStream inputStream) {

        try {

            final TikaConfig config = TikaConfig.getDefaultConfig();
            final Detector detector = config.getDetector();

            final TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);

            final Metadata metadata = new Metadata();
            final MediaType mediaType = detector.detect(tikaInputStream, metadata);

            return ofNullable(mediaType.getType() + "/" + mediaType.getSubtype());
        } catch (final IOException e) {
            return empty();
        }
    }
}
