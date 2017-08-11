package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import java.util.List;

import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.StringSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefinitionBuilderVisitorTest {

    @Test
    public void shouldGenerateClassDefinitions() throws Exception {
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

}
