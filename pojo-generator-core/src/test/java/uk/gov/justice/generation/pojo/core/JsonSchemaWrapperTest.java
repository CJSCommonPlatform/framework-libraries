package uk.gov.justice.generation.pojo.core;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    private JsonSchemaWrapperDelegate jsonSchemaWrapperDelegate;

    @Test
    public void shouldVisitObjectSchema() {
        final String fieldName = "myFieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().id(fieldName).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(objectSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaWrapperDelegate).acceptObjectSchema(fieldName, visitor, objectSchema);
    }

    @Test
    public void shouldVisitStringSchema() {

        final String fieldName = "myFieldName";
        final StringSchema stringSchema = StringSchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(stringSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).visit(fieldName, stringSchema);
    }

    @Test
    public void shouldVisitBooleanSchema() {
        final String fieldName = "myFieldName";
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(booleanSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).visit(fieldName, booleanSchema);
    }

    @Test
    public void shouldVisitNumberSchema() {
        final String fieldName = "myFieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(numberSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).visit(fieldName, numberSchema);
    }

    @Test
    public void shouldVisitEnumSchema() {
        final String fieldName = "myFieldName";
        final EnumSchema enumSchema = EnumSchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(enumSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(visitor).visit(fieldName, enumSchema);
    }

    @Test
    public void shouldReferenceSchema() {
        final String fieldName = "fieldName";
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(referenceSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaWrapperDelegate).acceptReferenceSchema(fieldName, visitor, referenceSchema);
    }

    @Test
    public void shouldVisitArraySchema() {
        final String fieldName = "fieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(arraySchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaWrapperDelegate).acceptArraySchema(fieldName, visitor, arraySchema);
    }

    @Test
    public void shouldVisitCombinedSchema() {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();

        final JsonSchemaWrapper jsonSchemaWrapper = new JsonSchemaWrapper(combinedSchema, jsonSchemaWrapperDelegate);
        jsonSchemaWrapper.accept(fieldName, visitor);

        verify(jsonSchemaWrapperDelegate).acceptCombinedSchema(fieldName, visitor, combinedSchema);
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
