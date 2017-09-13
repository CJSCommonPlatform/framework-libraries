package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;

/**
 * Defines a Class for providing the List of {@link ClassModifyingPlugin} and a List of {@link
 * TypeModifyingPlugin}.
 *
 * These are used during generation to modify Classes or TypeNames before generation.
 */
public interface PluginProvider {

    /**
     * Returns a List of {@link ClassModifyingPlugin} that is used to modify Classes before
     * generation.
     *
     * @return the List of {@link ClassModifyingPlugin}
     */
    List<ClassModifyingPlugin> classModifyingPlugins();

    /**
     * Returns a List of {@link TypeModifyingPlugin} that is used to modify TypeNames before
     * generation.
     *
     * @return the List of {@link TypeModifyingPlugin}
     */
    List<TypeModifyingPlugin> typeModifyingPlugins();

    /**
     * Returns the {@link NameGeneratablePlugin} that is used to generate the root schema field
     * name.
     *
     * @return the {@link NameGeneratablePlugin}
     */
    NameGeneratablePlugin nameGeneratablePlugin();
}
