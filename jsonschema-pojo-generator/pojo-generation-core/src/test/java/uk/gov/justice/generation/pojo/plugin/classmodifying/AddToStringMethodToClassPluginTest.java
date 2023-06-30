package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
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


    @Test
    public void shouldBeInstantiableUsingItsFactoryMethod() throws Exception {

        final Class<?> pluginClass = AddToStringMethodToClassPlugin.class;

        final Method[] declaredMethods = pluginClass.getDeclaredMethods();
        final Optional<Method> methodOptional = stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(FactoryMethod.class))
                .findFirst();

        if (methodOptional.isPresent()) {
            final Object plugin = methodOptional.get().invoke(null);
            assertThat(plugin, is(instanceOf(pluginClass)));
        } else {
            fail();
        }
    }

}
