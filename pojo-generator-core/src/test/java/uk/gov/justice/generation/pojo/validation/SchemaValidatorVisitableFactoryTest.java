package uk.gov.justice.generation.pojo.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.everit.json.schema.ObjectSchema;
import org.junit.Test;

public class SchemaValidatorVisitableFactoryTest {

    private final SchemaValidatorVisitableFactory schemaValidatorVisitableFactory = new SchemaValidatorVisitableFactory();

    @Test
    public void shouldCreateASchemaValidatorVisitable() throws Exception {

        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        final SchemaValidatorVisitable schemaValidatorVisitable = schemaValidatorVisitableFactory.create(objectSchema);

        assertThat(schemaValidatorVisitable.getSchema(), is(sameInstance(objectSchema)));
    }
}
