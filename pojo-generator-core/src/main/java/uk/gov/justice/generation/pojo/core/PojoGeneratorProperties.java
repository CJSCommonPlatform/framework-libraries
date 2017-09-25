package uk.gov.justice.generation.pojo.core;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.plugins.annotations.Parameter;

public class PojoGeneratorProperties implements GeneratorProperties {

    @Parameter(defaultValue = "false")
    private boolean excludeDefaultPlugins = false;

    @Parameter
    private List<String> plugins;

    @Parameter
    private String rootClassName;

    @Parameter
    private Map<String, String> typeMappings;

    public boolean isExcludeDefaultPlugins() {
        return excludeDefaultPlugins;
    }

    public Optional<List<String>> getPlugins() {
        return Optional.ofNullable(plugins);
    }

    public Optional<String> getRootClassName() {
        return Optional.ofNullable(rootClassName);
    }

    public Map<String, String> getTypeMappings() {
        if (null == typeMappings) {
            typeMappings = new HashMap<>();
        }

        return typeMappings;
    }
}
