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
        final String fieldName = "myFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().id(fieldName).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
    }

    @Test
    public void shouldVisitObjectAndStringSchema() {

        final StringSchema stringSchema = StringSchema.builder().build();
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, stringSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, stringSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndBooleanSchema() {
        final BooleanSchema booleanSchema = BooleanSchema.INSTANCE;
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, booleanSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, booleanSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndNumberSchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, numberSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, numberSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndEnumSchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final EnumSchema enumSchema = EnumSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, enumSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, enumSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndNullSchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final NullSchema nullSchema = NullSchema.INSTANCE;
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, nullSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, nullSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndEmptySchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final EmptySchema emptySchema = EmptySchema.INSTANCE;
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, emptySchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, emptySchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndReferenceSchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final StringSchema referredSchema = StringSchema.builder().build();
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();
        referenceSchema.setReferredSchema(referredSchema);
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, referenceSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, referredSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndArraySchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, arraySchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, arraySchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldVisitObjectAndCombinedSchema() {
        final String fieldName = "fieldName";
        final String childFieldName = "childFieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema(childFieldName, combinedSchema).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).enter(fieldName, objectSchema);
        verify(visitor).visit(childFieldName, combinedSchema);
        verify(visitor).leave(objectSchema);
    }

    @Test
    public void shouldThrowExceptionIfUnknownSchemaProcessed() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);

        new JsonSchemaWrapper(dummySchema).accept(fieldName, mock(Visitor.class));
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
