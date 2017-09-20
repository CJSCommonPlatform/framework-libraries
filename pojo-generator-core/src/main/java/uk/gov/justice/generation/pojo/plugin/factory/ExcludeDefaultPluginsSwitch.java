package uk.gov.justice.generation.pojo.plugin.factory;

import static java.lang.Boolean.valueOf;

import java.util.Map;

public class ExcludeDefaultPluginsSwitch {

    private static final String EXCLUDE_DEFAULT_PROPERTY = "excludeDefaultPlugins";

    public boolean shouldExcludeDefaultPlugins(final Map<String, String> generatorProperties) {
        if (generatorProperties.containsKey(EXCLUDE_DEFAULT_PROPERTY)) {
            return valueOf(generatorProperties.get(EXCLUDE_DEFAULT_PROPERTY));
        }

        return false;
    }
}
