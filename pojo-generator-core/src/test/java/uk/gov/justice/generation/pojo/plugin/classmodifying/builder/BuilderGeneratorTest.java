package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.ClassName.get;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuilderGeneratorTest {

    @Mock
    private ClassDefinition classDefinition;

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private BuilderMethodFactory builderMethodFactory;

    @Mock
    private BuilderFieldFactory builderFieldFactory;

    @Mock
    private AdditionalPropertiesDeterminer additionalPropertiesDeterminer;

    @Mock
    private PluginContext pluginContext;

    @InjectMocks
    private BuilderGenerator builderGenerator;

    @Test
    public void shouldGenerateABuilderWithTheCorrectFieldsBuildMethodAndWithMethods() throws Exception {

        final String fieldName = "alcubierreDrive";
        final String packageName = "org.bloggs.fred";
        final String pojoSimpleName = capitalize(fieldName);
        final ClassName pojoClassName = get(packageName, pojoSimpleName);
        final ClassName builderClassName = pojoClassName.nestedClass("Builder");

        final List<Definition> fieldDefinitions = singletonList(mock(Definition.class));

        final List<FieldSpec> fieldSpecs = singletonList(FieldSpec.builder(get(String.class), "captainsLog", PUBLIC).build());
        final MethodSpec withMethod = MethodSpec.methodBuilder("withCaptainsLog").build();
        final MethodSpec buildMethod = MethodSpec.methodBuilder("build").build();

        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(pojoClassName);
        when(classDefinition.getFieldName()).thenReturn(fieldName);
        when(classDefinition.getFieldDefinitions()).thenReturn(fieldDefinitions);

        when(builderFieldFactory.createFields(
                fieldDefinitions,
                classNameFactory,
                pluginContext)).thenReturn(fieldSpecs);
        when(builderMethodFactory.createTheWithMethods(
                fieldDefinitions,
                classNameFactory,
                builderClassName,
                pluginContext)).thenReturn(singletonList(withMethod));


        when(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)).thenReturn(false);
        when(builderMethodFactory.createTheBuildMethod(fieldDefinitions, pojoClassName)).thenReturn(buildMethod);

        final TypeSpec builder = builderGenerator.generate();

        assertThat(builder.name, is("Builder"));

        assertThat(builder.modifiers.size(), is(2));
        assertThat(builder.modifiers, hasItem(PUBLIC));
        assertThat(builder.modifiers, hasItem(STATIC));

        assertThat(builder.fieldSpecs, is(fieldSpecs));
        assertThat(builder.methodSpecs.size(), is(2));
        assertThat(builder.methodSpecs, hasItem(withMethod));
        assertThat(builder.methodSpecs, hasItem(buildMethod));
    }

    @Test
    public void shouldGenerateABuilderWithAdditionalPropertiesIfRequired() throws Exception {

        final String fieldName = "alcubierreDrive";
        final String packageName = "org.bloggs.fred";
        final String pojoSimpleName = capitalize(fieldName);
        final ClassName pojoClassName = get(packageName, pojoSimpleName);
        final ClassName builderClassName = pojoClassName.nestedClass("Builder");

        final List<Definition> fieldDefinitions = singletonList(mock(Definition.class));

        final FieldSpec captainsLogField = FieldSpec.builder(get(String.class), "captainsLog", PRIVATE).build();
        final List<FieldSpec> fieldSpecs = singletonList(captainsLogField);
        final MethodSpec withMethod = MethodSpec.methodBuilder("withCaptainsLog").build();
        final MethodSpec buildMethodWithAdditionalProperties = MethodSpec.methodBuilder("build").build();

        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(pojoClassName);
        when(classDefinition.getFieldName()).thenReturn(fieldName);
        when(classDefinition.getFieldDefinitions()).thenReturn(fieldDefinitions);
        when(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)).thenReturn(true);

        when(builderFieldFactory.createFields(
                fieldDefinitions,
                classNameFactory,
                pluginContext)).thenReturn(fieldSpecs);
        when(builderMethodFactory.createTheWithMethodsWithAdditionalProperties(
                fieldDefinitions,
                classNameFactory,
                builderClassName,
                pluginContext)).thenReturn(singletonList(withMethod));


        when(additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)).thenReturn(true);
        when(builderMethodFactory.createTheBuildMethodWithAdditionalProperties(fieldDefinitions, pojoClassName)).thenReturn(buildMethodWithAdditionalProperties);

        final TypeSpec builder = builderGenerator.generate();

        assertThat(builder.name, is("Builder"));

        assertThat(builder.modifiers.size(), is(2));
        assertThat(builder.modifiers, hasItem(PUBLIC));
        assertThat(builder.modifiers, hasItem(STATIC));

        assertThat(builder.fieldSpecs.size(), is(2));

        assertThat(builder.fieldSpecs.get(0).toString(), is("private java.lang.String captainsLog;\n"));
        assertThat(builder.fieldSpecs.get(1).toString(), is("private final java.util.Map<java.lang.String, java.lang.Object> additionalProperties = new java.util.HashMap<java.lang.String, java.lang.Object>();\n"));

        builder.methodSpecs.forEach(System.out::println);
        assertThat(builder.methodSpecs.size(), is(2));
        assertThat(builder.methodSpecs, hasItem(withMethod));
        assertThat(builder.methodSpecs, hasItem(buildMethodWithAdditionalProperties));
    }

    @Test
    public void shouldGetTheCorrectSimpleNameForTheBuilder() throws Exception {

        assertThat(builderGenerator.getSimpleClassName(), is("Builder"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionWhenRequestingPackageNameForTheBuilder() throws Exception {

        builderGenerator.getPackageName();
    }

    @Test
    public void shouldGenerateStaticGetBuilderMethod() throws Exception {

        final String fieldName = "alcubierreDrive";
        final String packageName = "org.bloggs.fred";
        final String pojoSimpleName = capitalize(fieldName);
        final ClassName pojoClassName = get(packageName, pojoSimpleName);
        final ClassName pojoBuilder = pojoClassName.nestedClass("Builder");

        when(classDefinition.getFieldName()).thenReturn(fieldName);
        when(classNameFactory.createClassNameFrom(classDefinition)).thenReturn(pojoClassName);

        final MethodSpec methodSpec = builderGenerator.generateStaticGetBuilderMethod();

        assertThat(methodSpec.modifiers.size(), is(2));
        assertThat(methodSpec.modifiers, hasItem(PUBLIC));
        assertThat(methodSpec.modifiers, hasItem(STATIC));

        assertThat(methodSpec.returnType, is(pojoBuilder));
        assertThat(methodSpec.code.toString().trim(), is(format("return new %s();", pojoBuilder)));
    }
}
