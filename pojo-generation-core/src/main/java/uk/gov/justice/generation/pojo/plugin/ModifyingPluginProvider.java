package uk.gov.justice.generation.pojo.plugin;

import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;

/**
 * The standard implementation of {@link PluginProvider} that takes a List of {@link
 * ClassModifyingPlugin} and a List of {@link TypeModifyingPlugin}.
 */
public class ModifyingPluginProvider implements PluginProvider {

    private final List<ClassModifyingPlugin> classModifyingPlugins;
    private final List<TypeModifyingPlugin> typeModifyingPlugins;

    private static final Class<SupportJavaOptionalsPlugin> OPTIONALS_PLUGIN_CLASS = SupportJavaOptionalsPlugin.class;

    /**
     * Constructor to take a List of {@link ClassModifyingPlugin}, a List of {@link
     * TypeModifyingPlugin}.
     *
     * @param classModifyingPlugins the List of {@link ClassModifyingPlugin}
     * @param typeModifyingPlugins  the List of {@link TypeModifyingPlugin}
     */
    public ModifyingPluginProvider(final List<ClassModifyingPlugin> classModifyingPlugins,
                                   final List<TypeModifyingPlugin> typeModifyingPlugins) {
        this.classModifyingPlugins = classModifyingPlugins;
        this.typeModifyingPlugins = typeModifyingPlugins;
    }

    @Override
    public List<ClassModifyingPlugin> classModifyingPlugins() {
        return classModifyingPlugins;
    }

    @Override
    public List<TypeModifyingPlugin> typeModifyingPlugins() {
        return moveOptionalPluginToTheEndOfTheList();
    }

    private List<TypeModifyingPlugin> moveOptionalPluginToTheEndOfTheList() {
        for (int i = 0; i < typeModifyingPlugins.size(); i++) {
            final TypeModifyingPlugin typeModifyingPlugin = typeModifyingPlugins.get(i);
            if (typeModifyingPlugin.getClass().isAssignableFrom(OPTIONALS_PLUGIN_CLASS)) {
                typeModifyingPlugins.remove(i);
                typeModifyingPlugins.add(typeModifyingPlugin);
            }
        }
        return typeModifyingPlugins;
    }
}
