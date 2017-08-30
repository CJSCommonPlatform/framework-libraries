package uk.gov.justice.generation.pojo.visitable.acceptor;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.List;

import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CombinedSchemaAcceptorTest {

    @Mock
    private JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    @InjectMocks
    private CombinedSchemaAcceptor combinedSchemaAcceptor;

    @Test
    public void shouldAcceptCombinedSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final CombinedSchema combinedSchema = mock(CombinedSchema.class);
        final ObjectSchema childSchema_1 = mock(ObjectSchema.class);
        final ObjectSchema childSchema_2 = mock(ObjectSchema.class);

        final List<Schema> propertySchemas = asList(childSchema_1, childSchema_2);

        when(combinedSchema.getSubschemas()).thenReturn(propertySchemas);

        combinedSchemaAcceptor.accept(fieldName, visitor, combinedSchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaAcceptorFactory);

        inOrder.verify(visitor).enter(fieldName, combinedSchema);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema("fieldName", visitor, childSchema_1);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema("fieldName", visitor, childSchema_2);
        inOrder.verify(visitor).leave(combinedSchema);
    }
}
