package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SchemaLoaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldLoadObjectSchema() throws Exception {
        final ObjectSchema objectSchema = (ObjectSchema) new SchemaLoader().loadFrom(get("src/test/resources/schemas/person-schema.json").toFile());

        final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();
        assertThat(propertySchemas.get("firstName"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("lastName"), is(instanceOf(StringSchema.class)));
    }

    @Test
    public void shouldThrowExceptionIfFileDoesNotExist() throws Exception {
        expectedException.expect(SchemaLoaderException.class);
        String message = "File failed to load: src/test/resources/schemas/unknown-file.json".replace('/', File.separatorChar);
        expectedException.expectMessage(message);
        expectedException.expectCause(isA(FileNotFoundException.class));

        new SchemaLoader().loadFrom(get("src/test/resources/schemas/unknown-file.json").toFile());
    }
}
