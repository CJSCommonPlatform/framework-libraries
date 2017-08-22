package uk.gov.justice.generation.pojo.core;

import static java.lang.String.format;

import com.google.common.annotations.VisibleForTesting;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class JsonSchemaWrapper implements Visitable {

    private final Schema schema;
    private final JsonSchemaWrapperDelegate jsonSchemaWrapperDelegate;

    public JsonSchemaWrapper(final Schema schema) {
        this(schema, new JsonSchemaWrapperDelegate());
    }

    @VisibleForTesting
    JsonSchemaWrapper(final Schema schema, final JsonSchemaWrapperDelegate jsonSchemaWrapperDelegate) {
        this.schema = schema;
        this.jsonSchemaWrapperDelegate = jsonSchemaWrapperDelegate;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor) {

        switch (schema.getClass().getSimpleName()) {
            case "ObjectSchema":
                jsonSchemaWrapperDelegate.acceptObjectSchema(fieldName, visitor, (ObjectSchema) schema);
                break;
            case "StringSchema":
                visitor.visit(fieldName, (StringSchema) schema);
                break;
            case "BooleanSchema":
                visitor.visit(fieldName, (BooleanSchema) schema);
                break;
            case "NumberSchema":
                visitor.visit(fieldName, (NumberSchema) schema);
                break;
            case "EnumSchema":
                visitor.visit(fieldName, (EnumSchema) schema);
                break;
            case "ReferenceSchema":
                jsonSchemaWrapperDelegate.acceptReferenceSchema(fieldName, visitor, (ReferenceSchema) schema);
                break;
            case "ArraySchema":
                jsonSchemaWrapperDelegate.acceptArraySchema(fieldName, visitor, (ArraySchema) schema);
                break;
            case "CombinedSchema":
                jsonSchemaWrapperDelegate.acceptCombinedSchema(fieldName, visitor, (CombinedSchema) schema);
                break;
            default:
                throw new UnsupportedSchemaException(format("Schema of type: %s is not supported.", this.schema.getClass().getSimpleName()));
        }
    }

    public Schema getSchema() {
        return schema;
    }
}
