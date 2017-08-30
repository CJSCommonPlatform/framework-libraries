package uk.gov.justice.generation.pojo.visitable;

import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorFactory;

import org.everit.json.schema.Schema;

public class VisitableSchemaFactory {

    public VisitableSchema createWith(final Schema schema, final AcceptorFactory acceptorFactory) {
        return new VisitableSchema(schema, acceptorFactory);
    }
}
