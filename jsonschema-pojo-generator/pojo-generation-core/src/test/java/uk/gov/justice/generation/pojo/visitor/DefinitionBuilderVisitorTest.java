package uk.gov.justice.generation.pojo.visitor;

import static org.everit.json.schema.CombinedSchema.ALL_CRITERION;
import static org.everit.json.schema.CombinedSchema.ANY_CRITERION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.CombinedDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;

import java.util.List;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefinitionBuilderVisitorTest {

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

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, outerObjectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(innerFieldName, innerObjectSchema)).thenReturn(innerDefinition);
        when(definitionFactory.constructDefinitionFor(outerStringFieldName, outerStringSchema)).thenReturn(outerStringDefinition);
        when(definitionFactory.constructDefinitionFor(innerStringField, innerStringSchema)).thenReturn(innerStringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(booleanFieldName, booleanSchema)).thenReturn(booleanDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(enumFieldName, enumSchema)).thenReturn(enumDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(numberFieldName, numberProperty)).thenReturn(numberDefinition);
        when(definitionFactory.constructDefinitionFor(integerFieldName, integerProperty)).thenReturn(integerDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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
        final ClassDefinition arrayDefinition = mock(ClassDefinition.class);
        final ClassDefinition arrayObjectDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(arrayFieldName, arraySchema)).thenReturn(arrayDefinition);
        when(definitionFactory.constructDefinitionFor(arrayObjectFieldName, arrayObjectSchema)).thenReturn(arrayObjectDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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
    public void shouldGenerateClassDefinitionWithReferenceSchemaProperty() throws Exception {
        final String outerFieldName = "outerClass";
        final String referenceFieldName = "referenceProperty";
        final String referredFieldName = "referredObject";

        final ReferenceSchema referenceSchema = ReferenceSchema.builder().refValue("#/definitions/uuid").build();

        final ObjectSchema referredSchema = ObjectSchema.builder()
                .build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(referenceFieldName, referenceSchema)
                .build();

        final ClassDefinition outerDefinition = mock(ClassDefinition.class);
        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        final ClassDefinition referredDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(referenceFieldName, referenceSchema)).thenReturn(referenceDefinition);
        when(definitionFactory.constructDefinitionFor(referredFieldName, referredSchema)).thenReturn(referredDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

        definitionBuilderVisitor.enter(outerFieldName, objectSchema);
        definitionBuilderVisitor.enter(referenceFieldName, referenceSchema);
        definitionBuilderVisitor.enter(referredFieldName, referredSchema);
        definitionBuilderVisitor.leave(referredSchema);
        definitionBuilderVisitor.leave(referenceSchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(referredDefinition));
        assertThat(definitions.get(1), is(outerDefinition));

        verify(outerDefinition).addFieldDefinition(referenceDefinition);
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

        when(definitionFactory.constructRootDefinitionFor(outerFieldName, objectSchema)).thenReturn(outerDefinition);
        when(definitionFactory.constructDefinitionFor(stringFieldName, stringSchema)).thenReturn(stringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .build();

        final CombinedSchema combinedSchema = CombinedSchema.builder()
                .subschema(objectSchema)
                .criterion(ALL_CRITERION)
                .build();

        final CombinedDefinition combinedDefinition = mock(CombinedDefinition.class);
        final ClassDefinition objectDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructRootDefinitionFor(combinedFieldName, combinedSchema)).thenReturn(combinedDefinition);
        when(definitionFactory.constructDefinitionFor(combinedFieldName, objectSchema)).thenReturn(objectDefinition);

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

        when(definitionFactory.constructRootDefinitionFor(combinedFieldName, combinedSchema)).thenReturn(combinedDefinition);
        when(definitionFactory.constructDefinitionFor(combinedFieldName, objectSchema)).thenReturn(objectDefinition);
        when(definitionFactory.constructDefinitionFor(innerFieldName, stringSchema)).thenReturn(stringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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

        when(definitionFactory.constructRootDefinitionFor(combinedFieldName, combinedSchema)).thenReturn(outerCombinedDefinition);
        when(definitionFactory.constructDefinitionFor(combinedFieldName, innerCombinedSchema)).thenReturn(innerCombinedDefinition);
        when(definitionFactory.constructDefinitionFor(innerObjectFieldName, objectSchema)).thenReturn(objectDefinition);
        when(definitionFactory.constructDefinitionFor(innerFieldName, stringSchema)).thenReturn(stringDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

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

    @Test
    public void shouldDoNothingWhenVisitingANullSchema() throws Exception {

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);
        definitionBuilderVisitor.visit("fieldName", NullSchema.builder().build());
    }

    @Test
    public void shouldGenerateRootClassDefinitionWithRootEmptySchemaProperty() throws Exception {
        final String emptyFieldName = "emptyProperty";

        final EmptySchema rootEmptySchema = EmptySchema.builder().build();

        final ClassDefinition emptyClassDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructRootDefinitionFor(emptyFieldName, rootEmptySchema)).thenReturn(emptyClassDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

        definitionBuilderVisitor.visit(emptyFieldName, rootEmptySchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        assertThat(definitions.get(0), is(emptyClassDefinition));

        verify(emptyClassDefinition).setAllowAdditionalProperties(true);
    }

    @Test
    public void shouldGenerateClassDefinitionWithNonRootEmptySchemaProperty() throws Exception {

        final EmptySchema emptySchema = EmptySchema.builder().build();
        final String emptyFieldName = "emptyProperty";
        final String rootObjectFieldName = "rootObject";


        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(emptyFieldName, emptySchema)
                .build();

        final ClassDefinition rootClassDefinition = mock(ClassDefinition.class);
        final ClassDefinition emptyClassDefinition = mock(ClassDefinition.class);

        when(definitionFactory.constructRootDefinitionFor(rootObjectFieldName, objectSchema)).thenReturn(rootClassDefinition);
        when(definitionFactory.constructDefinitionFor(emptyFieldName, emptySchema)).thenReturn(emptyClassDefinition);

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(definitionFactory);

        definitionBuilderVisitor.enter(rootObjectFieldName, objectSchema);
        definitionBuilderVisitor.visit(emptyFieldName, emptySchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        assertThat(definitions.get(0), is(emptyClassDefinition));
        assertThat(definitions.get(1), is(rootClassDefinition));

        verify(emptyClassDefinition).setAllowAdditionalProperties(true);
    }
}
