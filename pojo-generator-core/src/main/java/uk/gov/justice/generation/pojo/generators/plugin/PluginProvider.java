package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassGeneratorPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeNamePlugin;

import java.util.List;

public interface PluginProvider {

    List<ClassGeneratorPlugin> pluginClassGenerators();

    List<TypeNamePlugin> typeNamePlugins();
}
