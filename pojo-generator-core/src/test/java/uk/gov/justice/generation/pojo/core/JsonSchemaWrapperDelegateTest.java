package uk.gov.justice.generation.pojo.core;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JsonSchemaWrapperDelegateTest {

    @Mock
    private CombinedSchemaPropertyExtractor combinedSchemaPropertyExtractor;

    @Mock
    private JsonSchemaWrapperFactory jsonSchemaWrapperFactory;

    @InjectMocks
    private JsonSchemaWrapperDelegate jsonSchemaWrapperDelegate;

    @Test
    public void shouldAcceptACombinedSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final CombinedSchema combinedSchema = mock(CombinedSchema.class);
        final ObjectSchema childSchema_1 = mock(ObjectSchema.class);
        final ObjectSchema childSchema_2 = mock(ObjectSchema.class);

        final JsonSchemaWrapper jsonSchemaWrapper_1 = mock(JsonSchemaWrapper.class);
        final JsonSchemaWrapper jsonSchemaWrapper_2 = mock(JsonSchemaWrapper.class);

        final Map<String, Schema> propertySchemas = new HashMap<>();
        propertySchemas.put("childSchema_1", childSchema_1);
        propertySchemas.put("childSchema_2", childSchema_2);

        when(combinedSchemaPropertyExtractor.getAllPropertiesFrom(combinedSchema)).thenReturn(propertySchemas);
        when(jsonSchemaWrapperFactory.create(childSchema_1)).thenReturn(jsonSchemaWrapper_1);
        when(jsonSchemaWrapperFactory.create(childSchema_2)).thenReturn(jsonSchemaWrapper_2);

        jsonSchemaWrapperDelegate.acceptCombinedSchema(fieldName, visitor, combinedSchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaWrapper_1, jsonSchemaWrapper_2);

        inOrder.verify(visitor).enter(fieldName, combinedSchema);
        inOrder.verify(jsonSchemaWrapper_1).accept("childSchema_1", visitor);
        inOrder.verify(jsonSchemaWrapper_2).accept("childSchema_2", visitor);
        inOrder.verify(visitor).leave(combinedSchema);
    }

    @Test
    public void shouldAcceptAReferenceSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ReferenceSchema referenceSchema = mock(ReferenceSchema.class);
        final Schema referredSchema = mock(Schema.class);
        final JsonSchemaWrapper jsonSchemaWrapper = mock(JsonSchemaWrapper.class);

        when(referenceSchema.getReferredSchema()).thenReturn(referredSchema);
        when(jsonSchemaWrapperFactory.create(referredSchema)).thenReturn(jsonSchemaWrapper);

        jsonSchemaWrapperDelegate.acceptReferenceSchema(fieldName, visitor, referenceSchema);

        verify(jsonSchemaWrapper).accept(fieldName, visitor);
    }

    @Test
    public void shouldAcceptAnObjectSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ObjectSchema objectSchema = mock(ObjectSchema.class);
        final ObjectSchema childSchema_1 = mock(ObjectSchema.class);
        final ObjectSchema childSchema_2 = mock(ObjectSchema.class);

        final JsonSchemaWrapper jsonSchemaWrapper_1 = mock(JsonSchemaWrapper.class);
        final JsonSchemaWrapper jsonSchemaWrapper_2 = mock(JsonSchemaWrapper.class);

        final Map<String, Schema> propertySchemas = new HashMap<>();
        propertySchemas.put("childSchema_1", childSchema_1);
        propertySchemas.put("childSchema_2", childSchema_2);

        when(objectSchema.getPropertySchemas()).thenReturn(propertySchemas);
        when(jsonSchemaWrapperFactory.create(childSchema_1)).thenReturn(jsonSchemaWrapper_1);
        when(jsonSchemaWrapperFactory.create(childSchema_2)).thenReturn(jsonSchemaWrapper_2);

        jsonSchemaWrapperDelegate.acceptObjectSchema(fieldName, visitor, objectSchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaWrapper_1, jsonSchemaWrapper_2);

        inOrder.verify(visitor).enter(fieldName, objectSchema);
        inOrder.verify(jsonSchemaWrapper_1).accept("childSchema_1", visitor);
        inOrder.verify(jsonSchemaWrapper_2).accept("childSchema_2", visitor);
        inOrder.verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldAcceptAnArraySchemaWithAnAllItemSchema() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ArraySchema arraySchema = mock(ArraySchema.class);
        final Schema allItemSchema = mock(Schema.class);
        final JsonSchemaWrapper jsonSchemaWrapper = mock(JsonSchemaWrapper.class);

        when(arraySchema.getAllItemSchema()).thenReturn(allItemSchema);
        when(jsonSchemaWrapperFactory.create(allItemSchema)).thenReturn(jsonSchemaWrapper);

        jsonSchemaWrapperDelegate.acceptArraySchema(fieldName, visitor, arraySchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaWrapper);

        inOrder.verify(visitor).enter(fieldName, arraySchema);
        inOrder.verify(jsonSchemaWrapper).accept(fieldName, visitor);
        inOrder.verify(visitor).leave(arraySchema);
    }

    @Test
    public void shouldAcceptAnArraySchemaWithAListOfItemSchemas() throws Exception {

        final String fieldName = "fieldName";
        final Visitor visitor = mock(Visitor.class);
        final ArraySchema arraySchema = mock(ArraySchema.class);
        final Schema itemSchema_1 = mock(Schema.class);
        final Schema itemSchema_2 = mock(Schema.class);
        final JsonSchemaWrapper jsonSchemaWrapper_1 = mock(JsonSchemaWrapper.class);
        final JsonSchemaWrapper jsonSchemaWrapper_2 = mock(JsonSchemaWrapper.class);

        when(arraySchema.getItemSchemas()).thenReturn(asList(itemSchema_1, itemSchema_2));
        when(jsonSchemaWrapperFactory.create(itemSchema_1)).thenReturn(jsonSchemaWrapper_1);
        when(jsonSchemaWrapperFactory.create(itemSchema_2)).thenReturn(jsonSchemaWrapper_2);

        jsonSchemaWrapperDelegate.acceptArraySchema(fieldName, visitor, arraySchema);

        final InOrder inOrder = inOrder(visitor, jsonSchemaWrapper_1, jsonSchemaWrapper_2);

        inOrder.verify(visitor).enter(fieldName, arraySchema);
        inOrder.verify(jsonSchemaWrapper_1).accept(fieldName, visitor);
        inOrder.verify(jsonSchemaWrapper_2).accept(fieldName, visitor);
        inOrder.verify(visitor).leave(arraySchema);
    }
}
