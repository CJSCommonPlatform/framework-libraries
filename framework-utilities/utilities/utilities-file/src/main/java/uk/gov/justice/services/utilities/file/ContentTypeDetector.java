package uk.gov.justice.services.utilities.file;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;

public class ContentTypeDetector {

    public MediaType detectContentTypeOf(final BufferedInputStream inputStream) throws IOException {

        final TikaConfig config = TikaConfig.getDefaultConfig();
        final Detector detector = config.getDetector();

        final TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);

        final Metadata metadata = new Metadata();
        final org.apache.tika.mime.MediaType mediaType = detector.detect(tikaInputStream, metadata);

        return new MediaType(mediaType.getType(), mediaType.getSubtype());
    }
}
