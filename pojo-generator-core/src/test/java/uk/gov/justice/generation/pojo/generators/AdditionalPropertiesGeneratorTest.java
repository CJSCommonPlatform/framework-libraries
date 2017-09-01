package uk.gov.justice.generation.pojo.generators;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.squareup.javapoet.MethodSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdditionalPropertiesGeneratorTest {

    @InjectMocks
    private AdditionalPropertiesGenerator additionalPropertiesGenerator;

    @Test
    public void shouldGenerateTheCorrectJavaForTheAdditionalPropertiesFieldDeclaration() throws Exception {

        final String expectedField = "private final java.util.Map" +
                "<java.lang.String, java.lang.Object> additionalProperties " +
                "= new java.util.HashMap<>();\n";

        assertThat(additionalPropertiesGenerator.generateField().toString(), is(expectedField));
    }

    @Test
    public void shouldGenerateTheCorrectGetter() throws Exception {

        final String expectedGetter = "@com.fasterxml.jackson.annotation.JsonAnyGetter\n" +
                "public java.util.Map<java.lang.String, java.lang.Object> getAdditionalProperties() {\n" +
                "  return additionalProperties;\n" +
                "}\n";

        final String expectedSetter = "@com.fasterxml.jackson.annotation.JsonAnySetter\n" +
                "public void setAdditionalProperty(" +
                    "final java.lang.String name, " +
                    "final java.lang.Object value) {\n" +
                    "  additionalProperties.put(name, value);\n" +
                "}\n";

        final List<MethodSpec> methodSpecs = additionalPropertiesGenerator.generateMethods().collect(toList());

        assertThat(methodSpecs.size(), is(2));
        assertThat(methodSpecs.get(0).toString(), is(expectedGetter));
        assertThat(methodSpecs.get(1).toString(), is(expectedSetter));
    }
}
