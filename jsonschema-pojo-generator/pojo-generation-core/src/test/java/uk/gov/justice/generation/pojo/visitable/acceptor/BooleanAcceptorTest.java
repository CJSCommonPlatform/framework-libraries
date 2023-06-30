package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.BooleanSchema;
import org.junit.jupiter.api.Test;

public class BooleanAcceptorTest {

    @Test
    public void shouldVisitBooleanSchema() throws Exception {
        final String fieldName = "fieldName";
        final BooleanSchema booleanSchema = mock(BooleanSchema.class);
        final Visitor visitor = mock(Visitor.class);
        final BooleanAcceptor booleanSchemaAcceptor = new BooleanAcceptor();

        booleanSchemaAcceptor.accept(fieldName, booleanSchema, visitor);

        verify(visitor).visit(fieldName, booleanSchema);
    }
}
