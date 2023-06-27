package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.FieldSpec.builder;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.BOOLEAN;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;

import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.junit.Test;

public class FieldGeneratorTest {

    @Test
    public void shouldGenerateField() throws Exception {
        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final OptionalTypeNameUtil optionalTypeNameUtil = mock(OptionalTypeNameUtil.class);

        when(fieldDefinition.type()).thenReturn(BOOLEAN);
        when(fieldDefinition.getFieldName()).thenReturn("fieldName");
        when(fieldDefinition.isRequired()).thenReturn(true);
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(ClassName.get(Boolean.class));

        final FieldGenerator fieldGenerator = new FieldGenerator(
                fieldDefinition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);
        final FieldSpec fieldSpec = fieldGenerator.generateField();

        assertThat(fieldSpec, is(builder(Boolean.class, "fieldName", PRIVATE, FINAL).build()));
    }

    @Test
    public void shouldGenerateGetterMethod() throws Exception {

        final ClassName fieldType = ClassName.get(String.class);

        final FieldDefinition stringDefinition = mock(FieldDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final OptionalTypeNameUtil optionalTypeNameUtil = mock(OptionalTypeNameUtil.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(stringDefinition.getFieldName()).thenReturn("firstName");
        when(stringDefinition.isRequired()).thenReturn(true);
        when(classNameFactory.createTypeNameFrom(stringDefinition, pluginContext)).thenReturn(fieldType);
        when(optionalTypeNameUtil.isOptionalType(fieldType)).thenReturn(false);

        final FieldGenerator fieldGenerator = new FieldGenerator(
                stringDefinition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);
        
        final List<MethodSpec> methodSpecs = fieldGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs.size(), is(1));

        final String expectedGetterMethod =
                "public java.lang.String getFirstName() {\n" +
                        "  return firstName;\n" +
                        "}\n";
        
        assertThat(methodSpecs.get(0).toString(), is(expectedGetterMethod));
    }

    @Test
    public void shouldGenerateOptionalGetterMethod() throws Exception {

        final ClassName rawTypeName = ClassName.get(String.class);
        final ParameterizedTypeName optionalTypeName = ParameterizedTypeName.get(get(Optional.class), rawTypeName);

        final FieldDefinition stringDefinition = mock(FieldDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final OptionalTypeNameUtil optionalTypeNameUtil = mock(OptionalTypeNameUtil.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(stringDefinition.getFieldName()).thenReturn("firstName");
        when(stringDefinition.isRequired()).thenReturn(true);
        when(classNameFactory.createTypeNameFrom(stringDefinition, pluginContext)).thenReturn(optionalTypeName);
        when(optionalTypeNameUtil.isOptionalType(optionalTypeName)).thenReturn(true);

        final FieldGenerator fieldGenerator = new FieldGenerator(
                stringDefinition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);

        final List<MethodSpec> methodSpecs = fieldGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs.size(), is(1));

        final String expectedGetterMethod =
                "public java.util.Optional<java.lang.String> getFirstName() {\n" +
                        "  if (firstName != null) {\n" +
                        "    return firstName;\n" +
                        "  }\n" +
                        "  return java.util.Optional.empty();\n" +
                        "}\n";

        assertThat(methodSpecs.get(0).toString(), is(expectedGetterMethod));
    }
}
