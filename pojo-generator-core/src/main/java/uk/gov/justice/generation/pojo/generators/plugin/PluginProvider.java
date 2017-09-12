package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.generators.plugin.classgenerator.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.typename.TypeModifyingPlugin;

import java.util.List;

public interface PluginProvider {

    List<ClassModifyingPlugin> classModifyingPlugins();

    List<TypeModifyingPlugin> typeModifyingPlugins();
}
