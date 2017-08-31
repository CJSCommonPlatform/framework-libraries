package uk.gov.justice.generation.pojo.visitor;

import static org.everit.json.schema.CombinedSchema.ALL_CRITERION;
import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.List;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.CombinedSchema;
import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefinitionBuilderVisitorTest {

    private static final String PACKAGE_NAME = "org.bloggs.fred";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private DefaultDefinitionFactory definitionFactory;

    @Test
    public void shouldGenerateMultiLevelClassDefinitions() throws Exception {
        final String outerFieldName = "outerClass";
        final String innerFieldName = "innerClass";
        final String innerStringField = "innerProperty";
        final String outerStringFieldName = "outerProperty";


        final StringSchema innerStringSchema = StringSchema.builder().build();
        final StringSchema outerStringSchema = StringSchema.builder().build();

        final ObjectSchema innerObjectSchema = ObjectSchema.builder()
                .addPropertySchema(innerStringField, innerStringSchema)
                .build();

        final ObjectSchema outerObjectSchema = ObjectSchema.builder()
                .addPropertySchema(innerFieldName, innerObjectSchema)
                .addPropertySchema(outerStringFieldName, outerStringSchema)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final ClassDefinition innerDefinition = mock(ClassDefinition.class);
        final FieldDefinition innerStringDefinition = mock(FieldDefinition.class);
        final FieldDefinition outerStringDefinition = mock(FieldDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(outerFieldName, PACKAGE_NAME, outerObjectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(innerFieldName, PACKAGE_NAME, innerObjectSchema)).thenReturn(innerDefinition);
        when(definitionFactory.constructFieldDefinition(outerStringFieldName, outerStringSchema)).thenReturn(outerStringDefinition);
        when(definitionFactory.constructFieldDefinition(innerStringField, innerStringSchema)).thenReturn(innerStringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, outerObjectSchema);
        definitionBuilderVisitor.enter(innerFieldName, innerObjectSchema);
        definitionBuilderVisitor.visit(innerStringField, innerStringSchema);
        definitionBuilderVisitor.leave(innerObjectSchema);
        definitionBuilderVisitor.visit(outerStringFieldName, outerStringSchema);
        definitionBuilderVisitor.leave(outerObjectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(innerDefinition));
        assertThat(definitions.get(1), is(outerDefinition));

        verify(outerDefinition).addFieldDefinition(outerStringDefinition);
        verify(outerDefinition).addFieldDefinition(innerDefinition);
        verify(innerDefinition).addFieldDefinition(innerStringDefinition);
    }

    @Test
    public void shouldGenerateClassDefinitionWithBooleanSchemaProperty() throws Exception {
        final String outerFieldName = "outerClass";
        final String booleanFieldName = "booleanProperty";

        final BooleanSchema booleanSchema = BooleanSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(booleanFieldName, booleanSchema)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final FieldDefinition booleanDefinition = mock(FieldDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(outerFieldName, PACKAGE_NAME, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructFieldDefinition(booleanFieldName, booleanSchema)).thenReturn(booleanDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, objectSchema);
        definitionBuilderVisitor.visit(booleanFieldName, booleanSchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        assertThat(definitions.get(0), is(outerDefinition));

        verify(outerDefinition).addFieldDefinition(booleanDefinition);
    }

    @Test
    public void shouldGenerateClassDefinitionWithEnumSchemaProperty() throws Exception {
        final String outerFieldName = "outerClass";
        final String enumFieldName = "enumProperty";

        final EnumSchema enumSchema = EnumSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(enumFieldName, enumSchema)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final EnumDefinition enumDefinition = mock(EnumDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(outerFieldName, PACKAGE_NAME, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(enumFieldName, PACKAGE_NAME, enumSchema)).thenReturn(enumDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, objectSchema);
        definitionBuilderVisitor.visit(enumFieldName, enumSchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(enumDefinition));
        assertThat(definitions.get(1), is(outerDefinition));
    }

    @Test
    public void shouldGenerateClassDefinitionWithNumberSchemaProperty() throws Exception {
        final String outerFieldName = "outerClass";
        final String numberFieldName = "numberProperty";
        final String integerFieldName = "integerProperty";

        final NumberSchema numberProperty = NumberSchema.builder().build();
        final NumberSchema integerProperty = NumberSchema.builder().requiresInteger(true).id(integerFieldName).build();
        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(numberFieldName, numberProperty)
                .addPropertySchema(integerFieldName, integerProperty)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final FieldDefinition numberDefinition = mock(FieldDefinition.class);
        final FieldDefinition integerDefinition = mock(FieldDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(outerFieldName, PACKAGE_NAME, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructFieldDefinition(numberFieldName, numberProperty)).thenReturn(numberDefinition);
        when(definitionFactory.constructFieldDefinition(integerFieldName, integerProperty)).thenReturn(integerDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, objectSchema);
        definitionBuilderVisitor.visit(numberFieldName, numberProperty);
        definitionBuilderVisitor.visit(integerFieldName, integerProperty);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        assertThat(definitions.get(0), is(outerDefinition));

        verify(outerDefinition).addFieldDefinition(numberDefinition);
        verify(outerDefinition).addFieldDefinition(integerDefinition);
    }

    @Test
    public void shouldGenerateClassDefinitionWithArraySchemaProperty() throws Exception {
        final String outerFieldName = "outerClass";
        final String arrayFieldName = "arrayProperty";
        final String arrayObjectFieldName = "arrayObject";

        final ArraySchema arraySchema = ArraySchema.builder().build();

        final ObjectSchema arrayObjectSchema = ObjectSchema.builder()
                .build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(arrayFieldName, arraySchema)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final FieldDefinition arrayDefinition = mock(FieldDefinition.class);
        final ClassDefinition arrayObjectDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(outerFieldName, PACKAGE_NAME, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(arrayFieldName, PACKAGE_NAME, arraySchema)).thenReturn(arrayDefinition);
        when(definitionFactory.constructDefinitionFor(arrayObjectFieldName, PACKAGE_NAME, arrayObjectSchema)).thenReturn(arrayObjectDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, objectSchema);
        definitionBuilderVisitor.enter(arrayFieldName, arraySchema);
        definitionBuilderVisitor.enter(arrayObjectFieldName, arrayObjectSchema);
        definitionBuilderVisitor.leave(arrayObjectSchema);
        definitionBuilderVisitor.leave(arraySchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(arrayObjectDefinition));
        assertThat(definitions.get(1), is(outerDefinition));

        verify(outerDefinition).addFieldDefinition(arrayDefinition);
    }

    @Test
    public void shouldGenerateClassDefinitionWithStringSchemaProperty() throws Exception {
        final String outerFieldName = "outerClass";
        final String stringFieldName = "stringProperty";

        final StringSchema stringSchema = StringSchema.builder().build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(stringFieldName, stringSchema)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final FieldDefinition stringDefinition = mock(FieldDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(outerFieldName, PACKAGE_NAME, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructFieldDefinition(stringFieldName, stringSchema)).thenReturn(stringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, objectSchema);
        definitionBuilderVisitor.visit(stringFieldName, stringSchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        assertThat(definitions.get(0), is(outerDefinition));

        verify(outerDefinition).addFieldDefinition(stringDefinition);
    }

    @Test
    public void shouldGenerateSingleCombinedDefinitionFromCombinedSchemaWithObjectSchema() throws Exception {
        final String combinedFieldName = "combinedClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .build();

        final CombinedSchema combinedSchema = CombinedSchema.builder()
                .subschema(objectSchema)
                .criterion(ALL_CRITERION)
                .build();

        final CombinedDefinition combinedDefinition = mock(CombinedDefinition.class);
        final ClassDefinition objectDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(combinedFieldName, PACKAGE_NAME, combinedSchema)).thenReturn(combinedDefinition);
        when(definitionFactory.constructDefinitionFor(combinedFieldName, PACKAGE_NAME, objectSchema)).thenReturn(objectDefinition);

        definitionBuilderVisitor.enter(combinedFieldName, combinedSchema);
        definitionBuilderVisitor.enter(combinedFieldName, objectSchema);
        definitionBuilderVisitor.leave(objectSchema);
        definitionBuilderVisitor.leave(combinedSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        assertThat(definitions.get(0), is(combinedDefinition));

        verify(combinedDefinition).addFieldDefinition(objectDefinition);
    }

    @Test
    public void shouldGenerateSingleCombinedDefinitionWithObjectPropertyWithSingleProperty() throws Exception {
        final String combinedFieldName = "combinedClass";
        final String innerFieldName = "innerField";

        final StringSchema stringSchema = StringSchema.builder()
                .build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(innerFieldName, stringSchema)
                .build();

        final CombinedSchema combinedSchema = CombinedSchema.builder()
                .subschema(objectSchema)
                .criterion(ALL_CRITERION)
                .build();

        final CombinedDefinition combinedDefinition = mock(CombinedDefinition.class);
        final ClassDefinition objectDefinition = mock(ClassDefinition.class);
        final FieldDefinition stringDefinition = mock(FieldDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(combinedFieldName, PACKAGE_NAME, combinedSchema)).thenReturn(combinedDefinition);
        when(definitionFactory.constructDefinitionFor(combinedFieldName, PACKAGE_NAME, objectSchema)).thenReturn(objectDefinition);
        when(definitionFactory.constructFieldDefinition(innerFieldName, stringSchema)).thenReturn(stringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter("combinedClass", combinedSchema);
        definitionBuilderVisitor.enter("combinedClass", objectSchema);
        definitionBuilderVisitor.visit(innerFieldName, stringSchema);
        definitionBuilderVisitor.leave(objectSchema);
        definitionBuilderVisitor.leave(combinedSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        assertThat(definitions.get(0), is(combinedDefinition));

        verify(combinedDefinition).addFieldDefinition(objectDefinition);
        verify(objectDefinition).addFieldDefinition(stringDefinition);
    }

    @Test
    public void shouldGenerateCombinedDefinitionWithCombinedProperty() throws Exception {
        final String combinedFieldName = "combinedClass";
        final String innerObjectFieldName = "innerClass";
        final String innerFieldName = "innerField";

        final StringSchema stringSchema = StringSchema.builder()
                .build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(innerFieldName, stringSchema)
                .build();

        final CombinedSchema innerCombinedSchema = CombinedSchema.builder()
                .subschema(objectSchema)
                .criterion(ANY_CRITERION)
                .build();

        final CombinedSchema combinedSchema = CombinedSchema.builder()
                .subschema(innerCombinedSchema)
                .criterion(ALL_CRITERION)
                .build();

        final CombinedDefinition outerCombinedDefinition = mock(CombinedDefinition.class);
        final CombinedDefinition innerCombinedDefinition = mock(CombinedDefinition.class);
        final ClassDefinition objectDefinition = mock(ClassDefinition.class);
        final FieldDefinition stringDefinition = mock(FieldDefinition.class);

        when(definitionFactory.constructDefinitionWithEventFor(combinedFieldName, PACKAGE_NAME, combinedSchema)).thenReturn(outerCombinedDefinition);
        when(definitionFactory.constructDefinitionFor(combinedFieldName, PACKAGE_NAME, innerCombinedSchema)).thenReturn(innerCombinedDefinition);
        when(definitionFactory.constructDefinitionFor(innerObjectFieldName, PACKAGE_NAME, objectSchema)).thenReturn(objectDefinition);
        when(definitionFactory.constructFieldDefinition(innerFieldName, stringSchema)).thenReturn(stringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(PACKAGE_NAME, definitionFactory);

        definitionBuilderVisitor.enter(combinedFieldName, combinedSchema);
        definitionBuilderVisitor.enter(combinedFieldName, innerCombinedSchema);
        definitionBuilderVisitor.enter(innerObjectFieldName, objectSchema);
        definitionBuilderVisitor.visit(innerFieldName, stringSchema);
        definitionBuilderVisitor.leave(objectSchema);
        definitionBuilderVisitor.leave(innerCombinedSchema);
        definitionBuilderVisitor.leave(combinedSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(objectDefinition));
        assertThat(definitions.get(1), is(outerCombinedDefinition));

        verify(outerCombinedDefinition).addFieldDefinition(innerCombinedDefinition);
        verify(innerCombinedDefinition).addFieldDefinition(objectDefinition);
        verify(objectDefinition).addFieldDefinition(stringDefinition);
    }
}
