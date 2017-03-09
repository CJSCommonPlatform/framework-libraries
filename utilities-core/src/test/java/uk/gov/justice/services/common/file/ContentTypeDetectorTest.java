package uk.gov.justice.services.common.file;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import org.junit.Test;

public class ContentTypeDetectorTest {

    private final ContentTypeDetector contentTypeDetector = new ContentTypeDetector();

    private final ClasspathFileResource classpathFileResource = new ClasspathFileResource();

    @Test
    public void shouldDetectTheContentTypeOfJpeg() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/croydon.jpg");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final Optional<String> contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.isPresent(), is(true));
            assertThat(contentType.get(), is("image/jpeg"));
        }
    }

    @Test
    public void shouldLeaveTheInputStreamReadable() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/croydon.jpg");
        final int length = toByteArray(new FileInputStream(file)).length;


        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final Optional<String> contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.isPresent(), is(true));
            assertThat(contentType.get(), is("image/jpeg"));

            final byte[] bytes = toByteArray(bufferedInputStream);

            assertThat(bytes.length, is(length));
        }
    }

    @Test
    public void shouldDetectTheContentTypeOfPdf() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/Onion.pdf");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final Optional<String> contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.isPresent(), is(true));
            assertThat(contentType.get(), is("application/pdf"));
        }
    }
    
    @Test
    public void shouldDetectTheContentTypeOfPng() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/ScreenShot.png");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final Optional<String> contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.isPresent(), is(true));
            assertThat(contentType.get(), is("image/png"));
        }
    }

    @Test
    public void shouldDetectTheContentTypeOfZip() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/zipfile.zip");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final Optional<String> contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.isPresent(), is(true));
            assertThat(contentType.get(), is("application/zip"));
        }
    }

    @Test
    public void shouldHandleCsvFiles() throws Exception {

        final File file = classpathFileResource.getFileFromClasspath("/test-files/values.csv");

        try(final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            final Optional<String> contentType = contentTypeDetector.detectContentTypeOf(bufferedInputStream);

            assertThat(contentType.isPresent(), is(true));
            assertThat(contentType.get(), is("text/plain"));
        }
    }
}
