package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Visitor visitor;

    @Test
    public void shouldGenerateClassDefinitionsWithStringSchemaProperties() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String eventName = "example.events";
        final String outerClass = "OuterClass";
        final String innerClass = "InnerClass";

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName, eventName);

        final StringSchema innerProperty = StringSchema.builder().build();
        final StringSchema outerProperty = StringSchema.builder().build();

        final ObjectSchema objectSchema_1 = ObjectSchema.builder().addPropertySchema("innerProperty", innerProperty).id(outerClass).build();
        final ObjectSchema objectSchema_2 = ObjectSchema.builder().addPropertySchema("outerProperty", outerProperty).id(innerClass).build();

        definitionBuilderVisitor.enter(outerClass, objectSchema_1);
        definitionBuilderVisitor.enter(innerClass, objectSchema_2);
        definitionBuilderVisitor.visit("innerProperty", innerProperty);
        definitionBuilderVisitor.leave(objectSchema_2);
        definitionBuilderVisitor.visit("outerProperty", outerProperty);
        definitionBuilderVisitor.leave(objectSchema_1);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        final ClassDefinition classTypeDefinition_1 = (ClassDefinition) definitions.get(0);
        final ClassDefinition classTypeDefinition_2 = (ClassDefinition) definitions.get(1);

        assertThat(classTypeDefinition_1.getClassName().getPackageName(), is(packageName));
        assertThat(classTypeDefinition_1.getClassName().getSimpleName(), is(innerClass));
        assertThat(classTypeDefinition_1.getEventName(), is(Optional.empty()));
        assertThat(classTypeDefinition_1.getFieldDefinitions().get(0).getFieldName(), is("innerProperty"));
        assertThat(classTypeDefinition_1.getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.lang.String"));

        assertThat(classTypeDefinition_2.getClassName().getPackageName(), is(packageName));
        assertThat(classTypeDefinition_2.getClassName().getSimpleName(), is(outerClass));
        assertThat(classTypeDefinition_2.getEventName(), is(Optional.of(eventName)));
        assertThat(classTypeDefinition_2.getFieldDefinitions().get(1).getFieldName(), is("outerProperty"));
        assertThat(classTypeDefinition_2.getFieldDefinitions().get(1).getClassName().getFullyQualifiedName(), is("java.lang.String"));
    }

    @Test
    public void shouldGenerateClassDefinitionWithBooleanSchemaProperty() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String eventName = "example.events";
        final String outerClass = "OuterClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName, eventName);

        final BooleanSchema outerProperty = BooleanSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("outerProperty", outerProperty).id(outerClass).build();

        definitionBuilderVisitor.enter("outerClass", objectSchema);
        definitionBuilderVisitor.visit("outerProperty", outerProperty);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        final ClassDefinition classTypeDefinition = (ClassDefinition) definitions.get(0);

        assertThat(classTypeDefinition.getClassName().getPackageName(), is(packageName));
        assertThat(classTypeDefinition.getClassName().getSimpleName(), is(outerClass));
        assertThat(classTypeDefinition.getEventName(), is(Optional.of(eventName)));
        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getFieldName(), is("outerProperty"));
        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.lang.Boolean"));
    }

    @Test
    public void shouldGenerateClassDefinitionWithEnumSchemaProperty() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String eventName = "example.events";
        final String outerClass = "OuterClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName, eventName);

        final EnumSchema outerProperty = EnumSchema.builder().build();
        final ObjectSchema objectSchema = ObjectSchema.builder().addPropertySchema("outerProperty", outerProperty).id(outerClass).build();

        definitionBuilderVisitor.enter("outerClass", objectSchema);
        definitionBuilderVisitor.visit("outerProperty", outerProperty);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));

        assertThat(definitions.get(0), is(instanceOf(EnumDefinition.class)));
        assertThat(definitions.get(0).getClassName().getPackageName(), is(packageName));
        assertThat(definitions.get(0).getClassName().getSimpleName(), is("OuterProperty"));

        final ClassDefinition classTypeDefinition = (ClassDefinition) definitions.get(1);
        assertThat(classTypeDefinition.getClassName().getPackageName(), is(packageName));
        assertThat(classTypeDefinition.getClassName().getSimpleName(), is(outerClass));
        assertThat(classTypeDefinition.getEventName(), is(Optional.of(eventName)));
        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getFieldName(), is("outerProperty"));
        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("org.bloggs.fred.OuterProperty"));
    }

    @Test
    public void shouldGenerateClassDefinitionWithNumberSchemaProperty() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String eventName = "example.events";
        final String outerClass = "OuterClass";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName, eventName);

        final NumberSchema numberProperty = NumberSchema.builder().build();
        final NumberSchema integerProperty = NumberSchema.builder().requiresInteger(true).id("integerProperty").build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema("numberProperty", numberProperty)
                .addPropertySchema("integerProperty", integerProperty)
                .id(outerClass)
                .build();

        definitionBuilderVisitor.enter("outerClass", objectSchema);
        definitionBuilderVisitor.visit("numberProperty", numberProperty);
        definitionBuilderVisitor.visit("integerProperty", integerProperty);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        final ClassDefinition classTypeDefinition = (ClassDefinition) definitions.get(0);

        assertThat(classTypeDefinition.getClassName().getPackageName(), is(packageName));
        assertThat(classTypeDefinition.getClassName().getSimpleName(), is(outerClass));
        assertThat(classTypeDefinition.getEventName(), is(Optional.of(eventName)));

        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getFieldName(), is("numberProperty"));
        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.math.BigDecimal"));

        assertThat(classTypeDefinition.getFieldDefinitions().get(1).getFieldName(), is("integerProperty"));
        assertThat(classTypeDefinition.getFieldDefinitions().get(1).getClassName().getFullyQualifiedName(), is("java.lang.Integer"));
    }

    @Test
    public void shouldGenerateClassDefinitionWithArraySchemaProperty() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String eventName = "example.events";
        final String outerClass = "OuterClass";
        final String arrayObject = "ArrayObject";
        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName, eventName);

        final ArraySchema arraySchema = ArraySchema.builder().build();

        final ObjectSchema arrayObjectSchema = ObjectSchema.builder()
                .id(outerClass)
                .build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema("arrayProperty", arraySchema)
                .id(outerClass)
                .build();

        definitionBuilderVisitor.enter("outerClass", objectSchema);
        definitionBuilderVisitor.enter("arrayProperty", arraySchema);
        definitionBuilderVisitor.enter("arrayObject", arrayObjectSchema);
        definitionBuilderVisitor.leave(arrayObjectSchema);
        definitionBuilderVisitor.leave(arraySchema);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(2));
        final ClassDefinition outerClassDefinition = (ClassDefinition) definitions.get(1);
        final ClassDefinition arrayObjectDefinition = (ClassDefinition) definitions.get(0);

        assertThat(outerClassDefinition.getClassName().getPackageName(), is(packageName));
        assertThat(outerClassDefinition.getClassName().getSimpleName(), is(outerClass));
        assertThat(outerClassDefinition.getEventName(), is(Optional.of(eventName)));

        assertThat(arrayObjectDefinition.getClassName().getPackageName(), is(packageName));
        assertThat(arrayObjectDefinition.getClassName().getSimpleName(), is(arrayObject));

        assertThat(outerClassDefinition.getFieldDefinitions().get(0).getFieldName(), is("arrayProperty"));
        assertThat(outerClassDefinition.getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is("java.util.List"));
    }

    @Test
    public void shouldGenerateClassDefinitionsWithDescriptionOfTheClassName() throws Exception {
        final String packageName = "org.bloggs.fred";
        final String eventName = "example.events";
        final String propertyName = "uuidProperty";

        final DefinitionBuilderVisitor definitionBuilderVisitor = new DefinitionBuilderVisitor(packageName, eventName);

        final StringSchema uuidProperty = StringSchema.builder().description("UUID").build();

        final ObjectSchema objectSchema = ObjectSchema.builder()
                .addPropertySchema(propertyName, uuidProperty)
                .build();

        definitionBuilderVisitor.enter("outerClass", objectSchema);
        definitionBuilderVisitor.visit(propertyName, uuidProperty);
        definitionBuilderVisitor.leave(objectSchema);

        final List<Definition> definitions = definitionBuilderVisitor.getDefinitions();

        assertThat(definitions.size(), is(1));
        final ClassDefinition classTypeDefinition = (ClassDefinition) definitions.get(0);

        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getFieldName(), is(propertyName));
        assertThat(classTypeDefinition.getFieldDefinitions().get(0).getClassName().getFullyQualifiedName(), is(UUID.class.getName()));
    }
}
