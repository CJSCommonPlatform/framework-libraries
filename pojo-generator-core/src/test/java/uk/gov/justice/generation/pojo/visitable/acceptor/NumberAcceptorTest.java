package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.NumberSchema;
import org.junit.Test;

public class NumberAcceptorTest {

    @Test
    public void shouldVisitNumberSchema() throws Exception {
        final String fieldName = "fieldName";
        final NumberSchema numberSchema = mock(NumberSchema.class);
        final Visitor visitor = mock(Visitor.class);
        final NumberAcceptor numberAcceptor = new NumberAcceptor();

        numberAcceptor.accept(fieldName, visitor, numberSchema);

        verify(visitor).visit(fieldName, numberSchema);
    }
}
