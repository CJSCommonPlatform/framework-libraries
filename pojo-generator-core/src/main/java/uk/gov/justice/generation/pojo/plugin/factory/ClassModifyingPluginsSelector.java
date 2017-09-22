package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ClassModifyingPluginsSelector {

    private static final Class<ClassModifyingPlugin> CLASS_MODIFYING_PLUGIN = ClassModifyingPlugin.class;

    private final ExcludeDefaultPluginsSwitch excludeDefaultPluginsSwitch;
    private final DefaultPluginsProvider defaultPluginsProvider;

    public ClassModifyingPluginsSelector(
            final ExcludeDefaultPluginsSwitch excludeDefaultPluginsSwitch,
            final DefaultPluginsProvider defaultPluginsProvider) {
        this.excludeDefaultPluginsSwitch = excludeDefaultPluginsSwitch;
        this.defaultPluginsProvider = defaultPluginsProvider;
    }

    public List<ClassModifyingPlugin> selectFrom(final Map<Class<?>, List<Object>> pluginTypes, final Map<String, String> generatorProperties) {
        return concat(
                defaultClassModifyingPlugins(generatorProperties),
                userDefinedClassModifyingPlugins(pluginTypes)
        ).collect(toList());
    }

    private Stream<ClassModifyingPlugin> userDefinedClassModifyingPlugins(final Map<Class<?>, List<Object>> pluginTypes) {
        if (pluginTypes.containsKey(CLASS_MODIFYING_PLUGIN)) {
            return pluginTypes.get(CLASS_MODIFYING_PLUGIN)
                    .stream()
                    .map(plugin -> (ClassModifyingPlugin) plugin);
        }

        return Stream.empty();
    }

    private Stream<ClassModifyingPlugin> defaultClassModifyingPlugins(final Map<String, String> generatorProperties) {
        if (excludeDefaultPluginsSwitch.shouldExcludeDefaultPlugins(generatorProperties)) {
            return Stream.empty();
        }

        return defaultPluginsProvider.getDefaultPlugins().stream();
    }
}
