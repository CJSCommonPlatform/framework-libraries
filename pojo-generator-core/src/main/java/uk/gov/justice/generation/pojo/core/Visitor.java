package uk.gov.justice.generation.pojo.core;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import java.util.List;

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

public interface Visitor {

    void visitEnter(final ObjectSchema schema);

    void visitLeave(final ObjectSchema schema);

    void visit(final CombinedSchema schema);

    void visit(final ArraySchema schema);

    void visit(final ReferenceSchema schema);

    void visit(final BooleanSchema schema);

    void visit(final EmptySchema schema);

    void visit(final EnumSchema schema);

    void visit(final NullSchema schema);

    void visit(final NumberSchema schema);

    void visit(final StringSchema schema);

    List<ClassDefinition> getDefinitions();
}
