package uk.gov.justice.generation.pojo.generators.plugin.builder;

import static com.squareup.javapoet.ClassName.get;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BuilderMethodFactoryTest {


    @InjectMocks
    private BuilderMethodFactory builderMethodFactory;

    @Test
    public void shouldCreateTheBuildMethod() throws Exception {

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);

        final ClassName pojoClassName = get("org.bloggs.fred", "AlcubierreDrive");

        when(fieldDefinition_1.getFieldName()).thenReturn("fieldDefinition_1");
        when(fieldDefinition_2.getFieldName()).thenReturn("fieldDefinition_2");

        final MethodSpec theBuildMethod = builderMethodFactory.createTheBuildMethod(fieldDefinitions, pojoClassName);

        final String expectedBuildMethod =
                "public org.bloggs.fred.AlcubierreDrive build() {\n  " +
                        "return new org.bloggs.fred.AlcubierreDrive(fieldDefinition_1, fieldDefinition_2);\n" +
                 "}\n";

        assertThat(theBuildMethod.toString(), is(expectedBuildMethod));
    }

    @Test
    public void shouldCreateTheWithMethods() throws Exception {

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);

        final ClassName builderClassName = get("org.bloggs.fred", "AlcubierreDrive").nestedClass("Builder");

        when(fieldDefinition_1.getFieldName()).thenReturn("fieldDefinition_1");
        when(fieldDefinition_2.getFieldName()).thenReturn("fieldDefinition_2");

        when(classNameFactory.createTypeNameFrom(fieldDefinition_1)).thenReturn(get(String.class));
        when(classNameFactory.createTypeNameFrom(fieldDefinition_2)).thenReturn(get(Integer.class));

        final List<MethodSpec> withMethods = builderMethodFactory.createTheWithMethods(fieldDefinitions, classNameFactory, builderClassName);

        assertThat(withMethods.size(), is(2));

        final String expectedWithMethod_1 =
                "public org.bloggs.fred.AlcubierreDrive.Builder withFieldDefinition_1(" +
                        "final java.lang.String fieldDefinition_1) {\n  " +
                            "this.fieldDefinition_1 = fieldDefinition_1;\n  " +
                            "return this;\n" +
                        "}\n";
        final String expectedWithMethod_2 =
                "public org.bloggs.fred.AlcubierreDrive.Builder withFieldDefinition_2(" +
                        "final java.lang.Integer fieldDefinition_2) {\n  " +
                            "this.fieldDefinition_2 = fieldDefinition_2;\n  " +
                            "return this;\n" +
                        "}\n";

        assertThat(withMethods.get(0).toString(), is(expectedWithMethod_1));
        assertThat(withMethods.get(1).toString(), is(expectedWithMethod_2));
    }
}
