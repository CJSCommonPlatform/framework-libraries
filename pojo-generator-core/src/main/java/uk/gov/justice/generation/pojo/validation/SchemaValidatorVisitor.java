package uk.gov.justice.generation.pojo.validation;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.StringSchema;

/**
 * Runs validation against each schema in the json document
 */
public class SchemaValidatorVisitor implements Visitor {

    private final Validator validator;

    public SchemaValidatorVisitor(final Validator validator) {
        this.validator = validator;
    }

    @Override
    public void visit(final String fieldName, final EnumSchema schema) {
        validator.validate(schema);
    }

    @Override
    public void enter(final String fieldName, final ArraySchema schema) {
        validator.validate(schema);
    }

    @Override
    public void enter(final String fieldName, final ObjectSchema schema) {
        validator.validate(schema);
    }

    @Override
    public void leave(final ObjectSchema schema) {
        //Do nothing
    }

    @Override
    public void leave(final ArraySchema schema) {
        //Do nothing
    }

    @Override
    public void enter(final String fieldName, final ReferenceSchema schema) {
        //Do nothing
    }

    @Override
    public void leave(final ReferenceSchema schema) {
        //Do nothing
    }

    @Override
    public void enter(final String fieldName, final CombinedSchema schema) {
        //Do nothing
    }

    @Override
    public void leave(final CombinedSchema schema) {
        //Do nothing
    }

    @Override
    public void visit(final String fieldName, final NumberSchema schema) {
        //Do nothing
    }

    @Override
    public void visit(final String fieldName, final StringSchema schema) {
        //Do nothing
    }

    @Override
    public void visit(final String fieldName, final BooleanSchema schema) {
        //Do nothing
    }

    @Override
    public void visit(final String fieldName, final NullSchema schema) {
        //Do nothing
    }

    @Override
    public void visit(final String fieldName, final EmptySchema schema) {
        //Do nothing
    }
}
