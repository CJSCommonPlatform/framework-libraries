package uk.gov.justice.services.fileservice.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import javax.json.JsonObject;

public class StorableFile {

    private final UUID fileId;
    private final JsonObject metadata;
    private final byte[] content;

    public StorableFile(final UUID fileId, final JsonObject metadata, final byte[] content) {
        this.fileId = fileId;
        this.metadata = metadata;
        this.content = content;
    }

    public UUID getFileId() {
        return fileId;
    }

    public JsonObject getMetadata() {
        return metadata;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StorableFile that = (StorableFile) o;
        return Objects.equals(getFileId(), that.getFileId()) &&
                Objects.equals(getMetadata(), that.getMetadata()) &&
                Arrays.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileId(), getMetadata(), getContent());
    }

    @Override
    public String toString() {
        return "StorableFile{" +
                "fileId=" + fileId +
                ", metadata=" + metadata +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
