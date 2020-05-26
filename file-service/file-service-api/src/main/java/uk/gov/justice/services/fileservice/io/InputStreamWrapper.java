package uk.gov.justice.services.fileservice.io;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

public class InputStreamWrapper extends InputStream {

    private final InputStream inputStream;
    private final Connection connection;

    public InputStreamWrapper(
            final InputStream inputStream,
            final Connection connection) {
        this.inputStream = inputStream;
        this.connection = connection;
    }


    @Override
    public void close() throws IOException {

        try {
            close(inputStream);
        } finally {
            close(connection);
        }
    }


    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte b[]) throws IOException {
        return inputStream.read(b);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void mark(int readLimit) {
        inputStream.mark(readLimit);
    }

    @Override
    public void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }

    private void close(final AutoCloseable autoCloseable) throws IOException {
        try {
            autoCloseable.close();
        } catch (Exception e) {
            throw new IOException("Failed to close " + autoCloseable.getClass().getSimpleName(), e);
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Connection getConnection() {
        return connection;
    }
}
