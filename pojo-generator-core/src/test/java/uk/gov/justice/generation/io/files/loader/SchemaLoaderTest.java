package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.io.FileNotFoundException;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaLoaderTest {

    @Mock
    private SchemaLoaderResolver schemaLoaderResolver;

    @InjectMocks
    private SchemaLoader schemaLoader;

    @Test
    public void shouldLoadObjectSchema() throws Exception {

        final Schema schema = mock(Schema.class);

        when(schemaLoaderResolver.loadSchema(any(JSONObject.class))).thenReturn(schema);

        final File jsonSchemaFile = new ClasspathFileResource()
                .getFileFromClasspath("/schemas/person-schema.json");

        final Schema resultSchema = schemaLoader.loadFrom(jsonSchemaFile);

        assertThat(resultSchema, is(schema));
    }

    @Test
    public void shouldThrowExceptionIfFileDoesNotExist() throws Exception {

        final File jsonSchemaFile = get("src/test/resources/schemas/unknown-file.json").toFile();

        try {
            schemaLoader.loadFrom(jsonSchemaFile);
            fail();
        } catch (final SchemaLoaderException expected) {
            assertThat(expected.getCause(), is(instanceOf(FileNotFoundException.class)));
            assertThat(expected.getMessage(), is("File failed to load: src/test/resources/schemas/unknown-file.json".replace('/', File.separatorChar)));
        }
    }

    @Test
    public void shouldThrowExceptionIfSchemaFileDoesNotHaveAnId() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource()
                .getFileFromClasspath("/schemas/person-schema-with-missing-id.json");

        assertThat(jsonSchemaFile.exists(), is(true));

        try {
            schemaLoader.loadFrom(jsonSchemaFile);
            fail();
        } catch (final SchemaLoaderException expected) {
            assertThat(expected.getMessage(), startsWith("Missing id in Schema file '"));
            assertThat(expected.getMessage(), endsWith("schemas/person-schema-with-missing-id.json'. Unable to load"));
        }
    }
}
