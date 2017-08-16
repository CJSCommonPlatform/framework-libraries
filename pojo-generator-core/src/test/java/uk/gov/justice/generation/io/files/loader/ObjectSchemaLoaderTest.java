package uk.gov.justice.generation.io.files.loader;

import static java.nio.file.Paths.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.util.Map;

import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ObjectSchemaLoaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldLoadObjectSchema() throws Exception {
        final ObjectSchema objectSchema = new ObjectSchemaLoader().loadFrom(get("src/test/resources/schemas/person-schema.json").toFile());

        final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();
        assertThat(propertySchemas.get("firstName"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("lastName"), is(instanceOf(StringSchema.class)));
    }

    @Test
    public void shouldThrowExceptionIfBaseSchemaIsNotOfTypeObject() throws Exception {
        expectedException.expect(SchemaLoaderException.class);
        expectedException.expectMessage("Base schema must be of type object in: src/test/resources/schemas/strings-schema.json");

        new ObjectSchemaLoader().loadFrom(get("src/test/resources/schemas/strings-schema.json").toFile());
    }

    @Test
    public void shouldLoadThrowExceptionIfFileDoesNotExist() throws Exception {
        expectedException.expect(SchemaLoaderException.class);
        expectedException.expectMessage("File failed to load: src/test/resources/schemas/unknown-file.json");
        expectedException.expectCause(isA(FileNotFoundException.class));

        new ObjectSchemaLoader().loadFrom(get("src/test/resources/schemas/unknown-file.json").toFile());
    }
}