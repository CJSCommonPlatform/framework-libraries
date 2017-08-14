package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;

import java.util.List;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.junit.Test;

public class ElementGeneratorTest {

    @Test
    public void shouldGenerateField() throws Exception {
        final ClassName className = new ClassName("org.something", "Address");
        final ClassDefinition classDefinition = new ClassDefinition("address", className);

        final ElementGenerator elementGenerator = new ElementGenerator(classDefinition);
        final FieldSpec fieldSpec = elementGenerator.generateField();

        assertThat(fieldSpec, is(builder(get("org.something", "Address"), "address", PRIVATE, FINAL).build()));
    }

    @Test
    public void shouldGenerateMethod() throws Exception {
        final ClassName className = new ClassName("org.something", "Address");
        final ClassDefinition classDefinition = new ClassDefinition("address", className);

        final ElementGenerator elementGenerator = new ElementGenerator(classDefinition);
        final List<MethodSpec> methodSpecs = elementGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs, hasItem(methodBuilder("getAddress")
                .addModifiers(PUBLIC)
                .returns(get("org.something", "Address"))
                .addCode(CodeBlock.builder().addStatement("return $L", "address").build())
                .build()));
    }
}