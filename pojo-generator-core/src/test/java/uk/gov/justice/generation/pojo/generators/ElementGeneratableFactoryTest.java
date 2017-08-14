package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import org.junit.Test;

public class ElementGeneratableFactoryTest {

    @Test
    public void shouldCreateFieldGeneratorForFieldDefinition() throws Exception {
        final FieldDefinition fieldDefinition = new FieldDefinition("fieldName", new ClassName(String.class));
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(fieldDefinition);

        assertThat(elementGeneratable, is(instanceOf(FieldGenerator.class)));
    }

    @Test
    public void shouldCreatElementGeneratorForClassDefinition() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition("fieldName", new ClassName("", ""));
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(classDefinition);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }
}
