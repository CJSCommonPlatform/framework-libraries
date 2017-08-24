package uk.gov.justice.generation.pojo.validation;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;

import com.google.common.annotations.VisibleForTesting;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class SchemaValidatorVisitable implements ValidatingVisitable {

    private final Schema schema;

    public SchemaValidatorVisitable(final Schema schema) {
        this.schema = schema;
    }

    @Override
    public void accept(final ValidatingVisitor visitor) {

        switch (schema.getClass().getSimpleName()) {
            case "ObjectSchema":
                visitor.visit((ObjectSchema) schema);
                break;
            case "EnumSchema":
                visitor.visit((EnumSchema) schema);
                break;
            case "ArraySchema":
                visitor.visit((ArraySchema) schema);
                break;
            case "ReferenceSchema":
                visitor.visit((ReferenceSchema) schema);
                break;
            case "CombinedSchema":
                visitor.visit((CombinedSchema) schema);
                break;
            case "NumberSchema":
            case "StringSchema":
            case "BooleanSchema":
                break;
            default:
                throw new UnsupportedSchemaException(format("Schema of type '%s' is not supported.", this.schema.getClass().getSimpleName()));
        }
    }

    @VisibleForTesting
    public Schema getSchema() {
        return schema;
    }
}
