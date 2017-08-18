package uk.gov.justice.generation.pojo.generators;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.junit.Test;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;

import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringElementGeneratorTest {
    final ClassName className = new ClassName("org.something", "Address");
    final ClassDefinition classDefinition = new ClassDefinition("addressLine1", className);
    final StringElementGenerator stringElementGenerator = new StringElementGenerator(classDefinition);

    @Test
    public void shouldGenerateField() {
        final FieldSpec fieldSpec = stringElementGenerator.generateField();
        assertThat(fieldSpec.name, is("addressLine1"));
    }

    @Test
    public void shouldGenerateMethods() {
        final Stream<MethodSpec> methods = stringElementGenerator.generateMethods();
        assertThat(methods.findFirst().get().name, is("getAddress"));
    }

}