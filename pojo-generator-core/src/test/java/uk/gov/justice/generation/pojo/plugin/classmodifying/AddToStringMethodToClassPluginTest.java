package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.List;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AddToStringMethodToClassPluginTest {

    @Mock
    private AdditionalPropertiesDeterminer additionalPropertiesDeterminer;

    @InjectMocks
    private AddToStringMethodToClassPlugin addToStringMethodToClassPlugin;

    @Test
    public void shouldAddAToStringMethodToTheClassGeneration() throws Exception {

        final String expectedEqualsMethod =
                "@java.lang.Override\n" +
                        "public java.lang.String toString() {\n  " +
                        "return \"MyClass{\" +\n  " +
                        "\t\"firstName='\" + firstName + \"',\" +\n  " +
                        "\t\"lastName='\" + lastName + \"',\" +\n  " +
                        "\t\"age='\" + age + \"'\" +\n  \"}\";\n}\n";

        final TypeSpec.Builder classBuilder = classBuilder("MyClass").addModifiers(PUBLIC);

        final List<Definition> fieldDefinitions = asList(
                new FieldDefinition(STRING, "firstName"),
                new FieldDefinition(STRING, "lastName"),
                new FieldDefinition(STRING, "age"));

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classDefinition.getFieldDefinitions()).thenReturn(fieldDefinitions);
        when(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)).thenReturn(false);
        when(pluginContext.getClassNameFactory()).thenReturn(classNameFactory);
        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(get("org.bloggs.fred", "MyClass"));


        final TypeSpec.Builder builder = addToStringMethodToClassPlugin.generateWith(classBuilder, classDefinition, pluginContext);
        final TypeSpec classSpec = builder.build();

        assertThat(classSpec.methodSpecs.size(), is(1));
        assertThat(classSpec.methodSpecs.get(0).toString(), is(expectedEqualsMethod));
    }

    @Test
    public void shouldAddAdditionalPropertiesIfRequired() throws Exception {

        final String expectedEqualsMethod =
                "@java.lang.Override\n" +
                        "public java.lang.String toString() {" +
                        "\n  return \"MyClass{\" +\n  " +
                        "\t\"firstName='\" + firstName + \"',\" +\n  " +
                        "\t\"lastName='\" + lastName + \"',\" +\n  " +
                        "\t\"age='\" + age + \"',\" +\n  " +
                        "\t\"additionalProperties='\" + additionalProperties + \"'\" +\n  " +
                        "\"}\";\n}\n";

        final TypeSpec.Builder classBuilder = classBuilder("MyClass").addModifiers(PUBLIC);

        final List<Definition> fieldDefinitions = asList(
                new FieldDefinition(STRING, "firstName"),
                new FieldDefinition(STRING, "lastName"),
                new FieldDefinition(STRING, "age"));

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classDefinition.getFieldDefinitions()).thenReturn(fieldDefinitions);
        when(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)).thenReturn(true);
        when(pluginContext.getClassNameFactory()).thenReturn(classNameFactory);
        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(get("org.bloggs.fred", "MyClass"));


        final TypeSpec.Builder builder = addToStringMethodToClassPlugin.generateWith(classBuilder, classDefinition, pluginContext);
        final TypeSpec classSpec = builder.build();

        assertThat(classSpec.methodSpecs.size(), is(1));
        assertThat(classSpec.methodSpecs.get(0).toString(), is(expectedEqualsMethod));
    }
}
