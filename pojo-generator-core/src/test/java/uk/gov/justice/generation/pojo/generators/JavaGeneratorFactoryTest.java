package uk.gov.justice.generation.pojo.generators;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.PluginContext;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JavaGeneratorFactoryTest {

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private GenerationContext generationContext;

    @Mock
    private PluginContext pluginContext;

    @Mock
    private PluginProvider pluginProvider;

    @Test
    public void shouldReturnInstanceOfFieldGeneratorForFieldDefinition() throws Exception {
        final FieldDefinition fieldDefinition = new FieldDefinition(STRING, "test");
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory(classNameFactory).createGeneratorFor(fieldDefinition);

        assertThat(elementGeneratable, is(instanceOf(FieldGenerator.class)));
    }

    @Test
    public void shouldReturnInstanceOfElementGeneratorForClassDefinition() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(STRING, "test");
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory(classNameFactory).createGeneratorFor(classDefinition);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    public void shouldReturnInstanceOfElementGeneratorForEnumDefinition() throws Exception {
        final EnumDefinition enumDefinition = new EnumDefinition("test", emptyList());
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory(classNameFactory).createGeneratorFor(enumDefinition);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnListOfClassGeneratorAndEnumGeneratorForClassDefinitionAndEnumDefinition() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition(CLASS, "test1"),
                new EnumDefinition("test2", emptyList()));

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory)
                .createClassGeneratorsFor(classDefinitions, pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.size(), is(2));
        assertThat(classGeneratables, hasItems(instanceOf(ClassGenerator.class), instanceOf(EnumGenerator.class)));
    }

    @Test
    public void shouldReturnEmptyListIfClassesAreAlreadyCreated() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition(CLASS, "test1"),
                new EnumDefinition("test2", emptyList()));

        when(generationContext.getIgnoredClassNames()).thenReturn(asList("Test1", "Test2"));

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory)
                .createClassGeneratorsFor(classDefinitions, pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.size(), is(0));
    }

    @Test
    public void shouldReturnEmptyListForEmptyListOfDefinitions() throws Exception {
        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory)
                .createClassGeneratorsFor(emptyList(), pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldIgnoreDefinitionsThatAreNotClassDefinitionOrEnumDefinition() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition(CLASS, "class"),
                new FieldDefinition(STRING, "field1"),
                new EnumDefinition("enum", emptyList()),
                new FieldDefinition(STRING, "field2")
        );

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory)
                .createClassGeneratorsFor(classDefinitions, pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.size(), is(2));
        assertThat(classGeneratables, hasItems(instanceOf(ClassGenerator.class), instanceOf(EnumGenerator.class)));

    }
}
