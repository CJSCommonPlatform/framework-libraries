package uk.gov.justice.services.fileservice.repository;

import java.io.InputStream;

public class FileContent {

    private final InputStream content;
    private final Boolean deleted;

    public FileContent(final InputStream content, final Boolean deleted) {
        this.content = content;
        this.deleted = deleted;
    }

    public InputStream getContent() {
        return content;
    }

    public Boolean isDeleted() {
        return deleted;
    }
}
