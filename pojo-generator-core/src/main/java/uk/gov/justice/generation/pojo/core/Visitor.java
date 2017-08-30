package uk.gov.justice.generation.pojo.core;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.List;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;

public interface Visitor {

    void enter(final String fieldName, final ObjectSchema schema);

    void leave(final ObjectSchema schema);

    void enter(final String fieldName, final CombinedSchema schema);

    void leave(final CombinedSchema schema);

    void visit(final String fieldName, final BooleanSchema schema);

    void visit(final String fieldName, final EnumSchema schema);

    void visit(final String fieldName, final NumberSchema schema);

    void enter(String fieldName, ArraySchema schema);

    void leave(ArraySchema schema);

    void visit(final String fieldName, final StringSchema schema);

    List<Definition> getDefinitions();
}
