package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.ClassName.get;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BuilderMethodFactoryTest {

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private OptionalTypeNameUtil optionalTypeNameUtil;

    @Mock
    private WithMethodGenerator withMethodGenerator;

    @InjectMocks
    private BuilderMethodFactory builderMethodFactory;

    @Test
    public void shouldCreateTheBuildMethod() throws Exception {

        final PluginContext pluginContext = mock(PluginContext.class);
        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);

        final ClassName pojoClassName = get("org.bloggs.fred", "AlcubierreDrive");

        when(fieldDefinition_1.getFieldName()).thenReturn("fieldDefinition_1");
        when(fieldDefinition_2.getFieldName()).thenReturn("fieldDefinition_2");

        final MethodSpec theBuildMethod = builderMethodFactory.createTheBuildMethod(fieldDefinitions, pojoClassName, pluginContext);

        final String expectedBuildMethod =
                "public org.bloggs.fred.AlcubierreDrive build() {\n  " +
                        "return new org.bloggs.fred.AlcubierreDrive(fieldDefinition_1, fieldDefinition_2);\n" +
                        "}\n";

        assertThat(theBuildMethod.toString(), is(expectedBuildMethod));
    }

    @Test
    public void shouldCreateBuildMethodWithOptional() throws Exception {

        final PluginContext pluginContext = mock(PluginContext.class);
        final Definition fieldDefinition = mock(Definition.class);
        final ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(get(Optional.class), get(String.class));

        final List<Definition> fieldDefinitions = singletonList(fieldDefinition);
        final ClassName pojoClassName = get("org.bloggs.fred", "AlcubierreDrive");

        when(fieldDefinition.getFieldName()).thenReturn("fieldDefinition");
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(parameterizedTypeName);
        when(optionalTypeNameUtil.isOptionalType(parameterizedTypeName)).thenReturn(true);

        final MethodSpec theBuildMethod = builderMethodFactory.createTheBuildMethod(fieldDefinitions, pojoClassName, pluginContext);

        final String expectedBuildMethod =
                "public org.bloggs.fred.AlcubierreDrive build() {\n  " +
                        "return new org.bloggs.fred.AlcubierreDrive(Optional.ofNullable(fieldDefinition));\n" +
                        "}\n";

        assertThat(theBuildMethod.toString(), is(expectedBuildMethod));
    }

    @Test
    public void shouldCreateTheBuildMethodWithAdditionalProperties() throws Exception {

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);
        final PluginContext pluginContext = mock(PluginContext.class);

        final ClassName pojoClassName = get("org.bloggs.fred", "AlcubierreDrive");

        when(fieldDefinition_1.getFieldName()).thenReturn("fieldDefinition_1");
        when(fieldDefinition_2.getFieldName()).thenReturn("fieldDefinition_2");

        final MethodSpec theBuildMethod = builderMethodFactory.createTheBuildMethodWithAdditionalProperties(
                fieldDefinitions,
                pojoClassName,
                pluginContext);

        final String expectedBuildMethod =
                "public org.bloggs.fred.AlcubierreDrive build() {\n  " +
                        "return new org.bloggs.fred.AlcubierreDrive(fieldDefinition_1, fieldDefinition_2, additionalProperties);\n" +
                        "}\n";

        assertThat(theBuildMethod.toString(), is(expectedBuildMethod));
    }

    @Test
    public void shouldCreateTheWithMethods() throws Exception {

        final ClassName pojoClassName = get("org.bloggs.fred", "AlcubierreDrive");
        
        final MethodSpec generatedWithMethod_1 = MethodSpec.methodBuilder("generatedWithMethod_1").build();
        final MethodSpec generatedWithMethod_2 = MethodSpec.methodBuilder("generatedWithMethod_2").build();
        final MethodSpec generatedWithValuesFromMethod = MethodSpec.methodBuilder("generatedWithValuesFromMethod").build();

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);

        final PluginContext pluginContext = mock(PluginContext.class);

        final ClassName builderClassName = get("org.bloggs.fred", "AlcubierreDrive").nestedClass("Builder");
        
        when(withMethodGenerator.generateWithMethods(
                fieldDefinition_1,
                builderClassName,
                classNameFactory,
                pluginContext)).thenReturn(singletonList(generatedWithMethod_1));
        when(withMethodGenerator.generateWithMethods(
                fieldDefinition_2,
                builderClassName,
                classNameFactory,
                pluginContext)).thenReturn(singletonList(generatedWithMethod_2));
        when(withMethodGenerator.generateWithValuesFromMethod(
                pojoClassName,
                builderClassName,
                fieldDefinitions,
                classNameFactory,
                pluginContext
        )).thenReturn(generatedWithValuesFromMethod);

        final List<MethodSpec> withMethods = builderMethodFactory.createTheWithMethods(
                fieldDefinitions,
                classNameFactory,
                pojoClassName,
                builderClassName,
                pluginContext);

        assertThat(withMethods.size(), is(3));
        assertThat(withMethods.get(0), is(generatedWithMethod_1));
        assertThat(withMethods.get(1), is(generatedWithMethod_2));
        assertThat(withMethods.get(2), is(generatedWithValuesFromMethod));
    }

    @Test
    public void shouldCreateMethodsWithAdditionalProperties() throws Exception {

        final MethodSpec generatedWithMethod_1 = MethodSpec.methodBuilder("generatedWithMethod_1").build();
        final MethodSpec generatedWithMethod_2 = MethodSpec.methodBuilder("generatedWithMethod_2").build();

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final ClassName className_1 = get(String.class);
        final ClassName className_2 = get(Integer.class);

        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);
        final ClassName builderClassName = get("org.bloggs.fred", "AlcubierreDrive").nestedClass("Builder");
        
        when(withMethodGenerator.generateWithMethods(
                fieldDefinition_1,
                builderClassName,
                classNameFactory,
                pluginContext)).thenReturn(singletonList(generatedWithMethod_1));
        when(withMethodGenerator.generateWithMethods(
                fieldDefinition_2,
                builderClassName,
                classNameFactory,
                pluginContext)).thenReturn(singletonList(generatedWithMethod_2));


        final List<MethodSpec> withMethods = builderMethodFactory.createTheWithMethodsWithAdditionalProperties(
                fieldDefinitions,
                classNameFactory,
                builderClassName,
                pluginContext);

        assertThat(withMethods.size(), is(3));


        final String expectedAdditionalPropertiesWithMethod =
                "public org.bloggs.fred.AlcubierreDrive.Builder withAdditionalProperty(" +
                        "final java.lang.String name, " +
                        "final java.lang.Object value) {\n  " +
                        "additionalProperties.put(name, value);\n  " +
                        "return this;\n" +
                        "}\n";

        assertThat(withMethods.get(0), is(generatedWithMethod_1));
        assertThat(withMethods.get(1), is(generatedWithMethod_2));
        assertThat(withMethods.get(2).toString(), startsWith(expectedAdditionalPropertiesWithMethod));
    }

    @Test
    public void shouldCreateTheBuildMethodWithOnlyAdditionalProperties() throws Exception {

        final List<Definition> fieldDefinitions = emptyList();
        final PluginContext pluginContext = mock(PluginContext.class);

        final ClassName pojoClassName = get("org.bloggs.fred", "AlcubierreDrive");

        final MethodSpec theBuildMethod = builderMethodFactory.createTheBuildMethodWithAdditionalProperties(
                fieldDefinitions,
                pojoClassName,
                pluginContext);

        final String expectedBuildMethod =
                "public org.bloggs.fred.AlcubierreDrive build() {\n  " +
                        "return new org.bloggs.fred.AlcubierreDrive(additionalProperties);\n" +
                        "}\n";

        assertThat(theBuildMethod.toString(), is(expectedBuildMethod));
    }
}
