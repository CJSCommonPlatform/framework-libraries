package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.DefaultPluginProvider;
import uk.gov.justice.generation.pojo.generators.plugin.PluginProvider;

import java.util.stream.Stream;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassGeneratorTest {

    @Mock
    private JavaGeneratorFactory javaGeneratorFactory;

    @Mock
    private ClassNameFactory classNameFactory;

    @Test
    public void shouldGenerateTypeSpecForClassDefinitionWithNoFields() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");

        final ClassGenerator classGenerator = new ClassGenerator(classDefinition, javaGeneratorFactory, new DefaultPluginProvider(), classNameFactory);
        final TypeSpec typeSpec = classGenerator.generate();

        assertThat(typeSpec.annotations.isEmpty(), is(true));
        assertThat(typeSpec.name, is("Address"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs, hasItem(constructorBuilder().addModifiers(PUBLIC).build()));
    }

    @Test
    public void shouldGenerateTypeSpecForClassDefinitionWithOneField() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        final FieldDefinition fieldDefinition = new StringDefinition("field", null);
        classDefinition.addFieldDefinition(fieldDefinition);

        final FieldSpec fieldSpec = builder(String.class, "field").build();
        final MethodSpec methodSpec = methodBuilder("getField")
                .addModifiers(PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock.builder().addStatement("return $L", "field").build())
                .build();

        final FieldGenerator fieldGenerator = mock(FieldGenerator.class);

        when(javaGeneratorFactory.createGeneratorFor(fieldDefinition)).thenReturn(fieldGenerator);
        when(fieldGenerator.generateField()).thenReturn(fieldSpec);
        when(fieldGenerator.generateMethods()).thenReturn(Stream.of(methodSpec));
        when(classNameFactory.createClassNameFrom(fieldDefinition)).thenReturn(ClassName.get(String.class));

        final ClassGenerator classGenerator = new ClassGenerator(classDefinition, javaGeneratorFactory, new DefaultPluginProvider(), classNameFactory);
        final TypeSpec typeSpec = classGenerator.generate();

        assertThat(typeSpec.name, is("Address"));
        assertThat(typeSpec.modifiers.size(), is(1));
        assertThat(typeSpec.modifiers, hasItem(PUBLIC));
        assertThat(typeSpec.fieldSpecs.size(), is(2));
        assertThat(typeSpec.fieldSpecs, hasItem(fieldSpec));
        assertThat(typeSpec.methodSpecs.size(), is(2));
        assertThat(typeSpec.methodSpecs, hasItems(
                constructorBuilder()
                        .addModifiers(PUBLIC)
                        .addParameter(String.class, "field", FINAL)
                        .addStatement("this.field = field")
                        .build(),
                methodSpec)
        );
    }

    @Test
    public void shouldReturnClassName() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");

        final ClassGenerator classGenerator = new ClassGenerator(classDefinition, javaGeneratorFactory, mock(PluginProvider.class), classNameFactory);

        assertThat(classGenerator.getClassName(), is("Address"));
    }
}
