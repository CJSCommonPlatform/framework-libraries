package uk.gov.justice.services.fileservice.repository;

import java.io.InputStream;

public class FileContent {

    private final InputStream content;

    public FileContent(final InputStream content) {
        this.content = content;
    }

    public InputStream getContent() {
        return content;
    }
}
