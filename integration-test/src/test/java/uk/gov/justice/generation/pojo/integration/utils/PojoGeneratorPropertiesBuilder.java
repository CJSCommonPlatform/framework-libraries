package uk.gov.justice.generation.pojo.integration.utils;

import static uk.gov.justice.generation.pojo.integration.utils.TypeMappingFactory.typeMappingOf;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.core.TypeMapping;

import java.util.ArrayList;
import java.util.List;

public class PojoGeneratorPropertiesBuilder {

    private boolean excludeDefaultPlugins;
    private List<String> plugins;
    private String rootClassName;
    private List<TypeMapping> typeMappings = new ArrayList<>();

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

    public PojoGeneratorPropertiesBuilder addReferenceTypeMappingOf(final String name, final String implementation) throws IllegalAccessException {
        typeMappings.add(typeMappingOf("reference", name, implementation));
        return this;
    }

    public PojoGeneratorPropertiesBuilder addFormatTypeMappingOf(final String name, final String implementation) throws IllegalAccessException {
        typeMappings.add(typeMappingOf("format", name, implementation));
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
