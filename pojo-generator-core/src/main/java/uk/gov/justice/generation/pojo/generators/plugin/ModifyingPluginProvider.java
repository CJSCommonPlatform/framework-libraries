package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;

/**
 * The standard implementation of {@link PluginProvider} that takes a List of {@link
 * ClassModifyingPlugin} and a List of {@link TypeModifyingPlugin}.
 */
public class ModifyingPluginProvider implements PluginProvider {

    private final List<ClassModifyingPlugin> classModifyingPlugins;
    private final List<TypeModifyingPlugin> typeModifyingPlugins;
    private final NameGeneratablePlugin nameGeneratablePlugin;

    /**
     * Constructor to take a List of {@link ClassModifyingPlugin}, a List of {@link
     * TypeModifyingPlugin} and a {@link NameGeneratablePlugin}.
     *
     * @param classModifyingPlugins the List of {@link ClassModifyingPlugin}
     * @param typeModifyingPlugins  the List of {@link TypeModifyingPlugin}
     * @param nameGeneratablePlugin the {@link NameGeneratablePlugin}
     */
    public ModifyingPluginProvider(final List<ClassModifyingPlugin> classModifyingPlugins,
                                   final List<TypeModifyingPlugin> typeModifyingPlugins,
                                   final NameGeneratablePlugin nameGeneratablePlugin) {
        this.classModifyingPlugins = classModifyingPlugins;
        this.typeModifyingPlugins = typeModifyingPlugins;
        this.nameGeneratablePlugin = nameGeneratablePlugin;
    }

    @Override
    public List<ClassModifyingPlugin> classModifyingPlugins() {
        return classModifyingPlugins;
    }

    @Override
    public List<TypeModifyingPlugin> typeModifyingPlugins() {
        return typeModifyingPlugins;
    }

    @Override
    public NameGeneratablePlugin nameGeneratablePlugin() {
        return nameGeneratablePlugin;
    }
}
