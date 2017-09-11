package uk.gov.justice.generation.pojo.generators.plugin.classgenerator;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import com.squareup.javapoet.TypeSpec;

public interface ClassGeneratorPlugin {

    TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                  final ClassDefinition classDefinition,
                                  final PluginContext pluginContext);
}
