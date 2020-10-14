package uk.gov.justice.generation.pojo.visitor;

import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.everit.json.schema.FormatValidator.forFormat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.COMBINED;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ENUM;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.NUMBER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.core.UnsupportedSchemaException;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;

import java.io.Reader;
import java.util.Optional;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EmptySchema;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDefinitionFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ReferenceValueParser referenceValueParser;

    @Mock
    private StringFormatValueParser stringFormatValueParser;

    @InjectMocks
    private DefaultDefinitionFactory defaultDefinitionFactory;

    @Test
    public void shouldConstructRootClassDefinitionFromAnObjectSchema() throws Exception {
        final String fieldName = "fieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        final Definition definition = defaultDefinitionFactory.constructRootDefinitionFor(fieldName, objectSchema);

        assertThat(definition, is(instanceOf(ClassDefinition.class)));
        assertThat(definition.type(), is(CLASS));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(((ClassDefinition) definition).isRoot(), is(true));
    }

    @Test
    public void shouldConstructRootClassDefinitionFromACombinedSchema() throws Exception {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema
                .builder()
                .criterion(ANY_CRITERION)
                .build();

        final Definition definition = defaultDefinitionFactory.constructRootDefinitionFor(fieldName, combinedSchema);

        assertThat(definition, is(instanceOf(ClassDefinition.class)));
        assertThat(definition.type(), is(COMBINED));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(((ClassDefinition) definition).isRoot(), is(true));
    }

    @Test
    public void shouldConstructClassDefinition() throws Exception {
        final String fieldName = "fieldName";
        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, objectSchema);

        assertThat(definition, is(instanceOf(ClassDefinition.class)));
        assertThat(definition.type(), is(CLASS));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructCombinedDefinition() throws Exception {
        final String fieldName = "fieldName";
        final CombinedSchema combinedSchema = CombinedSchema.builder().criterion(ANY_CRITERION).build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, combinedSchema);

        assertThat(definition, is(instanceOf(CombinedDefinition.class)));
        assertThat(definition.type(), is(COMBINED));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForArraySchema() throws Exception {
        final String fieldName = "fieldName";
        final ArraySchema arraySchema = ArraySchema.builder().build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, arraySchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(ARRAY));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructEnumDefinition() throws Exception {
        final String fieldName = "fieldName";
        final String enumValue = "test value";
        final EnumSchema enumSchema = EnumSchema.builder().possibleValue(enumValue).build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, enumSchema);

        assertThat(definition, is(instanceOf(EnumDefinition.class)));
        assertThat(definition.type(), is(ENUM));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(((EnumDefinition) definition).getEnumValues(), hasItem(enumValue));
    }

    @Test
    public void shouldConstructStringDefinitionWithFormat() throws Exception {
        final String fieldName = "fieldName";
        final String format = "date-time";
        final StringSchema stringSchema = StringSchema.builder()
                .formatValidator(forFormat(format))
                .description("UUID")
                .build();

        when(stringFormatValueParser.parseFrom(any(Reader.class), eq(fieldName))).thenReturn(Optional.of(format));

        final StringDefinition definition = (StringDefinition) defaultDefinitionFactory.constructDefinitionFor(fieldName, stringSchema);

        assertThat(definition, is(instanceOf(StringDefinition.class)));
        assertThat(definition.type(), is(STRING));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(definition.getFormat(), is(Optional.of(format)));
    }

    @Test
    public void shouldConstructStringDefinitionWithNoFormat() throws Exception {
        final String fieldName = "fieldName";
        final StringSchema stringSchema = StringSchema.builder()
                .description("UUID")
                .build();

        when(stringFormatValueParser.parseFrom(any(Reader.class), eq(fieldName))).thenReturn(Optional.empty());

        final StringDefinition definition = (StringDefinition) defaultDefinitionFactory.constructDefinitionFor(fieldName, stringSchema);

        assertThat(definition, is(instanceOf(StringDefinition.class)));
        assertThat(definition.type(), is(STRING));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(definition.getFormat(), is(Optional.empty()));
    }

    @Test
    public void shouldConstructFieldDefinitionForBooleanSchema() throws Exception {
        final String fieldName = "fieldName";
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, booleanSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(BOOLEAN));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForNumberSchema() throws Exception {
        final String fieldName = "fieldName";
        final NumberSchema numberSchema = NumberSchema.builder().build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, numberSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(NUMBER));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructFieldDefinitionForIntegerNumberSchema() throws Exception {
        final String fieldName = "fieldName";
        final NumberSchema numberSchema = NumberSchema.builder().requiresInteger(true).build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, numberSchema);

        assertThat(definition, is(instanceOf(FieldDefinition.class)));
        assertThat(definition.type(), is(INTEGER));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    public void shouldConstructReferenceDefinition() throws Exception {
        final String fieldName = "fieldName";
        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", "uuid");
        final ReferenceSchema referenceSchema = ReferenceSchema.builder().refValue(referenceValue.toString()).build();

        when(referenceValueParser.parseFrom(any(Reader.class), eq(fieldName))).thenReturn(referenceValue);

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, referenceSchema);

        assertThat(definition, is(instanceOf(ReferenceDefinition.class)));
        assertThat(definition.type(), is(REFERENCE));
        assertThat(definition.getFieldName(), is(fieldName));
        assertThat(((ReferenceDefinition) definition).getReferenceValue(), is(referenceValue));
    }

    @Test
    public void shouldConstructClassDefinitionForEmptySchema() throws Exception {
        final String fieldName = "fieldName";
        final EmptySchema emptySchema = EmptySchema.builder().build();

        final Definition definition = defaultDefinitionFactory.constructDefinitionFor(fieldName, emptySchema);

        assertThat(definition, is(instanceOf(ClassDefinition.class)));
        assertThat(definition.type(), is(CLASS));
        assertThat(definition.getFieldName(), is(fieldName));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldThrowExceptionIfUnknownSchemaProcessedForConstructDefinition() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder<DummySchema> builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);
        defaultDefinitionFactory.constructDefinitionFor(fieldName, dummySchema);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldThrowExceptionIfUnknownSchemaProcessedForConstructFieldDefinition() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage("Schema of type: DummySchema is not supported.");

        final Schema.Builder<DummySchema> builder = mock(Schema.Builder.class);
        final String fieldName = "myDummy";
        final DummySchema dummySchema = new DummySchema(builder, fieldName);
        defaultDefinitionFactory.constructDefinitionFor("fieldName", dummySchema);
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
