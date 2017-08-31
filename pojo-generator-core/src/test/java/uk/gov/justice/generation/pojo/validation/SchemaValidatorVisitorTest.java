package uk.gov.justice.generation.pojo.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.reflect.Field;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SchemaValidatorVisitorTest {

    private static final String AN_EMPTY_STRING = "";
    
    @Mock
    private Validator validator;

    @InjectMocks
    private SchemaValidatorVisitor schemaValidatorVisitor;

    @Test
    public void shouldValidateAnEnumSchema() throws Exception {

        final EnumSchema enumSchema = EnumSchema.builder().build();

        schemaValidatorVisitor.visit(AN_EMPTY_STRING, enumSchema);

        verify(validator).validate(enumSchema);
    }

    @Test
    public void shouldValidateAnArraySchema() throws Exception {

        final ArraySchema arraySchema = ArraySchema.builder().build();

        schemaValidatorVisitor.enter(AN_EMPTY_STRING, arraySchema);

        verify(validator).validate(arraySchema);
    }

    @Test
    public void shouldSuccessfullyCallTheDefaultConstructor() throws Exception {

        final SchemaValidatorVisitor schemaValidatorVisitor = new SchemaValidatorVisitor(validator);

        assertThat(schemaValidatorVisitor, is(notNullValue()));

        final Field[] declaredFields = schemaValidatorVisitor.getClass().getDeclaredFields();

        assertThat(declaredFields[0].getName(), is("validator"));
        declaredFields[0].setAccessible(true);
        assertThat(declaredFields[0].get(schemaValidatorVisitor), is(notNullValue()));
    }

    @Test
    public void shouldDoNothing() throws Exception {
        schemaValidatorVisitor.enter(AN_EMPTY_STRING, mock(ObjectSchema.class));
        schemaValidatorVisitor.leave(mock(ObjectSchema.class));
        schemaValidatorVisitor.leave(mock(ArraySchema.class));
        schemaValidatorVisitor.enter(AN_EMPTY_STRING, mock(CombinedSchema.class));
        schemaValidatorVisitor.leave(mock(CombinedSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(NumberSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(StringSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(BooleanSchema.class));

        verifyZeroInteractions(validator);
    }
}
