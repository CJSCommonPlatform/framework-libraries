package uk.gov.justice.services.utilities.file;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

public class ContentTypeDetectorTest {

    private final ContentTypeDetector contentTypeDetector = new ContentTypeDetector();

    private final ClasspathFileResource classpathFileResource = new ClasspathFileResource();

    @Test
    public void shouldDetectTheContentTypeOfJpeg() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/croydon.jpg");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final MediaType contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.getType(), is("image"));
            assertThat(contentType.getSubtype(), is("jpeg"));
        }
    }

    @Test
    public void shouldLeaveTheInputStreamReadable() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/croydon.jpg");
        final int length = toByteArray(new FileInputStream(file)).length;


        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final MediaType contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.getType(), is("image"));
            assertThat(contentType.getSubtype(), is("jpeg"));

            final byte[] bytes = toByteArray(bufferedInputStream);

            assertThat(bytes.length, is(length));
        }
    }

    @Test
    public void shouldDetectTheContentTypeOfPdf() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/Onion.pdf");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final MediaType contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.getType(), is("application"));
            assertThat(contentType.getSubtype(), is("pdf"));
        }
    }

    @Test
    public void shouldDetectTheContentTypeOfPng() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/ScreenShot.png");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final MediaType contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.getType(), is("image"));
            assertThat(contentType.getSubtype(), is("png"));
        }
    }

    @Test
    public void shouldDetectTheContentTypeOfZip() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/zipfile.zip");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final MediaType contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.getType(), is("application"));
            assertThat(contentType.getSubtype(), is("zip"));
        }
    }

    @Test
    public void shouldHandleCsvFiles() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/values.csv");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final MediaType contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.getType(), is("text"));
            assertThat(contentType.getSubtype(), is("plain"));
        }
    }
}
