package uk.gov.justice.generation.pojo.core;

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
    public void accept(final Visitor visitor) {
        switch (schema.getClass().getSimpleName()) {
            case "ObjectSchema":
                final ObjectSchema objectSchema = (ObjectSchema) schema;

                visitor.visitEnter(objectSchema);
                objectSchema.getPropertySchemas().values()
                        .forEach(childSchema -> visitChildSchema(visitor, childSchema));
                visitor.visitLeave(objectSchema);
                break;
            case "StringSchema":
                visitor.visit((StringSchema) schema);
                break;
            case "BooleanSchema":
                visitor.visit((BooleanSchema) schema);
                break;
            case "NumberSchema":
                visitor.visit((NumberSchema) schema);
                break;
            case "EnumSchema":
                visitor.visit((EnumSchema) schema);
                break;
            case "NullSchema":
                visitor.visit((NullSchema) schema);
                break;
            case "EmptySchema":
                visitor.visit((EmptySchema) schema);
                break;
            case "ReferenceSchema":
                visitor.visit((ReferenceSchema) schema);
                break;
            case "ArraySchema":
                visitor.visit((ArraySchema) schema);
                break;
            case "CombinedSchema":
                visitor.visit((CombinedSchema) schema);
                break;
            default:
                throw new UnsupportedSchemaException(String.format("Schema of type: %s is not supported.", schema.getClass().getSimpleName()));
        }
    }

    private void visitChildSchema(final Visitor visitor, final Schema childSchema) {
        new JsonSchemaWrapper(childSchema).accept(visitor);
    }
}
