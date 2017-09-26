package uk.gov.justice.generation.pojo.plugin.factory;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;

import java.util.Collections;
import java.util.List;

public class PluginsFromClassnameListFactory {

    public List<String> parsePluginNames(final PojoGeneratorProperties generatorProperties) {
        return generatorProperties.getPlugins().orElseGet(Collections::emptyList);
    }
}
