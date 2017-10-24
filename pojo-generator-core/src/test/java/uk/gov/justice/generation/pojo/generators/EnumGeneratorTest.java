package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static java.util.Arrays.asList;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnumGeneratorTest {

    @Mock
    private PluginContext pluginContext;

    @Mock
    private EnumDefinition enumDefinition;

    @Mock
    private ClassNameFactory classNameFactory;

    @InjectMocks
    private EnumGenerator enumGenerator;

    @Test
    public void shouldGenerateTypeSpecForSingleWordEnumValue() {
        final ClassName className = get(Pojo.class);

        when(enumDefinition.getEnumValues()).thenReturn(singletonList("Mr"));
        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(className);
        when(classNameFactory.createClassNameFrom(enumDefinition)).thenReturn(className);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Pojo"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("MR"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(3));
    }

    @Test
    public void shouldGenerateTypeSpecForMultiWordEnumValue() {
        final ClassName className = get(Pojo.class);

        when(enumDefinition.getEnumValues()).thenReturn(singletonList("Multi word value"));
        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(className);
        when(classNameFactory.createClassNameFrom(enumDefinition)).thenReturn(className);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Pojo"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("MULTI_WORD_VALUE"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(3));
    }

    @Test
    public void shouldGenerateTypeSpecForBlankString() {
        final ClassName className = get(Pojo.class);

        when(enumDefinition.getEnumValues()).thenReturn(singletonList(""));
        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(className);
        when(classNameFactory.createClassNameFrom(enumDefinition)).thenReturn(className);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Pojo"));
        assertThat(typeSpec.enumConstants.keySet(), hasItem("BLANK"));
    }

    @Test
    public void shouldGenerateTypeSpecWithValueForMethod() {
        final ClassName className = get(Pojo.class);

        when(enumDefinition.getEnumValues()).thenReturn(asList("", "Mr"));
        when(classNameFactory.createTypeNameFrom(enumDefinition, pluginContext)).thenReturn(className);
        when(classNameFactory.createClassNameFrom(enumDefinition)).thenReturn(className);

        final TypeSpec typeSpec = enumGenerator.generate();

        assertThat(typeSpec.name, is("Pojo"));
        assertThat(typeSpec.enumConstants.keySet(), hasItems("BLANK", "MR"));

        final MethodSpec valueForMethod = typeSpec.methodSpecs.get(2);
        assertThat(valueForMethod.name, is("valueFor"));
        assertThat(valueForMethod.parameters, hasItem(ParameterSpec
                .builder(String.class, "value", FINAL)
                .build()));
        assertThat(valueForMethod.returnType.toString(), is("java.util.Optional<uk.gov.justice.generation.pojo.generators.EnumGeneratorTest.Pojo>"));
    }

    @Test
    public void shouldReturnClassName() {
        final ClassName className = get(Pojo.class);

        when(classNameFactory.createClassNameFrom(enumDefinition)).thenReturn(className);

        assertThat(enumGenerator.getSimpleClassName(), is("Pojo"));
    }

    @Test
    public void shouldReturnPackageName() {
        final ClassName className = get(Pojo.class);

        when(classNameFactory.createClassNameFrom(enumDefinition)).thenReturn(className);

        assertThat(enumGenerator.getPackageName(), is("uk.gov.justice.generation.pojo.generators"));
    }

    private class Pojo {
    }
}
