package uk.gov.justice.generation.pojo.generators.plugin.classgenerator;

import static com.squareup.javapoet.FieldSpec.builder;
import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.FieldDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.FieldGenerator;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

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
public class FieldAndMethodPluginTest {

    @Mock
    private JavaGeneratorFactory generatorFactory;

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private PluginContext pluginContext;

    @Test
    public void shouldGenerateTypeSpecForClassDefinitionWithNoFields() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        when(pluginContext.getJavaGeneratorFactory()).thenReturn(generatorFactory);

        new FieldAndMethodPlugin()
                .generateWith(
                        typeSpecBuilder,
                        classDefinition,
                        pluginContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.annotations.isEmpty(), is(true));
        assertThat(typeSpec.name, is("ClassName"));
        assertThat(typeSpec.fieldSpecs.size(), is(0));
        assertThat(typeSpec.methodSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs, hasItem(constructorBuilder().addModifiers(PUBLIC).build()));
    }

    @Test
    public void shouldGenerateTypeSpecForClassDefinitionWithOneField() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        final FieldDefinition fieldDefinition = new FieldDefinition(STRING, "field");
        classDefinition.addFieldDefinition(fieldDefinition);

        final FieldSpec fieldSpec = builder(String.class, "field").build();
        final MethodSpec methodSpec = methodBuilder("getField")
                .addModifiers(PUBLIC)
                .returns(String.class)
                .addCode(CodeBlock.builder().addStatement("return $L", "field").build())
                .build();

        final FieldGenerator fieldGenerator = mock(FieldGenerator.class);

        when(pluginContext.getJavaGeneratorFactory()).thenReturn(generatorFactory);
        when(pluginContext.getClassNameFactory()).thenReturn(classNameFactory);
        when(generatorFactory.createGeneratorFor(fieldDefinition)).thenReturn(fieldGenerator);
        when(fieldGenerator.generateField()).thenReturn(fieldSpec);
        when(fieldGenerator.generateMethods()).thenReturn(Stream.of(methodSpec));
        when(classNameFactory.createTypeNameFrom(fieldDefinition)).thenReturn(ClassName.get(String.class));

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new FieldAndMethodPlugin()
                .generateWith(
                        typeSpecBuilder,
                        classDefinition,
                        pluginContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.name, is("ClassName"));
        assertThat(typeSpec.fieldSpecs.size(), is(1));
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
    public void shouldGenerateTypeSpecForClassDefinitionWithAdditionalProperties() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        classDefinition.setAllowAdditionalProperties(true);

        when(pluginContext.getJavaGeneratorFactory()).thenReturn(generatorFactory);

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new FieldAndMethodPlugin()
                .generateWith(
                        typeSpecBuilder,
                        classDefinition,
                        pluginContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.annotations.isEmpty(), is(true));
        assertThat(typeSpec.name, is("ClassName"));
        assertThat(typeSpec.fieldSpecs.size(), is(1));

        assertThat(typeSpec.fieldSpecs.get(0).name, is("additionalProperties"));

        assertThat(typeSpec.methodSpecs.size(), is(3));
        assertThat(typeSpec.methodSpecs.get(0), is(constructorBuilder().addModifiers(PUBLIC).build()));

        final MethodSpec additionalPropertiesGetter = typeSpec.methodSpecs.get(1);
        final MethodSpec additionalPropertiesSetter = typeSpec.methodSpecs.get(2);

        assertThat(additionalPropertiesGetter.name, is("getAdditionalProperties"));
        assertThat(additionalPropertiesSetter.name, is("setAdditionalProperty"));
    }
}
