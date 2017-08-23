package uk.gov.justice.generation.pojo.core;

import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class JsonSchemaWrapperDelegate {

    private final CombinedSchemaPropertyExtractor combinedSchemaPropertyExtractor;
    private final JsonSchemaWrapperFactory jsonSchemaWrapperFactory;

    public JsonSchemaWrapperDelegate() {
        this(new CombinedSchemaPropertyExtractor(), new JsonSchemaWrapperFactory());
    }

    @VisibleForTesting
    JsonSchemaWrapperDelegate(
            final CombinedSchemaPropertyExtractor combinedSchemaPropertyExtractor,
            final JsonSchemaWrapperFactory jsonSchemaWrapperFactory) {
        this.combinedSchemaPropertyExtractor = combinedSchemaPropertyExtractor;
        this.jsonSchemaWrapperFactory = jsonSchemaWrapperFactory;
    }

    public void acceptCombinedSchema(final String fieldName, final Visitor visitor, final CombinedSchema combinedSchema) {

        final Map<String, Schema> propertySchemas = combinedSchemaPropertyExtractor.getAllPropertiesFrom(combinedSchema);

        visitor.enter(fieldName, combinedSchema);
        propertySchemas.forEach((childName, childSchema) -> visitChildSchema(childName, visitor, childSchema));
        visitor.leave(combinedSchema);
    }

    public void acceptReferenceSchema(final String fieldName, final Visitor visitor, final ReferenceSchema referenceSchema) {
        final Schema referredSchema = referenceSchema.getReferredSchema();
        visitChildSchema(fieldName, visitor, referredSchema);
    }

    public void acceptObjectSchema(final String fieldName, final Visitor visitor, final ObjectSchema objectSchema) {
        final Map<String, Schema> propertySchemas = objectSchema.getPropertySchemas();

        visitor.enter(fieldName, objectSchema);
        propertySchemas.forEach((childName, childSchema) -> visitChildSchema(childName, visitor, childSchema));
        visitor.leave(objectSchema);
    }

    public void acceptArraySchema(final String fieldName, final Visitor visitor, final ArraySchema arraySchema) {

        final Schema allItemSchema = arraySchema.getAllItemSchema();

        if (allItemSchema != null) {
            doAcceptArraySchema(fieldName, arraySchema, allItemSchema, visitor);
        } else {
            arraySchema
                    .getItemSchemas()
                    .forEach(schema -> doAcceptArraySchema(fieldName, arraySchema, schema, visitor));
        }

    }

    private void doAcceptArraySchema(final String fieldName, final ArraySchema arraySchema, final Schema schema, final Visitor visitor) {
        visitor.enter(fieldName, arraySchema);
        visitChildSchema(fieldName, visitor, schema);
        visitor.leave(arraySchema);
    }

    private void visitChildSchema(final String fieldName, final Visitor visitor, final Schema childSchema) {
        jsonSchemaWrapperFactory.create(childSchema).accept(fieldName, visitor);
    }
}
