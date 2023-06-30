package uk.gov.justice.generation.pojo.generators;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProvider;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.OptionalTypeNameUtil;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JavaGeneratorFactoryTest {

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private GenerationContext generationContext;

    @Mock
    private PluginContext pluginContext;

    @Mock
    private PluginProvider pluginProvider;

    @Mock
    private OptionalTypeNameUtil optionalTypeNameUtil;

    @Test
    public void shouldReturnInstanceOfFieldGeneratorForFieldDefinition() throws Exception {
        final FieldDefinition fieldDefinition = new FieldDefinition(STRING, "test");
        final PluginContext pluginContext = mock(PluginContext.class);

        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createGeneratorFor(fieldDefinition, pluginContext);

        assertThat(elementGeneratable, is(instanceOf(FieldGenerator.class)));
    }

    @Test
    public void shouldReturnInstanceOfElementGeneratorForClassDefinition() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(STRING, "test");
        final PluginContext pluginContext = mock(PluginContext.class);

        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createGeneratorFor(classDefinition, pluginContext);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    public void shouldReturnInstanceOfElementGeneratorForEnumDefinition() throws Exception {
        final EnumDefinition enumDefinition = new EnumDefinition("test", emptyList(), "id");
        final PluginContext pluginContext = mock(PluginContext.class);

        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createGeneratorFor(enumDefinition, pluginContext);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnListOfClassGeneratorAndEnumGeneratorForClassDefinitionAndEnumDefinition() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition(CLASS, "test1"),
                new EnumDefinition("test2", emptyList(), "id"));

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createClassGeneratorsFor(classDefinitions, pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.size(), is(2));
        assertThat(classGeneratables, hasItems(instanceOf(ClassGenerator.class), instanceOf(EnumGenerator.class)));
    }

    @Test
    public void shouldReturnEmptyListIfClassesAreAlreadyCreated() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition(CLASS, "test1"),
                new EnumDefinition("test2", emptyList(), "id"));

        when(generationContext.getIgnoredClassNames()).thenReturn(asList("Test1", "Test2"));

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createClassGeneratorsFor(classDefinitions, pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.size(), is(0));
    }

    @Test
    public void shouldReturnEmptyListForEmptyListOfDefinitions() throws Exception {
        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createClassGeneratorsFor(emptyList(), pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldIgnoreDefinitionsThatAreNotClassDefinitionOrEnumDefinition() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition(CLASS, "class"),
                new FieldDefinition(STRING, "field1"),
                new EnumDefinition("enum", emptyList(), "id"),
                new FieldDefinition(STRING, "field2")
        );

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory(classNameFactory, optionalTypeNameUtil)
                .createClassGeneratorsFor(classDefinitions, pluginProvider, pluginContext, generationContext);

        assertThat(classGeneratables.size(), is(2));
        assertThat(classGeneratables, hasItems(instanceOf(ClassGenerator.class), instanceOf(EnumGenerator.class)));

    }
}
