package uk.gov.justice.generation.pojo.visitable.acceptor;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

public class StringSchemaAcceptor implements JsonSchemaAcceptor {

    @Override
    public void accept(final String fieldName, final Visitor visitor, final Schema schema) {
        visitor.visit(fieldName, (StringSchema) schema);
    }
}
