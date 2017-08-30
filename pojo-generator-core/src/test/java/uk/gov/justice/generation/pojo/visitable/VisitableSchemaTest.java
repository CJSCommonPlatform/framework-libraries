package uk.gov.justice.generation.pojo.visitable;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.visitable.acceptor.Acceptable;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorFactory;
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
public class VisitableSchemaTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Visitor visitor;

    @Mock
    private AcceptorFactory acceptorFactory;

    @Test
    public void shouldVisitObjectSchema() {
        final String fieldName = "myFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().id(fieldName).build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ObjectSchema.class)).thenReturn(true);
        when(acceptorMap.get(ObjectSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(objectSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, objectSchema);
    }

    @Test
    public void shouldVisitStringSchema() {

        final String fieldName = "myFieldName";
        final StringSchema stringSchema = StringSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(StringSchema.class)).thenReturn(true);
        when(acceptorMap.get(StringSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(stringSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, stringSchema);
    }

    @Test
    public void shouldVisitBooleanSchema() {
        final String fieldName = "myFieldName";
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(BooleanSchema.class)).thenReturn(true);
        when(acceptorMap.get(BooleanSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(booleanSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, booleanSchema);
    }

    @Test
    public void shouldVisitNumberSchema() {
        final String fieldName = "myFieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(NumberSchema.class)).thenReturn(true);
        when(acceptorMap.get(NumberSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(numberSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, numberSchema);
    }

    @Test
    public void shouldVisitEnumSchema() {
        final String fieldName = "myFieldName";
        final EnumSchema enumSchema = EnumSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(EnumSchema.class)).thenReturn(true);
        when(acceptorMap.get(EnumSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(enumSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, enumSchema);
    }

    @Test
    public void shouldReferenceSchema() {
        final String fieldName = "fieldName";
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ReferenceSchema.class)).thenReturn(true);
        when(acceptorMap.get(ReferenceSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(referenceSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, referenceSchema);
    }

    @Test
    public void shouldVisitArraySchema() {
        final String fieldName = "fieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ArraySchema.class)).thenReturn(true);
        when(acceptorMap.get(ArraySchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(arraySchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, arraySchema);
    }

    @Test
    public void shouldVisitCombinedSchema() {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();

        final Map acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorFactory.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(CombinedSchema.class)).thenReturn(true);
        when(acceptorMap.get(CombinedSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(combinedSchema, acceptorFactory);
        visitableSchema.accept(fieldName, visitor);

        verify(acceptable).accept(fieldName, visitor, combinedSchema);
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessed() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);

        new VisitableSchema(dummySchema, acceptorFactory).accept(fieldName, mock(Visitor.class));
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
