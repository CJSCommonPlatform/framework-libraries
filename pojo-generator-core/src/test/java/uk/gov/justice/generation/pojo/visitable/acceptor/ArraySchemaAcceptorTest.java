package uk.gov.justice.generation.pojo.visitable.acceptor;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitable.JsonSchemaWrapper;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArraySchemaAcceptorTest {

    @Mock
    private JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    @InjectMocks
    private ArraySchemaAcceptor arraySchemaAcceptor;

    @Test
    public void shouldAcceptArraySchemaWithAnAllItemSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ArraySchema arraySchema = mock(ArraySchema.class);
        final Schema allItemSchema = mock(Schema.class);

        when(arraySchema.getAllItemSchema()).thenReturn(allItemSchema);

        arraySchemaAcceptor.accept(fieldName, visitor, arraySchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaAcceptorFactory);

        inOrder.verify(visitor).enter(fieldName, arraySchema);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema(fieldName, visitor, allItemSchema);
        inOrder.verify(visitor).leave(arraySchema);
    }

    @Test
    public void shouldAcceptArraySchemaWithAListOfItemSchemas() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ArraySchema arraySchema = mock(ArraySchema.class);
        final Schema itemSchema_1 = mock(Schema.class);
        final Schema itemSchema_2 = mock(Schema.class);
        final JsonSchemaWrapper jsonSchemaWrapper_1 = mock(JsonSchemaWrapper.class);
        final JsonSchemaWrapper jsonSchemaWrapper_2 = mock(JsonSchemaWrapper.class);

        when(arraySchema.getAllItemSchema()).thenReturn(null);
        when(arraySchema.getItemSchemas()).thenReturn(asList(itemSchema_1, itemSchema_2));

        arraySchemaAcceptor.accept(fieldName, visitor, arraySchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaAcceptorFactory);

        inOrder.verify(visitor).enter(fieldName, arraySchema);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema(fieldName, visitor, itemSchema_1);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema(fieldName, visitor, itemSchema_2);
        inOrder.verify(visitor).leave(arraySchema);
    }
}
