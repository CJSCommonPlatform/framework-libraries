package uk.gov.justice.generation.pojo.visitable.acceptor;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.HashMap;
import java.util.Map;

import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectSchemaAcceptorTest {

    @Mock
    private JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    @InjectMocks
    private ObjectSchemaAcceptor objectSchemaAcceptor;

    @Test
    public void shouldAcceptObjectSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ObjectSchema objectSchema = mock(ObjectSchema.class);
        final ObjectSchema childSchema_1 = mock(ObjectSchema.class);
        final ObjectSchema childSchema_2 = mock(ObjectSchema.class);

        final Map<String, Schema> propertySchemas = new HashMap<>();
        propertySchemas.put("childSchema_1", childSchema_1);
        propertySchemas.put("childSchema_2", childSchema_2);

        when(objectSchema.getPropertySchemas()).thenReturn(propertySchemas);

        objectSchemaAcceptor.accept(fieldName, visitor, objectSchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaAcceptorFactory);

        inOrder.verify(visitor).enter(fieldName, objectSchema);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema("childSchema_1", visitor, childSchema_1);
        inOrder.verify(jsonSchemaAcceptorFactory).visitSchema("childSchema_2", visitor, childSchema_2);
        inOrder.verify(visitor).leave(objectSchema);
    }
}
