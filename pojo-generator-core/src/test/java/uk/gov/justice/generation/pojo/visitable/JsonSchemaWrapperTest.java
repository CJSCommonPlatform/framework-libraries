package uk.gov.justice.generation.pojo.visitable;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.visitable.acceptor.JsonSchemaAcceptor;
import uk.gov.justice.generation.pojo.visitable.acceptor.JsonSchemaAcceptorFactory;
import uk.gov.justice.generation.pojo.visitor.Visitor;

import java.util.Map;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JsonSchemaWrapperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Visitor visitor;

    @Mock
    private JsonSchemaAcceptorFactory jsonSchemaAcceptorFactory;

    @Test
    public void shouldVisitObjectSchema() {
        final String fieldName = "myFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().id(fieldName).build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ObjectSchema.class)).thenReturn(true);
        when(acceptorMap.get(ObjectSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, objectSchema);
    }

    @Test
    public void shouldVisitStringSchema() {

        final String fieldName = "myFieldName";
        final StringSchema stringSchema = StringSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(StringSchema.class)).thenReturn(true);
        when(acceptorMap.get(StringSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(stringSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, stringSchema);
    }

    @Test
    public void shouldVisitBooleanSchema() {
        final String fieldName = "myFieldName";
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(BooleanSchema.class)).thenReturn(true);
        when(acceptorMap.get(BooleanSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(booleanSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, booleanSchema);
    }

    @Test
    public void shouldVisitNumberSchema() {
        final String fieldName = "myFieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(NumberSchema.class)).thenReturn(true);
        when(acceptorMap.get(NumberSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(numberSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, numberSchema);
    }

    @Test
    public void shouldVisitEnumSchema() {
        final String fieldName = "myFieldName";
        final EnumSchema enumSchema = EnumSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(EnumSchema.class)).thenReturn(true);
        when(acceptorMap.get(EnumSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(enumSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, enumSchema);
    }

    @Test
    public void shouldReferenceSchema() {
        final String fieldName = "fieldName";
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ReferenceSchema.class)).thenReturn(true);
        when(acceptorMap.get(ReferenceSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(referenceSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, referenceSchema);
    }

    @Test
    public void shouldVisitArraySchema() {
        final String fieldName = "fieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ArraySchema.class)).thenReturn(true);
        when(acceptorMap.get(ArraySchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(arraySchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, arraySchema);
    }

    @Test
    public void shouldVisitCombinedSchema() {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();

        final Map acceptorMap = mock(Map.class);
        final JsonSchemaAcceptor jsonSchemaAcceptor = mock(JsonSchemaAcceptor.class);

        when(jsonSchemaAcceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(CombinedSchema.class)).thenReturn(true);
        when(acceptorMap.get(CombinedSchema.class)).thenReturn(jsonSchemaAcceptor);

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(combinedSchema, jsonSchemaAcceptorFactory);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaAcceptor).accept(fieldName, visitor, combinedSchema);
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessed() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);

        new JsonSchemaWrapper(dummySchema, jsonSchemaAcceptorFactory).accept(fieldName, mock(Visitor.class));
    }

    private class DummySchema extends Schema {

        private final String id;

        DummySchema(final Builder<?> builder, final String id) {
            super(builder);
            this.id = id;
        }

        @Override
        public void validate(final Object o) {
            //do nothing
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
