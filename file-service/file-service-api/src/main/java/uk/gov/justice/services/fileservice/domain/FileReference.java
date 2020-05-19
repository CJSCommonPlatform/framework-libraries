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
public class FileReference implements AutoCloseable {

    private final UUID fileId;
    private final JsonObject metadata;
    private final InputStream contentStream;
    private final Boolean deleted;

    public FileReference(
            final UUID fileId,
            final JsonObject metadata,
            final InputStream contentStream) {
        this(fileId, metadata, contentStream, false);
    }

    public FileReference(
            final UUID fileId,
            final JsonObject metadata,
            final InputStream contentStream,
            final Boolean deleted) {
        this.fileId = fileId;
        this.metadata = metadata;
        this.contentStream = contentStream;
        this.deleted = deleted;
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

    /**
     * @return the deleted flag of this File
     */
    public Boolean isDeleted() {
        return deleted;
    }

    /**
     * Implementation of {@link AutoCloseable} {@code close()}. Closes
     * the content stream of the file, which in turn closes the sql {@link java.sql.Connection}
     * to the database.
     *
     * @throws Exception if closing the content stream fails
     */
    @Override
    public void close() throws Exception {

        if(contentStream != null) {
            contentStream.close();
        }
    }

    @Override
    public String toString() {
        return "FileReference{" +
                "fileId=" + fileId +
                ", metadata=" + metadata +
                ", deleted=" + deleted +
                '}';
    }
}
