package uk.gov.justice.generation.pojo.visitable.acceptor;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArrayAcceptorTest {

    @Mock
    private AcceptorService acceptorService;

    @InjectMocks
    private ArrayAcceptor arrayAcceptor;

    @Test
    public void shouldAcceptArraySchemaWithAnAllItemSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ArraySchema arraySchema = mock(ArraySchema.class);
        final Schema allItemSchema = mock(Schema.class);

        when(arraySchema.getAllItemSchema()).thenReturn(allItemSchema);

        arrayAcceptor.accept(fieldName, arraySchema, visitor);

        final InOrder inOrder = inOrder(visitor, acceptorService);

        inOrder.verify(visitor).enter(fieldName, arraySchema);
        inOrder.verify(acceptorService).visitSchema(fieldName, allItemSchema, visitor);
        inOrder.verify(visitor).leave(arraySchema);
    }

    @Test
    public void shouldAcceptArraySchemaWithAListOfItemSchemas() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ArraySchema arraySchema = mock(ArraySchema.class);
        final Schema itemSchema_1 = mock(Schema.class);
        final Schema itemSchema_2 = mock(Schema.class);

        when(arraySchema.getAllItemSchema()).thenReturn(null);
        when(arraySchema.getItemSchemas()).thenReturn(asList(itemSchema_1, itemSchema_2));

        arrayAcceptor.accept(fieldName, arraySchema, visitor);

        verify(visitor, atMost(1)).enter(fieldName, arraySchema);
        verify(acceptorService).visitSchema(fieldName, itemSchema_1, visitor);
        verify(acceptorService).visitSchema(fieldName, itemSchema_2, visitor);
        verify(visitor, atMost(1)).leave(arraySchema);
    }
}
