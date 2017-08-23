package uk.gov.justice.generation.pojo.core;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.List;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public interface Visitor {

    void enter(final String fieldName, final Schema schema);

    void leave(final Schema schema);

    void visit(final String fieldName, final BooleanSchema schema);

    void visit(final String fieldName, final EnumSchema schema);

    void visit(final String fieldName, final NumberSchema schema);

    void enter(String fieldName, ArraySchema schema);

    void leave(ArraySchema schema);

    void visit(final String fieldName, final StringSchema schema);

    List<Definition> getDefinitions();
}
