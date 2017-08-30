package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.EnumSchema;
import org.junit.Test;

public class EnumAcceptorTest {

    @Test
    public void shouldVisitEnumSchema() throws Exception {
        final String fieldName = "fieldName";
        final EnumSchema enumSchema = mock(EnumSchema.class);
        final Visitor visitor = mock(Visitor.class);
        final EnumAcceptor enumAcceptor = new EnumAcceptor();

        enumAcceptor.accept(fieldName, visitor, enumSchema);

        verify(visitor).visit(fieldName, enumSchema);
    }
}
