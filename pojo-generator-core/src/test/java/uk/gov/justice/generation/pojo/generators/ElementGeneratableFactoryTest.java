package uk.gov.justice.generation.pojo.generators;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.EnumDefinition;
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
    public void shouldCreateElementGeneratorForClassDefinition() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition("fieldName", new ClassName("", ""));
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(classDefinition);

        assertThat(elementGeneratable, is(instanceOf(ElementGenerator.class)));
    }

    @Test
    public void shouldCreateElementGeneratorForEnumDefinition() throws Exception {
        final EnumDefinition enumDefinition = new EnumDefinition("fieldName", new ClassName("", ""), emptyList());
        final ElementGeneratable elementGeneratable = new JavaGeneratorFactory().createGeneratorFor(enumDefinition);

        assertThat(elementGeneratable, is(instanceOf(StringElementGenerator.class)));
    }
}
