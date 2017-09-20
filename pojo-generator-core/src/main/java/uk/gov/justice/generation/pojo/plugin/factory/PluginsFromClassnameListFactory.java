package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;
import java.util.Map;

public class PluginsFromClassnameListFactory {

    private static final String PLUGINS_PROPERTY = "plugins";

    public List<String> parsePluginNames(final Map<String, String> generatorProperties) {

        if (generatorProperties.containsKey(PLUGINS_PROPERTY)) {
            final String pluginValues = generatorProperties.get(PLUGINS_PROPERTY);

            if (isNotEmpty(pluginValues)) {
                return asList(pluginValues.split(","));
            }
        }

        return emptyList();
    }
}
