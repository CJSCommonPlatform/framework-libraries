package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.Schema;

public interface JsonSchemaAcceptorFactory {

    Map<Class<? extends Schema>, JsonSchemaAcceptor> acceptorMap();

    void visitSchema(String fieldName, Visitor visitor, Schema childSchema);
}
