package uk.gov.justice.generation.pojo.core;

import org.apache.maven.plugins.annotations.Parameter;

public class TypeMapping {

    @Parameter(required = true)
    private String name;

    @Parameter(required = true)
    private String type;

    @Parameter(required = true)
    private String implementation;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImplementation() {
        return implementation;
    }
}
