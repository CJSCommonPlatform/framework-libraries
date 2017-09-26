package uk.gov.justice.generation.pojo.plugin.classmodifying;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.Plugin;

import com.squareup.javapoet.TypeSpec;

/**
 * Interface for all plugins that modify the generated classes. These can be plugins for
 * example, adding fields and methods to the class, making the class {@link java.io.Serializable},
 * or adding specific annotations to the class or it's methods.
 *
 * {@link AddFieldsAndMethodsToClassPlugin}
 * {@link AddAdditionalPropertiesToClassPlugin}
 * {@link MakeClassSerializablePlugin}
 */
public interface ClassModifyingPlugin extends Plugin {

    /**
     * Modifies the generation of a class.
     *
     * @param classBuilder A builder for generating the class
     * @param classDefinition The definition of the class from the json schema document
     * @param pluginContext Access to any data the plugin might need
     *
     * @return The modified class's {@link TypeSpec} builder
     */
    TypeSpec.Builder generateWith(final TypeSpec.Builder classBuilder,
                                  final ClassDefinition classDefinition,
                                  final PluginContext pluginContext);
}
