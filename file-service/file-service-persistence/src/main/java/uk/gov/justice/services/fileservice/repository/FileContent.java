package uk.gov.justice.services.fileservice.repository;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.InputStream;

public class FileContent implements AutoCloseable {

    private final InputStream content;

    public FileContent(final InputStream content) {
        this.content = content;
    }

    public InputStream getContent() {
        return content;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void close()  {
        closeQuietly(content);
    }
}
