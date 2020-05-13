package uk.gov.justice.services.test.utils.core.files;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class ClasspathFileResourceTest {

    private final ClasspathFileResource classpathFileResource = new ClasspathFileResource();

    @Test
    public void shouldLoadAFileFromTheClasspath() throws Exception {

        final String path = "/test-files/cat.jpg";
        final File fileFromClasspath = classpathFileResource.getFileFromClasspath(path);

        assertThat(fileFromClasspath.exists(), is(true));
        assertThat(fileFromClasspath.getAbsolutePath().endsWith(path), is(true));
    }

    @Test
    public void shouldThrowARuntimeExceptionIfTheFileCannotBeFound() throws Exception {

        try {
            classpathFileResource.getFileFromClasspath("/does-not-exist.jpg");
            fail();
        } catch (final RuntimeException expected) {
            assertThat(expected.getMessage(), is("'/does-not-exist.jpg' not found on classpath"));
        }
    }
}
