package uk.gov.justice.generation.pojo.generators;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import org.junit.Test;

public class SourceCodeGeneratorFactoryTest {

    @Test
    public void shouldCreateFieldGeneratorForFieldDefinition() throws Exception {
        final FieldDefinition fieldDefinition = new FieldDefinition("fieldName", new ClassName(String.class));
        final SourceCodeGenerator sourceCodeGenerator = new SourceCodeGeneratorFactory().createFor(fieldDefinition);

        assertThat(sourceCodeGenerator, is(instanceOf(FieldGenerator.class)));
    }

    @Test
    public void shouldCreateClassGeneratorForClassDefinition() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition("fieldName", new ClassName("", ""));
        final SourceCodeGenerator sourceCodeGenerator = new SourceCodeGeneratorFactory().createFor(classDefinition);

        assertThat(sourceCodeGenerator, is(instanceOf(ClassGenerator.class)));
    }
}
