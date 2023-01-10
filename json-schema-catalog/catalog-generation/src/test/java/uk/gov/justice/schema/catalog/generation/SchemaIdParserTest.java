package uk.gov.justice.schema.catalog.generation;

import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import javax.json.stream.JsonParsingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class SchemaIdParserTest {

    @Mock
    private Logger logger;

    @Spy
    @SuppressWarnings("unused")
    private final UrlConverter urlConverter = new UrlConverter();

    @InjectMocks
    private SchemaIdParser schemaIdParser;


    @Test
    public void shouldParseTheSchemaFileAndExtractItsId() throws Exception {

        final URL schemaFile = getClass().getClassLoader().getResource("raml/json/schema/context/person.json");

        final Optional<URI> id = schemaIdParser.parse(schemaFile);

        assertThat(id, is(of(new URI("http://justice.gov.uk/context/person.json"))));
    }

    @Test
    public void shouldFailIfTheSchemaHasNoId() throws Exception {

        final URL schemaFile = getClass().getClassLoader().getResource("dodgy-schemas/schema-with-missing-id.json");

        assertThat(schemaIdParser.parse(schemaFile).isPresent(), is(false));

        final ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(logger).warn(stringArgumentCaptor.capture());

        final String warningMessage = stringArgumentCaptor.getValue();
        assertThat(warningMessage, startsWith("Failed to generate catalog. Schema 'file:"));
        assertThat(warningMessage, endsWith("/dodgy-schemas/schema-with-missing-id.json' has no id"));
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

    @Test
    public void shouldFailIfTheSchemaDoesNotParse() throws Exception {

        final URL schemaFile = getClass().getClassLoader().getResource("dodgy-schemas/schema-with-missing-curly-brace.json");

        try {
            schemaIdParser.parse(schemaFile);
            fail();
        } catch (final CatalogGenerationException expected) {
            assertThat(expected.getCause(), is(instanceOf(JsonParsingException.class)));
            assertThat(expected.getMessage(), containsString("Failed to parse schema file '"));
            assertThat(expected.getMessage(), containsString("dodgy-schemas/schema-with-missing-curly-brace.json'"));
        }
    }
}
