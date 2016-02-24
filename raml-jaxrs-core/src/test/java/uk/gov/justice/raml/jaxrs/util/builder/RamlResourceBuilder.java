package uk.gov.justice.raml.jaxrs.util.builder;

import static java.text.MessageFormat.format;

import java.util.LinkedList;
import java.util.List;

public class RamlResourceBuilder {

    private static String RAML_RESOURCE_STRING_TEMPLATE = "{0}:\r\n" +
            "{1}";
    private String path = "/default/path";
    private List<RamlResourceMethodBuilder> methods = new LinkedList<>();

    public static RamlResourceBuilder aResource() {
        return new RamlResourceBuilder();
    }

    public RamlResourceBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public RamlResourceBuilder with(RamlResourceMethodBuilder method) {
        methods.add(method);
        return this;
    }

    public String toString() {
        StringBuilder methodsString = new StringBuilder();
        methods.forEach(m -> methodsString.append(m.toString()));
        return format(RAML_RESOURCE_STRING_TEMPLATE, path, methodsString);
    }
}
