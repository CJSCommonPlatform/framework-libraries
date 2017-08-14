package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

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
    public void shouldReturnInstanceOfClassGenerator() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition("test", mock(ClassName.class));
        final ClassGeneratable classGeneratable = new JavaGeneratorFactory().createClassGeneratorFor(classDefinition);

        assertThat(classGeneratable, is(instanceOf(ClassGenerator.class)));
    }
}