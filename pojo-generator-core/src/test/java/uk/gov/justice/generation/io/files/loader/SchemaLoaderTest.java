package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.services.test.utils.core.files.ClasspathFileResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SchemaLoaderTest {

    private final SchemaLoader schemaLoader = new SchemaLoader();

    @Test
    public void shouldLoadObjectSchema() throws Exception {

        final File jsonSchemaFile = new ClasspathFileResource()
                .getFileFromClasspath("/schemas/person-schema.json");

        final ObjectSchema objectSchema = (ObjectSchema) schemaLoader.loadFrom(jsonSchemaFile);

        final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();
        assertThat(propertySchemas.get("firstName"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("lastName"), is(instanceOf(StringSchema.class)));
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
