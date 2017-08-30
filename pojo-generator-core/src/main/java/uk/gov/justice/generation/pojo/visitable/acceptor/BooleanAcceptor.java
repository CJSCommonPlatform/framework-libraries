package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.Schema;

public class BooleanAcceptor implements Acceptable {

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        visitor.visit(fieldName, (BooleanSchema) schema);
    }
}
