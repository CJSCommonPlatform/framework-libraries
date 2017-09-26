package uk.gov.justice.generation;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;

import java.io.File;

import org.junit.Test;

public class SchemaPojoGeneratorFactoryTest {

    @Test
    public void shouldCreateSchemaPojoGenerator() throws Exception {
        final Generator<File> schemaPojoGenerator = new SchemaPojoGeneratorFactory().create();

        assertThat(schemaPojoGenerator, notNullValue());
        assertThat(schemaPojoGenerator, is(instanceOf(SchemaPojoGenerator.class)));
    }
}
