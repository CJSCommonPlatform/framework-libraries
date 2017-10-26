package uk.gov.justice.generation.pojo.validation;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;

import java.util.Set;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.StringSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {


    @InjectMocks
    private Validator validator;

    @Test
    public void shouldSuccessfullyValidateAnEnumSchemaIfAllTheEnumsAreOfTheSameClass() throws Exception {

        final Set<Object> possibleValues = newHashSet("SOME", "ENUM", "VALUES");

        final EnumSchema enumSchema = EnumSchema.builder().possibleValues(possibleValues).build();

        validator.validate(enumSchema);
    }

    @Test
    public void shouldFailValidationIfTheEnumsAreOfDifferentClasses() throws Exception {

        final Set<Object> possibleValues = newHashSet("SOME", "ENUM", 23);

        final EnumSchema enumSchema = EnumSchema.builder().possibleValues(possibleValues).build();

        try {
            validator.validate(enumSchema);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is("Enums must have members of the same type. Found java.lang.String, java.lang.Integer out of possible values [ENUM, SOME, 23]"));
        }
    }

    @Test
    public void shouldSuccessfullyValidateAnArraySchemaIfAllTheMembersAreOfTheSameClass() throws Exception {

        final StringSchema stringSchema_1 = StringSchema.builder().build();
        final StringSchema stringSchema_2 = StringSchema.builder().build();

        final ArraySchema arraySchema = ArraySchema.builder()
                .addItemSchema(stringSchema_1)
                .addItemSchema(stringSchema_2)
                .build();

        validator.validate(arraySchema);
    }

    @Test
    public void shouldFailValidationIfTheArrayIsOfDifferentClasses() throws Exception {

        final StringSchema stringSchema = StringSchema.builder().build();
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final ArraySchema arraySchema = ArraySchema.builder()
                .addItemSchema(stringSchema)
                .addItemSchema(numberSchema)
                .build();

        final String message = "Arrays must have members of the same type. " +
                "Found org.everit.json.schema.StringSchema, " +
                "org.everit.json.schema.NumberSchema";

        try {
            validator.validate(arraySchema);
            fail();
        } catch (final UnsupportedSchemaException expected) {
            assertThat(expected.getMessage(), is(message));
        }
    }

    @Test
    public void shouldHandleAnArraySchemaWithNullItemSchemas() throws Exception {

        final ArraySchema arraySchema = ArraySchema.builder()
                .build();

        assertThat(arraySchema.getItemSchemas(), is(nullValue()));

        validator.validate(arraySchema);
    }
}
