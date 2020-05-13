package uk.gov.justice.services.test.utils.core.rest;

import static java.util.Optional.empty;

import java.io.InputStream;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

/**
 * Used in conjunction with {@link MultipartRestClient}. Contains the parameters relating to the
 * file part for upload.
 */
public class FilePart {

    private final String filePartName;
    private final String filename;
    private final Optional<MediaType> mediaType;
    private final InputStream inputStream;

    /**
     * Creates a FilePart. As no MediaType is specified then the file service will attempt to
     * deduce the media type from the InputStream
     *
     * @param filePartName the name of the file part. Should match the name in the raml
     * @param filename the file name of the file associated with the InputStream
     * @param inputStream the InputStream from the file
     */
    public FilePart(
            final String filePartName,
            final String filename,
            final InputStream inputStream) {
        this(filePartName, filename, empty(), inputStream);
    }

    /**
     * Creates a FilePart with an {@link Optional} {@link MediaType}. If no MediaType is specified then
     * the file service will attempt to deduce the media type from the InputStream
     *
     * @param filePartName the name of the file part. Should match the name in the raml
     * @param filename the file name of the file associated with the InputStream
     * @param mediaType the {@link Optional} media type of the file
     * @param inputStream the InputStream from the file
     */
    public FilePart(
            final String filePartName,
            final String filename,
            final Optional<MediaType> mediaType,
            final InputStream inputStream) {
        this.filePartName = filePartName;
        this.filename = filename;
        this.mediaType = mediaType;
        this.inputStream = inputStream;
    }

    public String getFilePartName() {
        return filePartName;
    }

    public String getFilename() {
        return filename;
    }

    public Optional<MediaType> getMediaType() {
        return mediaType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
