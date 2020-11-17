package uk.gov.justice.generation.pojo.visitable;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.visitable.acceptor.Acceptable;
import uk.gov.justice.generation.pojo.visitable.acceptor.AcceptorService;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class VisitableSchemaTest {

    @Mock
    private Visitor visitor;

    @Mock
    private AcceptorService acceptorService;

    @Test
    public void shouldVisitObjectSchema() {
        final String fieldName = "myFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().id(fieldName).build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ObjectSchema.class)).thenReturn(true);
        when(acceptorMap.get(ObjectSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, objectSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, objectSchema, visitor);
    }

    @Test
    public void shouldVisitStringSchema() {

        final String fieldName = "myFieldName";
        final StringSchema stringSchema = StringSchema.builder().build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(StringSchema.class)).thenReturn(true);
        when(acceptorMap.get(StringSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, stringSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, stringSchema, visitor);
    }

    @Test
    public void shouldVisitBooleanSchema() {
        final String fieldName = "myFieldName";
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(BooleanSchema.class)).thenReturn(true);
        when(acceptorMap.get(BooleanSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, booleanSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, booleanSchema, visitor);
    }

    @Test
    public void shouldVisitNumberSchema() {
        final String fieldName = "myFieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(NumberSchema.class)).thenReturn(true);
        when(acceptorMap.get(NumberSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, numberSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, numberSchema, visitor);
    }

    @Test
    public void shouldVisitEnumSchema() {
        final String fieldName = "myFieldName";
        final EnumSchema enumSchema = EnumSchema.builder().build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(EnumSchema.class)).thenReturn(true);
        when(acceptorMap.get(EnumSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, enumSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, enumSchema, visitor);
    }

    @Test
    public void shouldReferenceSchema() {
        final String fieldName = "fieldName";
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ReferenceSchema.class)).thenReturn(true);
        when(acceptorMap.get(ReferenceSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, referenceSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, referenceSchema, visitor);
    }

    @Test
    public void shouldVisitArraySchema() {
        final String fieldName = "fieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(ArraySchema.class)).thenReturn(true);
        when(acceptorMap.get(ArraySchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, arraySchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, arraySchema, visitor);
    }

    @Test
    public void shouldVisitCombinedSchema() {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();

        final Map<Class<? extends Schema>, Acceptable> acceptorMap = mock(Map.class);
        final Acceptable acceptable = mock(Acceptable.class);

        when(acceptorService.acceptorMap()).thenReturn(acceptorMap);
        when(acceptorMap.containsKey(CombinedSchema.class)).thenReturn(true);
        when(acceptorMap.get(CombinedSchema.class)).thenReturn(acceptable);

        final VisitableSchema visitableSchema = new VisitableSchema(fieldName, combinedSchema, acceptorService);
        visitableSchema.accept(visitor);

        verify(acceptable).accept(fieldName, combinedSchema, visitor);
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessed() throws Exception {

        final Schema.Builder<? extends Schema> builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);

        final UnsupportedSchemaException unsupportedSchemaException = assertThrows(UnsupportedSchemaException.class, () ->
                new VisitableSchema(fieldName, dummySchema, acceptorService).accept(mock(Visitor.class))
        );

        assertThat(unsupportedSchemaException.getMessage(), is("Schema of type: DummySchema is not supported."));
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
