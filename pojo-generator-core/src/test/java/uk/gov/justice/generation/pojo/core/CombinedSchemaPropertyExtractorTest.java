package uk.gov.justice.generation.pojo.core;

import static org.everit.json.schema.CombinedSchema.ALL_CRITERION;
import static org.everit.json.schema.CombinedSchema.ONE_CRITERION;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;

import com.fasterxml.jackson.module.jsonSchema.types.NumberSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CombinedSchemaPropertyExtractorTest {

    @InjectMocks
    private CombinedSchemaPropertyExtractor combinedSchemaPropertyExtractor;

    @Test
    public void shouldExtractAllPropertiesFromAllSubSchemas() throws Exception {

        final ObjectSchema defaultFieldsSchema = ObjectSchema.builder()
                .addPropertySchema("address1", StringSchema.builder().build())
                .addPropertySchema("address2", StringSchema.builder().build())
                .addPropertySchema("city", StringSchema.builder().build())
                .build();

        final ObjectSchema optionalFieldsSchema1 = ObjectSchema.builder()
                .addPropertySchema("state", StringSchema.builder().build())
                .addPropertySchema("zipcode", StringSchema.builder().build())
                .build();

        final ObjectSchema optionalFieldsSchema2 = ObjectSchema.builder()
                .addPropertySchema("county", StringSchema.builder().build())
                .addPropertySchema("postcode", StringSchema.builder().build())
                .build();

        final CombinedSchema innerCombinedSchema = CombinedSchema.builder()
                .subschema(optionalFieldsSchema1)
                .subschema(optionalFieldsSchema2)
                .criterion(ONE_CRITERION)
                .build();

        final CombinedSchema outerCombinedSchema = CombinedSchema.builder()
                .subschema(defaultFieldsSchema)
                .subschema(innerCombinedSchema)
                .criterion(ALL_CRITERION)
                .build();


        final Map<String, Schema> propertySchemas = combinedSchemaPropertyExtractor.getAllPropertiesFrom(outerCombinedSchema);

        assertThat(propertySchemas.size(), is(7));
        assertThat(propertySchemas.get("address1"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("address2"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("city"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("county"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("postcode"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("state"), is(instanceOf(StringSchema.class)));
        assertThat(propertySchemas.get("zipcode"), is(instanceOf(StringSchema.class)));
    }

    @Test
    public void shouldFailIfAnyOfTheSubSchemasAreNeitherObjectSchemaNorCombinedSchema() throws Exception {

        final EmptySchema emptySchema = EmptySchema.builder().build();

        final CombinedSchema outerCombinedSchema = CombinedSchema.builder()
                .subschema(emptySchema)
                .criterion(ALL_CRITERION)
                .build();

        try {
            combinedSchemaPropertyExtractor.getAllPropertiesFrom(outerCombinedSchema);
            fail();
        } catch (final UnsupportedOperationException expected) {
            assertThat(expected.getMessage(), is("Schemas of type EmptySchema not supported as sub schemas of CombinedSchema"));
        }
    }
}
