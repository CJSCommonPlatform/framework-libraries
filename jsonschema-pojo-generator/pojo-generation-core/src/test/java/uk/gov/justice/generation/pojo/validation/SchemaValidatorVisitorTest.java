package uk.gov.justice.generation.pojo.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.lang.reflect.Field;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NullSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.StringSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
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
    public void shouldValidateAnObjectSchema() throws Exception {

        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        schemaValidatorVisitor.enter(AN_EMPTY_STRING, objectSchema);

        verify(validator).validate(objectSchema);
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
        schemaValidatorVisitor.leave(mock(ObjectSchema.class));
        schemaValidatorVisitor.leave(mock(ArraySchema.class));
        schemaValidatorVisitor.enter(AN_EMPTY_STRING, mock(CombinedSchema.class));
        schemaValidatorVisitor.leave(mock(CombinedSchema.class));
        schemaValidatorVisitor.enter(AN_EMPTY_STRING, mock(ReferenceSchema.class));
        schemaValidatorVisitor.leave(mock(ReferenceSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(NumberSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(StringSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(BooleanSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(NullSchema.class));
        schemaValidatorVisitor.visit(AN_EMPTY_STRING, mock(EmptySchema.class));

        verifyNoInteractions(validator);
    }
}
