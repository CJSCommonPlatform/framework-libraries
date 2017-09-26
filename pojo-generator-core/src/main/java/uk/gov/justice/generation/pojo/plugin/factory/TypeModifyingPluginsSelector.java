package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;
import java.util.Map;

public class TypeModifyingPluginsSelector {

    private static final Class<TypeModifyingPlugin> TYPE_MODIFYING_PLUGIN = TypeModifyingPlugin.class;

    public List<TypeModifyingPlugin> selectFrom(final Map<Class<?>, List<Plugin>> pluginTypes) {
        if (pluginTypes.containsKey(TYPE_MODIFYING_PLUGIN)) {
            return pluginTypes.get(TYPE_MODIFYING_PLUGIN)
                    .stream()
                    .map(plugin -> (TypeModifyingPlugin) plugin)
                    .collect(toList());
        }

        return emptyList();
    }

}
