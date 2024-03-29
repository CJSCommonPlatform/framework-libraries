package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddAdditionalPropertiesToClassPluginTest {

    @InjectMocks
    private AddAdditionalPropertiesToClassPlugin addAdditionalPropertiesToClassPlugin;

    @Test
    public void shouldGenerateTheAdditionalPropertiesFieldDeclarationIfAllowAdditionalPropertiesIsTrue() throws Exception {

        final String expectedField = "private final java.util.HashMap" +
                "<java.lang.String, java.lang.Object> additionalProperties;\n";

        final TypeSpec.Builder classBuilder = classBuilder("MyClass");

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(true);

        final TypeSpec.Builder modifiedBuilder = addAdditionalPropertiesToClassPlugin.generateWith(
                classBuilder,
                classDefinition,
                pluginContext);

        final TypeSpec typeSpec = modifiedBuilder.build();

        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.fieldSpecs.get(0).toString(), is(expectedField));
    }

    @Test
    public void shouldGenerateTheAdditionalPropertiesGetterAndSetterIfAllowAdditionalPropertiesIsTrue() throws Exception {

        final String expectedGetter =
                "public java.util.Map<java.lang.String, java.lang.Object> getAdditionalProperties() {\n" +
                        "  return additionalProperties;\n" +
                        "}\n";

        final String expectedSetter =
                "public void setAdditionalProperty(" +
                        "final java.lang.String name, " +
                        "final java.lang.Object value) {\n" +
                        "  additionalProperties.put(name, value);\n" +
                        "}\n";

        final TypeSpec.Builder classBuilder = classBuilder("MyClass");

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(true);

        final TypeSpec.Builder modifiedBuilder = addAdditionalPropertiesToClassPlugin.generateWith(
                classBuilder,
                classDefinition,
                pluginContext);

        final TypeSpec typeSpec = modifiedBuilder.build();

        assertThat(typeSpec.methodSpecs.size(), is(2));
        assertThat(typeSpec.methodSpecs.get(0).toString(), is(expectedGetter));
        assertThat(typeSpec.methodSpecs.get(1).toString(), is(expectedSetter));
    }

    @Test
    public void shouldNotModifyTheClassSpecIfAdditionalPropertiesIsFalse() throws Exception {

        final TypeSpec.Builder classBuilder = classBuilder("MyClass");

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(classDefinition.allowAdditionalProperties()).thenReturn(false);

        final TypeSpec.Builder modifiedBuilder = addAdditionalPropertiesToClassPlugin.generateWith(
                classBuilder,
                classDefinition,
                pluginContext);

        final TypeSpec typeSpec = modifiedBuilder.build();

        assertThat(typeSpec.fieldSpecs.size(), is(0));
        assertThat(typeSpec.methodSpecs.size(), is(0));
    }
}
