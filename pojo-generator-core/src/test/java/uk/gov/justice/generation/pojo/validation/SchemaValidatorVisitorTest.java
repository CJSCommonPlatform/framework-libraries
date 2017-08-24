package uk.gov.justice.generation.pojo.validation;

import static org.everit.json.schema.CombinedSchema.ONE_CRITERION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.StringSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SchemaValidatorVisitorTest {

    @Mock
    private Validator validator;

    @Mock
    private SchemaValidatorVisitableFactory schemaValidatorVisitableFactory;

    @InjectMocks
    private SchemaValidatorVisitor schemaValidatorVisitor;

    @Test
    public void shouldVisitAllChildSchemasOfAnObjectSchema() throws Exception {

        final StringSchema stringSchema = StringSchema.builder().build();
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema("stringSchema", stringSchema)
                .addPropertySchema("numberSchema", numberSchema)
                .build();

        final SchemaValidatorVisitable schemaValidatorVisitable_1 = mock(SchemaValidatorVisitable.class);
        final SchemaValidatorVisitable schemaValidatorVisitable_2 = mock(SchemaValidatorVisitable.class);

        when(schemaValidatorVisitableFactory.create(stringSchema)).thenReturn(schemaValidatorVisitable_1);
        when(schemaValidatorVisitableFactory.create(numberSchema)).thenReturn(schemaValidatorVisitable_2);

        schemaValidatorVisitor.visit(objectSchema);

        verify(schemaValidatorVisitable_1).accept(schemaValidatorVisitor);
        verify(schemaValidatorVisitable_2).accept(schemaValidatorVisitor);
    }

    @Test
    public void shouldValidateAnEnumSchema() throws Exception {

        final EnumSchema enumSchema = EnumSchema.builder().build();

        schemaValidatorVisitor.visit(enumSchema);

        verify(validator).validate(enumSchema);
    }

    @Test
    public void shouldVisitAllChildSchemasOfAnArraySchema() throws Exception {

        final StringSchema stringSchema_1 = mock(StringSchema.class);
        final StringSchema stringSchema_2 = mock(StringSchema.class);

        final ArraySchema arraySchema = ArraySchema.builder()
                .addItemSchema(stringSchema_1)
                .addItemSchema(stringSchema_2)
                .build();

        final SchemaValidatorVisitable schemaValidatorVisitable_1 = mock(SchemaValidatorVisitable.class);
        final SchemaValidatorVisitable schemaValidatorVisitable_2 = mock(SchemaValidatorVisitable.class);

        when(schemaValidatorVisitableFactory.create(stringSchema_1)).thenReturn(schemaValidatorVisitable_1);
        when(schemaValidatorVisitableFactory.create(stringSchema_2)).thenReturn(schemaValidatorVisitable_2);

        schemaValidatorVisitor.visit(arraySchema);

        verify(schemaValidatorVisitable_1).accept(schemaValidatorVisitor);
        verify(schemaValidatorVisitable_2).accept(schemaValidatorVisitor);
    }

    @Test
    public void shouldVisitTheAllItemSchemaOfAnArraySchema() throws Exception {

        final ObjectSchema allItemSchema = ObjectSchema.builder().build();

        final ArraySchema arraySchema = ArraySchema.builder()
                .allItemSchema(allItemSchema)
                .build();

        final SchemaValidatorVisitable schemaValidatorVisitable = mock(SchemaValidatorVisitable.class);

        when(schemaValidatorVisitableFactory.create(allItemSchema)).thenReturn(schemaValidatorVisitable);

        schemaValidatorVisitor.visit(arraySchema);

        verify(schemaValidatorVisitable).accept(schemaValidatorVisitor);
    }

    @Test
    public void shouldVisitTheReferredSchemaOfAReferenceSchema() throws Exception {
        final ObjectSchema referredObjectSchema = ObjectSchema.builder().build();

        final ReferenceSchema referenceSchema = ReferenceSchema.builder()
                .build();

        referenceSchema.setReferredSchema(referredObjectSchema);

        final SchemaValidatorVisitable schemaValidatorVisitable = mock(SchemaValidatorVisitable.class);

        when(schemaValidatorVisitableFactory.create(referredObjectSchema)).thenReturn(schemaValidatorVisitable);

        schemaValidatorVisitor.visit(referenceSchema);

        verify(schemaValidatorVisitable).accept(schemaValidatorVisitor);
    }

    @Test
    public void shouldVisitAllChildSchemasOfACombinedSchema() throws Exception {

        final StringSchema stringSchema_1 = mock(StringSchema.class);
        final StringSchema stringSchema_2 = mock(StringSchema.class);

        final CombinedSchema arraySchema = CombinedSchema.builder()
                .subschema(stringSchema_1)
                .subschema(stringSchema_2)
                .criterion(ONE_CRITERION)
                .build();

        final SchemaValidatorVisitable schemaValidatorVisitable_1 = mock(SchemaValidatorVisitable.class);
        final SchemaValidatorVisitable schemaValidatorVisitable_2 = mock(SchemaValidatorVisitable.class);

        when(schemaValidatorVisitableFactory.create(stringSchema_1)).thenReturn(schemaValidatorVisitable_1);
        when(schemaValidatorVisitableFactory.create(stringSchema_2)).thenReturn(schemaValidatorVisitable_2);

        schemaValidatorVisitor.visit(arraySchema);

        verify(schemaValidatorVisitable_1).accept(schemaValidatorVisitor);
        verify(schemaValidatorVisitable_2).accept(schemaValidatorVisitor);
    }

    @Test
    public void shouldSuccessfullyCallTheDefaultConstructor() throws Exception {

        final SchemaValidatorVisitor schemaValidatorVisitor = new SchemaValidatorVisitor();

        assertThat(schemaValidatorVisitor, is(notNullValue()));

        final Field[] declaredFields = schemaValidatorVisitor.getClass().getDeclaredFields();

        assertThat(declaredFields[0].getName(), is("validator"));
        declaredFields[0].setAccessible(true);
        assertThat(declaredFields[0].get(schemaValidatorVisitor), is(notNullValue()));

        assertThat(declaredFields[1].getName(), is("schemaValidatorVisitableFactory"));
        declaredFields[1].setAccessible(true);
        assertThat(declaredFields[1].get(schemaValidatorVisitor), is(notNullValue()));
    }
}
