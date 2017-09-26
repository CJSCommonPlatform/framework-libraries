package uk.gov.justice.generation.pojo.plugin.factory;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.PluginProviderException;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;
import java.util.Map;

public class PluginTypeSorter {

    private static final Class<ClassModifyingPlugin> CLASS_MODIFYING_PLUGIN = ClassModifyingPlugin.class;
    private static final Class<TypeModifyingPlugin> TYPE_MODIFYING_PLUGIN = TypeModifyingPlugin.class;
    private static final Class<NameGeneratablePlugin> NAME_GENERATABLE_PLUGIN = NameGeneratablePlugin.class;

    private static final String INCORRECT_CLASS_TYPE_MESSAGE = "Incorrect Class Type, Class name: %s, does not implement ClassModifyingPlugin or TypeModifyingPlugin or NameGeneratablePlugin.";

    public Map<Class<?>, List<Plugin>> sortByType(final List<Plugin> plugins) {

        return plugins
                .stream()
                .collect(groupingBy(this::groupByPluginInterface));
    }

    private Class<?> groupByPluginInterface(final Object plugin) {

        final List<Class<?>> classList = asList(plugin.getClass().getInterfaces());

        if (classList.contains(CLASS_MODIFYING_PLUGIN)) {
            return CLASS_MODIFYING_PLUGIN;
        }

        if (classList.contains(TYPE_MODIFYING_PLUGIN)) {
            return TYPE_MODIFYING_PLUGIN;
        }

        if (classList.contains(NAME_GENERATABLE_PLUGIN)) {
            return NAME_GENERATABLE_PLUGIN;
        }

        throw new PluginProviderException(format(INCORRECT_CLASS_TYPE_MESSAGE, plugin.getClass().getName()));
    }
}
