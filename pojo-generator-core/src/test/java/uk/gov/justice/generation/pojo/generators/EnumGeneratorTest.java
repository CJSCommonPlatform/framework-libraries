package uk.gov.justice.generation.pojo.generators;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnumGeneratorTest {

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private PluginContext pluginContext;

    @Test
    public void shouldGenerateTypeSpecForSingleWordEnumValue() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", singletonList("Mr"));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition, classNameFactory, pluginContext);

        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(ClassName.get("", "Title"));

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("MR"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(3));
    }

    @Test
    public void shouldGenerateTypeSpecForMultiWordEnumValue() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", singletonList("Multi word value"));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition, classNameFactory, pluginContext);

        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(ClassName.get("", "Title"));

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("MULTI_WORD_VALUE"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(3));
    }

    @Test
    public void shouldGenerateTypeSpecForBlankString() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", singletonList(""));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition, classNameFactory, pluginContext);

        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(ClassName.get("", "Title"));

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("BLANK"));
    }

    @Test
    public void shouldGenerateTypeSpecWithValueForMethod() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", asList("", "Mr"));
        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition, classNameFactory, pluginContext);

        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(ClassName.get("", "Title"));

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Title"));
        assertThat(typeSpec.enumConstants.keySet(), hasItems("BLANK", "MR"));

        final MethodSpec valueForMethod = typeSpec.methodSpecs.get(2);
        assertThat(valueForMethod.name, is("valueFor"));
        assertThat(valueForMethod.parameters, hasItem(ParameterSpec
                .builder(String.class, "value", FINAL)
                .build()));
        assertThat(valueForMethod.returnType.toString(), is("java.util.Optional<Title>"));
    }

    @Test
    public void shouldReturnClassName() {
        final EnumDefinition enumDefinition = new EnumDefinition("title", emptyList());

        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(ClassName.get("", "Title"));

        final EnumGenerator enumGenerator = new EnumGenerator(enumDefinition, classNameFactory, pluginContext);

        assertThat(enumGenerator.getSimpleClassName(), is("Title"));

    }

}
