package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;

import java.util.List;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.junit.Test;

public class FieldGeneratorTest {

    @Test
    public void shouldGenerateField() throws Exception {
        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassName className = new ClassName(String.class);

        when(fieldDefinition.getClassName()).thenReturn(className);
        when(fieldDefinition.getFieldName()).thenReturn("firstName");
        when(fieldDefinition.getGenericType()).thenReturn(empty());

        final FieldGenerator fieldGenerator = new FieldGenerator(fieldDefinition);
        final FieldSpec fieldSpec = fieldGenerator.generateField();

        assertThat(fieldSpec, is(builder(String.class, "firstName", PRIVATE, FINAL).build()));
    }

    @Test
    public void shouldGenerateMethod() throws Exception {
        final FieldDefinition fieldDefinition = mock(FieldDefinition.class);
        final ClassName className = new ClassName(String.class);

        when(fieldDefinition.getClassName()).thenReturn(className);
        when(fieldDefinition.getFieldName()).thenReturn("firstName");
        when(fieldDefinition.getGenericType()).thenReturn(empty());

        final FieldGenerator fieldGenerator = new FieldGenerator(fieldDefinition);
        final List<MethodSpec> methodSpecs = fieldGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs, hasItem(MethodSpec.methodBuilder("getFirstName")
                .addModifiers(PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock.builder().addStatement("return $L", "firstName").build())
                .build()));
    }
}
