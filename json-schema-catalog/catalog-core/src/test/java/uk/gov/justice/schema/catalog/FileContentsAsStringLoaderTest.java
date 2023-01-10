package uk.gov.justice.schema.catalog;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileContentsAsStringLoaderTest {

    @InjectMocks
    private FileContentsAsStringLoader fileContentsAsStringLoader;

    @Test
    public void shouldLoadADocumentAsAString() throws Exception {

        final URL url = getClass().getClassLoader().getResource("json/schema/context/person.json");

        assertThat(url, is(notNullValue()));

        final String json = fileContentsAsStringLoader.readFileContents(url);

        assertThat(json, is(notNullValue()));

        with(json)
                .assertThat("$.$schema", is("http://json-schema.org/draft-04/schema#"))
                .assertThat("$.id", is("http://justice.gov.uk/context/person.json"))
                .assertThat("$.type", is("object"))
                .assertThat("$.properties.nino.type", is("string"))
                .assertThat("$.properties.name.type", is("string"))
                .assertThat("$.properties.correspondence_address.$ref", is("http://justice.gov.uk/standards/complex_address.json#/definitions/complex_address2"))
        ;
    }

    @Test
    public void shouldFailIfReadingTheFileThrowsAnIOException() throws Exception {

        final URL url = new File("this/file/does/not/exist.json").toURI().toURL();

        try {
            fileContentsAsStringLoader.readFileContents(url);
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(FileNotFoundException.class)));
            assertThat(expected.getMessage(), startsWith("Failed to read file contents from 'file:"));
            assertThat(expected.getMessage(), endsWith("this/file/does/not/exist.json'"));
        }
    }
}
