package uk.gov.justice.generation.pojo.generators;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.List;

import org.junit.Test;

public class JavaGeneratorFactoryTest {

    @Test
    public void shouldReturnInstanceOfFieldGeneratorForFieldDefinition() throws Exception {
        final FieldDefinition fieldDefinition = new FieldDefinition("test", mock(ClassName.class));
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(fieldDefinition);

        assertThat(elementGeneratable, is(instanceOf(FieldGenerator.class)));
    }

    @Test
    public void shouldReturnInstanceOfElementGeneratorForClassDefinition() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition("test", mock(ClassName.class));
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(classDefinition);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    public void shouldReturnInstanceOfElementGeneratorForEnumDefinition() throws Exception {
        final EnumDefinition enumDefinition = new EnumDefinition("test", mock(ClassName.class), emptyList());
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(enumDefinition);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnListOfClassGeneratorAndEnumGeneratorForClassDefintionAndEnumDefintion() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition("test1", mock(ClassName.class)),
                new EnumDefinition("test2", mock(ClassName.class), emptyList()));

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory().createClassGeneratorsFor(classDefinitions);

        assertThat(classGeneratables.size(), is(2));
        assertThat(classGeneratables, hasItems(instanceOf(ClassGenerator.class), instanceOf(EnumGenerator.class)));
    }

    @Test
    public void shouldReturnEmptyListForEmptyListOfDefinitions() throws Exception {
        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory().createClassGeneratorsFor(emptyList());

        assertThat(classGeneratables.isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldIgnoreDefinitionsThatAreNotClassDefinitionOrEnumDefinition() throws Exception {
        final List<Definition> classDefinitions = asList(
                new ClassDefinition("class", mock(ClassName.class)),
                new FieldDefinition("field1", mock(ClassName.class)),
                new EnumDefinition("enum", mock(ClassName.class), emptyList()),
                new FieldDefinition("field2", mock(ClassName.class))
        );

        final List<ClassGeneratable> classGeneratables = new JavaGeneratorFactory().createClassGeneratorsFor(classDefinitions);

        assertThat(classGeneratables.size(), is(2));
        assertThat(classGeneratables, hasItems(instanceOf(ClassGenerator.class), instanceOf(EnumGenerator.class)));

    }
}
