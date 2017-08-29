package uk.gov.justice.generation.pojo.validation;

import com.google.common.annotations.VisibleForTesting;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class SchemaValidatorVisitor implements ValidatingVisitor {

    private final Validator validator;
    private final SchemaValidatorVisitableFactory schemaValidatorVisitableFactory;

    public SchemaValidatorVisitor() {
        this(new Validator(), new SchemaValidatorVisitableFactory());
    }

    @VisibleForTesting
    SchemaValidatorVisitor(final Validator validator, final SchemaValidatorVisitableFactory schemaValidatorVisitableFactory) {
        this.validator = validator;
        this.schemaValidatorVisitableFactory = schemaValidatorVisitableFactory;
    }

    @Override
    public void visit(final ObjectSchema schema) {
        schema.getPropertySchemas().values()
                .forEach(childSchema -> visitChildSchema(this, childSchema));
    }

    @Override
    public void visit(final EnumSchema schema) {
        validator.validate(schema);
    }

    @Override
    public void visit(final ArraySchema schema) {
        validator.validate(schema);

        final Schema allItemSchema = schema.getAllItemSchema();
        if (allItemSchema != null) {
            visitChildSchema(this, allItemSchema);
        } else {
            schema.getItemSchemas().forEach(childSchema -> visitChildSchema(this, childSchema));
        }
    }

    @Override
    public void visit(final ReferenceSchema schema) {
        final Schema referredSchema = schema.getReferredSchema();
        visitChildSchema(this, referredSchema);
    }

    @Override
    public void visit(final CombinedSchema schema) {
        schema.getSubschemas().forEach(childSchema -> visitChildSchema(this, childSchema));
    }

    private void visitChildSchema(final ValidatingVisitor visitor, final Schema childSchema) {
        schemaValidatorVisitableFactory.create(childSchema).accept(visitor);
    }
}
