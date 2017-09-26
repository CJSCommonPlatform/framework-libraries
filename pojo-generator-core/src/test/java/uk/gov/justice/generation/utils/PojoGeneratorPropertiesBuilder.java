package uk.gov.justice.generation.utils;

import static uk.gov.justice.generation.utils.ReflectionUtil.setField;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;

import java.util.List;
import java.util.Map;

public class PojoGeneratorPropertiesBuilder {

    private boolean excludeDefaultPlugins;
    private List<String> plugins;
    private String rootClassName;
    private Map<String, String> typeMappings;

    public static PojoGeneratorPropertiesBuilder pojoGeneratorPropertiesBuilder() {
        return new PojoGeneratorPropertiesBuilder();
    }

    public PojoGeneratorPropertiesBuilder withExcludeDefaultPlugins(final boolean excludeDefaultPlugins) {
        this.excludeDefaultPlugins = excludeDefaultPlugins;
        return this;
    }

    public PojoGeneratorPropertiesBuilder withPlugins(final List<String> plugins) {
        this.plugins = plugins;
        return this;
    }

    public PojoGeneratorPropertiesBuilder withRootClassName(final String rootClassName) {
        this.rootClassName = rootClassName;
        return this;
    }

    public PojoGeneratorPropertiesBuilder withTypeMappings(final Map<String, String> typeMappings) {
        this.typeMappings = typeMappings;
        return this;
    }

    public PojoGeneratorProperties build() throws IllegalAccessException {
        final PojoGeneratorProperties pojoGeneratorProperties = new PojoGeneratorProperties();

        setField(pojoGeneratorProperties, "excludeDefaultPlugins", excludeDefaultPlugins);
        setField(pojoGeneratorProperties, "plugins", plugins);
        setField(pojoGeneratorProperties, "rootClassName", rootClassName);
        setField(pojoGeneratorProperties, "typeMappings", typeMappings);

        return pojoGeneratorProperties;
    }

}
