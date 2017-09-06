package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceAcceptorTest {

    @Mock
    private AcceptorFactory acceptorFactory;

    @InjectMocks
    private ReferenceAcceptor referenceAcceptor;

    @Test
    public void shouldAcceptAReferenceSchema() throws Exception {
        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ReferenceSchema referenceSchema = mock(ReferenceSchema.class);
        final Schema referredSchema = mock(Schema.class);

        when(referenceSchema.getReferredSchema()).thenReturn(referredSchema);

        referenceAcceptor.accept(fieldName, visitor, referenceSchema);

        final InOrder inOrder = inOrder(visitor, acceptorFactory);

        inOrder.verify(visitor).enter(fieldName, referenceSchema);
        inOrder.verify(acceptorFactory).visitSchema(fieldName, visitor, referredSchema);
        inOrder.verify(visitor).leave(referenceSchema);
    }
}
