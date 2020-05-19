package uk.gov.justice.generation.pojo.validation;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;

/**
 * interface of the validating visitor
 */
public interface ValidatingVisitor {

    void visit(final ObjectSchema schema);
    void visit(final EnumSchema schema);
    void visit(final ArraySchema schema);
    void visit(final ReferenceSchema schema);
    void visit(final CombinedSchema schema);
}
