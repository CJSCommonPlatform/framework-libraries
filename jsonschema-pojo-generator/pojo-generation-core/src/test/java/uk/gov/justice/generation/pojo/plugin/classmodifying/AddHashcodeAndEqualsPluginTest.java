package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AddHashcodeAndEqualsPluginTest {

    @Mock
    private AdditionalPropertiesDeterminer additionalPropertiesDeterminer;

    @InjectMocks
    private AddHashcodeAndEqualsPlugin addHashcodeAndEqualsPlugin;

    @Test
    public void shouldGenerateEqualsAndHashCodeMethodsUsingTheClassesFields() throws Exception {

        final String expectedEqualsMethod =
                "@java.lang.Override\n" +
                "public boolean equals(final java.lang.Object obj) {\n  " +
                        "if (this == obj) return true;\n  " +
                        "if (obj == null || getClass() != obj.getClass()) return false;\n  " +
                        "final org.bloggs.fred.MyClass that = (org.bloggs.fred.MyClass) obj;\n\n  " +
                        "return java.util.Objects.equals(this.firstName, that.firstName) &&\n  " +
                        "java.util.Objects.equals(this.lastName, that.lastName) &&\n  " +
                        "java.util.Objects.equals(this.age, that.age);\n}\n";

        final String expectedHashCodeMethod =
                "@java.lang.Override\n" +
                        "public int hashCode() {\n  " +
                        "return java.util.Objects.hash(firstName, lastName, age);" +
                        "}\n";

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

        final TypeSpec.Builder builder = addHashcodeAndEqualsPlugin.generateWith(classBuilder, classDefinition, pluginContext);

        final TypeSpec modifiedClassBuilder = builder.build();

        assertThat(modifiedClassBuilder.methodSpecs.size(), is(2));
        assertThat(modifiedClassBuilder.methodSpecs.get(0).toString(), is(expectedEqualsMethod));
        assertThat(modifiedClassBuilder.methodSpecs.get(1).toString(), is(expectedHashCodeMethod));
    }

    @Test
    public void shouldNotAddHasCodeNorEqualsIfNoFieldsAreDefined() throws Exception {
        final TypeSpec.Builder classBuilder = classBuilder("MyClass").addModifiers(PUBLIC);

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classDefinition.getFieldDefinitions()).thenReturn(emptyList());
        when(pluginContext.getClassNameFactory()).thenReturn(classNameFactory);
        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(get("org.bloggs.fred", "MyClass"));

        final TypeSpec.Builder builder = addHashcodeAndEqualsPlugin.generateWith(classBuilder, classDefinition, pluginContext);

        final TypeSpec modifiedClassBuilder = builder.build();

        assertThat(modifiedClassBuilder.methodSpecs.size(), is(0));
    }

    @Test
    public void shouldAddAdditionalPropertiesToHashcodeAndEquals() throws Exception {

        final String expectedEqualsMethod =
                "@java.lang.Override\n" +
                        "public boolean equals(final java.lang.Object obj) {\n  " +
                        "if (this == obj) return true;\n  " +
                        "if (obj == null || getClass() != obj.getClass()) return false;\n  " +
                        "final org.bloggs.fred.MyClass that = (org.bloggs.fred.MyClass) obj;\n\n  " +
                        "return java.util.Objects.equals(this.firstName, that.firstName) &&\n  " +
                        "java.util.Objects.equals(this.lastName, that.lastName) &&\n  " +
                        "java.util.Objects.equals(this.age, that.age) &&\n  " +
                        "java.util.Objects.equals(this.additionalProperties, that.additionalProperties);\n}\n";

        final String expectedHashCodeMethod =
                "@java.lang.Override\n" +
                        "public int hashCode() {\n  " +
                        "return java.util.Objects.hash(firstName, lastName, age, additionalProperties);" +
                        "}\n";

        final TypeSpec.Builder classBuilder = classBuilder("MyClass").addModifiers(PUBLIC);

        final List<Definition> fieldDefinitions = asList(
                new FieldDefinition(STRING, "firstName"),
                new FieldDefinition(STRING, "lastName"),
                new FieldDefinition(STRING, "age"));

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(classDefinition.getFieldDefinitions()).thenReturn(fieldDefinitions);
        when(pluginContext.getClassNameFactory()).thenReturn(classNameFactory);
        when(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)).thenReturn(true);
        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(get("org.bloggs.fred", "MyClass"));

        assertThat(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext), is(true));

        final TypeSpec.Builder builder = addHashcodeAndEqualsPlugin.generateWith(classBuilder, classDefinition, pluginContext);

        final TypeSpec modifiedClassBuilder = builder.build();

        assertThat(modifiedClassBuilder.methodSpecs.size(), is(2));
        assertThat(modifiedClassBuilder.methodSpecs.get(0).toString(), is(expectedEqualsMethod));
        assertThat(modifiedClassBuilder.methodSpecs.get(1).toString(), is(expectedHashCodeMethod));
    }

    @Test
    public void shouldBeInstantiableUsingItsFactoryMethod() throws Exception {

        final Class<AddHashcodeAndEqualsPlugin> pluginClass = AddHashcodeAndEqualsPlugin.class;

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
