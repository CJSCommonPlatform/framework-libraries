package uk.gov.justice.generation.pojo.core;

import static java.lang.String.format;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class JsonSchemaWrapper implements Visitable {

    private final Schema schema;

    public JsonSchemaWrapper(final Schema schema) {
        this.schema = schema;
    }

    @Override
    public void accept(final String fieldName, final Visitor visitor) {

        switch (schema.getClass().getSimpleName()) {
            case "ObjectSchema":
                final ObjectSchema objectSchema = (ObjectSchema) schema;

                visitor.enter(fieldName, objectSchema);
                objectSchema.getPropertySchemas()
                        .forEach((childName, childSchema) -> visitChildSchema(childName, visitor, childSchema));
                visitor.leave(objectSchema);
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
            case "NullSchema":
                visitor.visit(fieldName, (NullSchema) schema);
                break;
            case "EmptySchema":
                visitor.visit(fieldName, (EmptySchema) schema);
                break;
            case "ReferenceSchema":
                final ReferenceSchema referenceSchema = (ReferenceSchema) this.schema;
                final Schema referredSchema = referenceSchema.getReferredSchema();
                visitChildSchema(fieldName, visitor, referredSchema);
                break;
            case "ArraySchema":
                visitor.visit(fieldName, (ArraySchema) schema);
                break;
            case "CombinedSchema":
                visitor.visit(fieldName, (CombinedSchema) schema);
                break;
            default:
                throw new UnsupportedSchemaException(format("Schema of type: %s is not supported.", schema.getClass().getSimpleName()));
        }
    }

    private void visitChildSchema(final String fieldName, final Visitor visitor, final Schema childSchema) {
        new JsonSchemaWrapper(childSchema).accept(fieldName, visitor);
    }
}
