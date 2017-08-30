package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.Schema;

public interface AcceptorFactory {

    Map<Class<? extends Schema>, Acceptable> acceptorMap();

    void visitSchema(String fieldName, Visitor visitor, Schema childSchema);
}
