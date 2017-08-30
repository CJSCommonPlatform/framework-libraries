package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.Schema;

public interface JsonSchemaAcceptor {

    void accept(final String fieldName, final Visitor visitor, final Schema schema);
}
