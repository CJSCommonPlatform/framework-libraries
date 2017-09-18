package uk.gov.justice.generation.pojo.validation;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;

import java.util.List;
import java.util.Set;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.Schema;

public class Validator {

    /**
     * Validates that an Enum Schema contains members that are the same type. i.e
     * all strings, numbers etc
     *
     * @param enumSchema The Enum Schema for validation
     */
    public void validate(final EnumSchema enumSchema) {
        final Set<Object> possibleValues = enumSchema.getPossibleValues();

        Class<?> clazz = null;
        for(final Object possibleValue: possibleValues) {
            if (clazz == null) {
                clazz = possibleValue.getClass();
            } else {
                if (possibleValue.getClass() != clazz) {
                    final String message = format(
                            "Enums must have members of the same type. " +
                            "Found %s, %s out of possible values %s",
                            clazz.getName(),
                            possibleValue.getClass().getName(),
                            possibleValues);

                    throw new UnsupportedSchemaException(message);
                }
            }
        }
    }

    public void validate(final ArraySchema arraySchema) {
        final List<Schema> itemSchemas = arraySchema.getItemSchemas();

        Class<? extends Schema> clazz = null;
        for (final Schema schema: itemSchemas) {

            if (clazz == null) {
                clazz = schema.getClass();
            } else {
                if (schema.getClass() != clazz) {
                    final String message = format(
                            "Arrays must have members of the same type. " +
                                    "Found %s, %s",
                            clazz.getName(),
                            schema.getClass().getName());
                    throw new UnsupportedSchemaException(message);
                }
            }
        }
    }
}
