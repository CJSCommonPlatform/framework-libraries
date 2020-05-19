package uk.gov.justice.maven.generator.io.files.parser.generator.property;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorProperties;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

public class TestGeneratorProperties implements GeneratorProperties {

    @Parameter
    private String property1;

    @Parameter
    private String property2;

    @Parameter
    private List<String> nestedProperty;

    public String getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }

    public List<String> getNestedProperty() {
        return nestedProperty;
    }
}
