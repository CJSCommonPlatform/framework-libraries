package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.junit.Test;

public class FieldGeneratorTest {

    @Test
    public void shouldGenerateField() throws Exception {
        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(fieldDefinition.type()).thenReturn(BOOLEAN);
        when(fieldDefinition.getFieldName()).thenReturn("fieldName");
        when(fieldDefinition.isRequired()).thenReturn(true);
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(ClassName.get(Boolean.class));

        final FieldGenerator fieldGenerator = new FieldGenerator(fieldDefinition, classNameFactory, pluginContext);
        final FieldSpec fieldSpec = fieldGenerator.generateField();

        assertThat(fieldSpec, is(builder(Boolean.class, "fieldName", PRIVATE, FINAL).build()));
    }

    @Test
    public void shouldGenerateMethod() throws Exception {
        final FieldDefinition stringDefinition = mock(FieldDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(stringDefinition.getFieldName()).thenReturn("firstName");
        when(stringDefinition.isRequired()).thenReturn(true);
        when(classNameFactory.createTypeNameFrom(stringDefinition, pluginContext)).thenReturn(ClassName.get(String.class));

        final FieldGenerator fieldGenerator = new FieldGenerator(
                stringDefinition,
                classNameFactory,
                pluginContext);
        final List<MethodSpec> methodSpecs = fieldGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs, hasItem(MethodSpec.methodBuilder("getFirstName")
                .addModifiers(PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock.builder().addStatement("return $L", "firstName").build())
                .build()));
    }
}
