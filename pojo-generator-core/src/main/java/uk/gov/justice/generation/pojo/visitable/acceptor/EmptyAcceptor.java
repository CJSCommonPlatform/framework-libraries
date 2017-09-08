package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.Schema;

public class EmptyAcceptor implements Acceptable {

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        visitor.visit(fieldName, (EmptySchema) schema);
    }
}
