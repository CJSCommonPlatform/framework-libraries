package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.Schema;

public class NumberSchemaAcceptor implements JsonSchemaAcceptor {

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        visitor.visit(fieldName, (NumberSchema) schema);
    }
}
