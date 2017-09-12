package uk.gov.justice.generation.pojo.generators.plugin.classgenerator;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import com.squareup.javapoet.TypeSpec;

/**
 * Interface for all plugins that modify the generated classes. These can be plugins for
 * example, adding fields and methods to the class, making the class {@link java.io.Serializable},
 * or adding specific annotations to the class or it's methods.
 *
 * {@link MakeClassSerializablePlugin}
 * {@link AddFieldsAndMethodsToClassPlugin}
 */
public interface ClassModifyingPlugin {

    /**
     * Modifies the generation of a class.
     *
     * @param typeSpecBuilder A builder for generating the class
     * @param classDefinition The definition of the class from the json schema document
     * @param pluginContext Access to any data the plugin might need
     *
     * @return The modified class's {@link TypeSpec} builder
     */
    TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                  final ClassDefinition classDefinition,
                                  final PluginContext pluginContext);
}
