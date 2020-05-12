package uk.gov.justice.generation.pojo.plugin.classmodifying.hashcode;

import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class SerialVersionUIDGenerator {

    public long generateHashCode(final Schema schema) {

        long hash = 0;

        if (schema instanceof ObjectSchema) {
            final ObjectSchema objectSchema = (ObjectSchema) schema;

            for(final Schema childSchema: objectSchema.getPropertySchemas().values()) {
                hash = hash * 23 + generateHashCode(childSchema);
            }
        } else if (schema instanceof ReferenceSchema) {
            final ReferenceSchema referenceSchema = (ReferenceSchema) schema;

            return generateHashCode(referenceSchema.getReferredSchema());

        } else if(schema instanceof CombinedSchema) {
            final CombinedSchema combinedSchema = (CombinedSchema) schema;

            for(final Schema childSchema: combinedSchema.getSubschemas()) {
                hash = hash * 23 + generateHashCode(childSchema);
            }
        }

        final String schemaWithoutWhitespace = removeWhitespaceFrom(schema.toString());

        return hash * 23 + generateHashCodeFrom(schemaWithoutWhitespace);
    }

    private String removeWhitespaceFrom(final String aString) {
        return aString.replaceAll("\\s+","");
    }

    private long generateHashCodeFrom(final String aString) {
        return aString.chars()
                .mapToLong(value -> value)
                .reduce(0, (previousValue, currentChar) -> 31 * previousValue + currentChar);
    }
}
