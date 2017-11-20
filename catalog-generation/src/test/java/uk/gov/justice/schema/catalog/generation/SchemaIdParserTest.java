package uk.gov.justice.schema.catalog.generation;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaIdParserTest {

    @Spy
    @SuppressWarnings("unused")
    private final UrlConverter urlConverter = new UrlConverter();

    @InjectMocks
    private SchemaIdParser schemaIdParser;

    @Test
    public void shouldParseTheSchemaFileAndExtractItsId() throws Exception {

        final URL schemaFile = getClass().getClassLoader().getResource("raml/json/schema/context/person.json");

        final URI id = schemaIdParser.parse(schemaFile);

        assertThat(id.toString(), is("http://justice.gov.uk/context/person.json"));
    }

    @Test
    public void shouldFailIfTheSchemaHasNoId() throws Exception {

        final URL schemaFile = getClass().getClassLoader().getResource("dodgy-schemas/schema-with-missing-id.json");

        try {
            schemaIdParser.parse(schemaFile);
            fail();
        } catch (final CatalogGenerationException expected) {
            assertThat(expected.getMessage(), startsWith("Failed to generate catalog. Schema 'file:"));
            assertThat(expected.getMessage(), endsWith("/dodgy-schemas/schema-with-missing-id.json' has no id"));
        }
    }

    @Test
    public void shouldFailIfLoadingTheSchemaFileThrowsAnIOException() throws Exception {

        final URL schemaFile = new URL("file:/this/file/does/not/exist.json");

        try {
            schemaIdParser.parse(schemaFile);
            fail();
        } catch (final CatalogGenerationException expected) {
            assertThat(expected.getCause(), is(instanceOf(IOException.class)));
            assertThat(expected.getMessage(), is("Failed to extract id from schema file 'file:/this/file/does/not/exist.json'"));
        }
    }
}
