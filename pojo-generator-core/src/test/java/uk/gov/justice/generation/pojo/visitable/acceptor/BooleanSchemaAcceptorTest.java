package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.BooleanSchema;
import org.junit.Test;

public class BooleanSchemaAcceptorTest {

    @Test
    public void shouldVisitBooleanSchema() throws Exception {
        final String fieldName = "fieldName";
        final BooleanSchema booleanSchema = mock(BooleanSchema.class);
        final Visitor visitor = mock(Visitor.class);
        final BooleanSchemaAcceptor booleanSchemaAcceptor = new BooleanSchemaAcceptor();

        booleanSchemaAcceptor.accept(fieldName, visitor, booleanSchema);

        verify(visitor).visit(fieldName, booleanSchema);
    }
}
