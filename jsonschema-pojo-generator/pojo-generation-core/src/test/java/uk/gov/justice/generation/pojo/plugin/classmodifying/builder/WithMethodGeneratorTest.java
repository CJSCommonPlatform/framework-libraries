package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.ClassName.get;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class WithMethodGeneratorTest {

    @Mock
    private OptionalTypeNameUtil optionalTypeNameUtil;

    @InjectMocks
    private WithMethodGenerator withMethodgenerator;

    @Test
    public void shouldCreateTheBuildersWithMethod() throws Exception {

        final String fieldName = "firstName";

        final boolean isOptionalType = false;
        final TypeName rawTypeName = TypeName.get(String.class);

        final Definition fieldDefinition = mock(Definition.class);
        final ClassName builderClassName = get("org.bloggs.fred", "AlcubierreDriveBuilder");
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(fieldDefinition.getFieldName()).thenReturn(fieldName);
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(rawTypeName);
        when(optionalTypeNameUtil.isOptionalType(rawTypeName)).thenReturn(isOptionalType);

        final List<MethodSpec> methodSpecs = withMethodgenerator.generateWithMethods(
                fieldDefinition,
                builderClassName,
                classNameFactory,
                pluginContext
        );

        assertThat(methodSpecs.size(), is(1));

        final String expectedWithMethod =
                "public org.bloggs.fred.AlcubierreDriveBuilder withFirstName(final java.lang.String firstName) {\n" +
                        "  this.firstName = firstName;\n" +
                        "  return this;\n" +
                        "}\n";


        assertThat(methodSpecs.get(0).toString(), is(expectedWithMethod));
    }


    @Test
    public void shouldOverloadTheWithMethodIfTheFieldIsOptional() throws Exception {

        final String fieldName = "firstName";

        final boolean isOptionalType = true;
        final TypeName rawTypeName = TypeName.get(String.class);
        final ParameterizedTypeName optionalTypeName = ParameterizedTypeName.get(get(Optional.class), rawTypeName);

        final Definition fieldDefinition = mock(Definition.class);
        final ClassName builderClassName = get("org.bloggs.fred", "AlcubierreDriveBuilder");
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(fieldDefinition.getFieldName()).thenReturn(fieldName);
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(optionalTypeName);
        when(optionalTypeNameUtil.isOptionalType(optionalTypeName)).thenReturn(isOptionalType);

        final List<MethodSpec> methodSpecs = withMethodgenerator.generateWithMethods(
                fieldDefinition,
                builderClassName,
                classNameFactory,
                pluginContext
        );

        assertThat(methodSpecs.size(), is(2));

        final String expectedWithMethod =
                "public org.bloggs.fred.AlcubierreDriveBuilder withFirstName(final java.lang.String firstName) {\n" +
                        "  this.firstName = firstName;\n" +
                        "  return this;\n" +
                        "}\n";

        final String overloadedBuildMethod =
                "public org.bloggs.fred.AlcubierreDriveBuilder withFirstName(final java.util.Optional<java.lang.String> firstName) {" +
                        "\n  if (firstName != null) {\n" +
                        "    this.firstName = firstName.orElse(null);\n" +
                        "  }\n" +
                        "  return this;\n}\n";

        assertThat(methodSpecs.get(0).toString(), is(expectedWithMethod));
        assertThat(methodSpecs.get(1).toString(), is(overloadedBuildMethod));
    }
}
