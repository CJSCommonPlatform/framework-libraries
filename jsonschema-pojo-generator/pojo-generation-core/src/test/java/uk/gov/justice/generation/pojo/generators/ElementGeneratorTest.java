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
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;

import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;

public class ElementGeneratorTest {

    @Test
    public void shouldGenerateField() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final OptionalTypeNameUtil optionalTypeNameUtil = mock(OptionalTypeNameUtil.class);

        when(classNameFactory.createTypeNameFrom(classDefinition, pluginContext)).thenReturn(ClassName.get("org.something", "Address"));

        final ElementGenerator elementGenerator = new ElementGenerator(
                classDefinition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);
        final FieldSpec fieldSpec = elementGenerator.generateField();

        assertThat(fieldSpec, is(builder(get("org.something", "Address"), "address", PRIVATE, FINAL).build()));
    }

    @Test
    public void shouldGenerateGetterMethod() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final OptionalTypeNameUtil optionalTypeNameUtil = mock(OptionalTypeNameUtil.class);

        final ClassName returnType = get("org.something", "Address");
        when(classNameFactory.createTypeNameFrom(classDefinition, pluginContext)).thenReturn(returnType);
        when(optionalTypeNameUtil.isOptionalType(returnType)).thenReturn(false);

        final ElementGenerator elementGenerator = new ElementGenerator(
                classDefinition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);
        final List<MethodSpec> methodSpecs = elementGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs.size(), is(1));

        final String expectedGetterMethod =
                "public org.something.Address getAddress() {\n" +
                        "  return address;" +
                        "\n}" +
                        "\n";

        assertThat(methodSpecs.get(0).toString(), is(expectedGetterMethod));
    }

    @Test
    public void shouldGenerateOptionalGetterMethod() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final OptionalTypeNameUtil optionalTypeNameUtil = mock(OptionalTypeNameUtil.class);

        final TypeName rawTypeName = get("org.something", "Address");
        final ParameterizedTypeName optionalTypeName = ParameterizedTypeName.get(get(Optional.class), rawTypeName);

        when(classNameFactory.createTypeNameFrom(classDefinition, pluginContext)).thenReturn(optionalTypeName);
        when(optionalTypeNameUtil.isOptionalType(optionalTypeName)).thenReturn(true);

        final ElementGenerator elementGenerator = new ElementGenerator(
                classDefinition,
                classNameFactory,
                pluginContext,
                optionalTypeNameUtil);
        final List<MethodSpec> methodSpecs = elementGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs.size(), is(1));

        final String expectedGetterMethod =
                "public java.util.Optional<org.something.Address> getAddress() {\n" +
                        "  if (address != null) {\n" +
                        "    return address;\n" +
                        "  }\n" +
                        "  return java.util.Optional.empty();\n" +
                        "}\n";

        assertThat(methodSpecs.get(0).toString(), is(expectedGetterMethod));
    }
}
