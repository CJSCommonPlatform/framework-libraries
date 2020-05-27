package uk.gov.justice.generation.pojo.core;

import static java.util.stream.Collectors.toMap;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.maven.plugins.annotations.Parameter;

public class PojoGeneratorProperties implements GeneratorProperties {

    @Parameter(defaultValue = "false")
    private boolean excludeDefaultPlugins = false;

    @Parameter
    private List<String> plugins;

    @Parameter
    private String rootClassName;

    @Parameter
    private List<TypeMapping> typeMappings = new ArrayList<>();

    public boolean isExcludeDefaultPlugins() {
        return excludeDefaultPlugins;
    }

    public Optional<List<String>> getPlugins() {
        return Optional.ofNullable(plugins);
    }

    public Optional<String> getRootClassName() {
        return Optional.ofNullable(rootClassName);
    }

    public Map<String, String> typeMappingsFilteredBy(final Predicate<TypeMapping> mappingType) {
        return typeMappings.stream()
                .filter(mappingType)
                .collect(toMap(TypeMapping::getName, TypeMapping::getImplementation));
    }
}
