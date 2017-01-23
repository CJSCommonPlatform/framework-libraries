package uk.gov.justice.services.fileservice.domain;

import static java.util.Objects.hash;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import javax.json.JsonObject;

/**
 * Reference to a file stored on the file server. Contains the file's id, the file's metadata as
 * a {@link JsonObject} and an {@link InputStream} of the file's content.
 */
public class FileReference {

    private final UUID fileId;
    private final JsonObject metadata;
    private final InputStream contentStream;

    public FileReference(final UUID fileId, final JsonObject metadata, final InputStream contentStream) {
        this.fileId = fileId;
        this.metadata = metadata;
        this.contentStream = contentStream;
    }

    /**
     * @return The id of the file
     */
    public UUID getFileId() {
        return fileId;
    }

    /**
     * @return The metadata of the file as a {@link JsonObject}
     */
    public JsonObject getMetadata() {
        return metadata;
    }

    /**
     * @return An {@link InputStream} of the stored file.
     */
    public InputStream getContentStream() {
        return contentStream;
    }

    @Override
    public String toString() {
        return "FileReference{" +
                "fileId=" + fileId +
                ", metadata=" + metadata +
                '}';
    }
}
