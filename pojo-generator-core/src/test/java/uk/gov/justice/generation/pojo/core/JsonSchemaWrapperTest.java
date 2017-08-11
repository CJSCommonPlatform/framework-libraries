package uk.gov.justice.generation.pojo.core;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
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

    @Test
    public void shouldWrapObjectSchema() {
        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndStringSchema() {
        final StringSchema stringSchema = StringSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", stringSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(stringSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndBooleanSchema() {
        final BooleanSchema booleanSchema = BooleanSchema.INSTANCE;
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", booleanSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(booleanSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndNumberSchema() {
        final NumberSchema numberSchema = NumberSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", numberSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(numberSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndEnumSchema() {
        final EnumSchema enumSchema = EnumSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", enumSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(enumSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndNullSchema() {
        final NullSchema nullSchema = NullSchema.INSTANCE;
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", nullSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(nullSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndEmptySchema() {
        final EmptySchema emptySchema = EmptySchema.INSTANCE;
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", emptySchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(emptySchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndReferenceSchema() {
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", referenceSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(referenceSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndArraySchema() {
        final ArraySchema arraySchema = ArraySchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", arraySchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(arraySchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndCombinedSchema() {
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("field", combinedSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(visitor);

        verify(visitor).visitEnter(objectSchema);
        verify(visitor).visit(combinedSchema);
        verify(visitor).visitLeave(objectSchema);
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessed() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder builder = mock(Schema.Builder.class);
        final DummySchema dummySchema = new DummySchema(builder);

        new JsonSchemaWrapper(dummySchema).accept(mock(Visitor.class));
    }

    private class DummySchema extends Schema {

        DummySchema(final Builder<?> builder) {
            super(builder);
        }

        @Override
        public void validate(final Object o) {
            //do nothing
        }
    }
}
