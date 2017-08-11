package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import java.util.List;

import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefinitionBuilderVisitorTest {

    private static final String INVALID_SCHEMA_ID_MESSAGE = "Invalid Schema: all schema value types must have the id set for correct source generation.";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldGenerateClassDefinitionsWithStringSchemaProperties() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String outerClass = "OuterClass";
        final String innerClass = "InnerClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);

        final StringSchema innerProperty = StringSchema.builder().id("innerProperty").build();
        final StringSchema outerProperty = StringSchema.builder().id("outerProperty").build();

        final ObjectSchema objectSchema_1 = ObjectSchema.builder().addPropertySchema("innerProperty", innerProperty).id(outerClass).build();
        final ObjectSchema objectSchema_2 = ObjectSchema.builder().addPropertySchema("outerProperty", outerProperty).id(innerClass).build();

        definitionBuilderVisitor.visitEnter(objectSchema_1);
        definitionBuilderVisitor.visitEnter(objectSchema_2);
        definitionBuilderVisitor.visit(innerProperty);
        definitionBuilderVisitor.visitLeave(objectSchema_2);
        definitionBuilderVisitor.visit(outerProperty);
        definitionBuilderVisitor.visitLeave(objectSchema_1);

        final List<ClassDefinition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));

        assertThat(definitions.get(0).getClassName().getPackageName(), is(packageName));
        assertThat(definitions.get(0).getClassName().getSimpleName(), is(innerClass));
        assertThat(definitions.get(0).getFieldDefinitions().get(0).getFieldName(), is("innerProperty"));
        assertThat(definitions.get(0).getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.lang.String"));

        assertThat(definitions.get(1).getClassName().getPackageName(), is(packageName));
        assertThat(definitions.get(1).getClassName().getSimpleName(), is(outerClass));
        assertThat(definitions.get(1).getFieldDefinitions().get(1).getFieldName(), is("outerProperty"));
        assertThat(definitions.get(1).getFieldDefinitions().get(1).getClassName().getFullyQualifiedName(), is("java.lang.String"));
    }

    @Test
    public void shouldGenerateClassDefinitionWithBooleanSchemaProperty() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String outerClass = "OuterClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);

        final BooleanSchema outerProperty = BooleanSchema.builder().id("outerProperty").build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("outerProperty", outerProperty).id(outerClass).build();

        definitionBuilderVisitor.visitEnter(objectSchema);
        definitionBuilderVisitor.visit(outerProperty);
        definitionBuilderVisitor.visitLeave(objectSchema);

        final List<ClassDefinition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));

        assertThat(definitions.get(0).getClassName().getPackageName(), is(packageName));
        assertThat(definitions.get(0).getClassName().getSimpleName(), is(outerClass));
        assertThat(definitions.get(0).getFieldDefinitions().get(0).getFieldName(), is("outerProperty"));
        assertThat(definitions.get(0).getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.lang.Boolean"));
    }

    @Test
    public void shouldGenerateClassDefinitionWithNumberSchemaProperty() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String outerClass = "OuterClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);

        final NumberSchema numberProperty = NumberSchema.builder().id("numberProperty").build();
        final NumberSchema integerProperty = NumberSchema.builder().requiresInteger(true).id("integerProperty").build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema("numberProperty", numberProperty)
                .addPropertySchema("integerProperty", integerProperty)
                .id(outerClass)
                .build();

        definitionBuilderVisitor.visitEnter(objectSchema);
        definitionBuilderVisitor.visit(numberProperty);
        definitionBuilderVisitor.visit(integerProperty);
        definitionBuilderVisitor.visitLeave(objectSchema);

        final List<ClassDefinition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));

        assertThat(definitions.get(0).getClassName().getPackageName(), is(packageName));
        assertThat(definitions.get(0).getClassName().getSimpleName(), is(outerClass));

        assertThat(definitions.get(0).getFieldDefinitions().get(0).getFieldName(), is("numberProperty"));
        assertThat(definitions.get(0).getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.math.BigDecimal"));

        assertThat(definitions.get(0).getFieldDefinitions().get(1).getFieldName(), is("integerProperty"));
        assertThat(definitions.get(0).getFieldDefinitions().get(1).getClassName().getFullyQualifiedName(), is("java.lang.Integer"));
    }

    @Test
    public void shouldThrowExceptionIfSchemaIdIsNotSetOnObjectSchema() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage(INVALID_SCHEMA_ID_MESSAGE);

        final String packageName = "org.bloggs.fred";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);
        final ObjectSchema objectSchema = ObjectSchema.builder().build();

        definitionBuilderVisitor.visitEnter(objectSchema);
    }

    @Test
    public void shouldThrowExceptionIfSchemaIdIsNotSetOnStringSchema() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage(INVALID_SCHEMA_ID_MESSAGE);

        final String packageName = "org.bloggs.fred";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);
        final StringSchema stringSchema = StringSchema.builder().build();

        definitionBuilderVisitor.visit(stringSchema);
    }

    @Test
    public void shouldThrowExceptionIfSchemaIdIsNotSetOnBooleanSchema() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage(INVALID_SCHEMA_ID_MESSAGE);

        final String packageName = "org.bloggs.fred";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);
        final BooleanSchema booleanSchema = BooleanSchema.builder().build();

        definitionBuilderVisitor.visit(booleanSchema);
    }

    @Test
    public void shouldThrowExceptionIfSchemaIdIsNotSetOnNumberSchema() throws Exception {
        expectedException.expect(UnsupportedSchemaException.class);
        expectedException.expectMessage(INVALID_SCHEMA_ID_MESSAGE);

        final String packageName = "org.bloggs.fred";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName);
        final NumberSchema numberSchema = NumberSchema.builder().build();

        definitionBuilderVisitor.visit(numberSchema);
    }
}
