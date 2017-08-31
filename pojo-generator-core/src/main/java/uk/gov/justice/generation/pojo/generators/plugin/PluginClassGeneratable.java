package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import com.squareup.javapoet.TypeSpec;

public interface PluginClassGeneratable {

    TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder, final ClassDefinition classDefinition, final JavaGeneratorFactory javaGeneratorFactory);
}
