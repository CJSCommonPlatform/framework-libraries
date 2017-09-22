package uk.gov.justice.generation.pojo.plugin.testplugins;

import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.NameGeneratablePlugin;

import org.everit.json.schema.Schema;

public class UserDefinedNameGeneratorPlugin implements NameGeneratablePlugin {

    @Override
    public String rootFieldNameFrom(final Schema schema, final String schemaFilename, final PluginContext pluginContext) {
        return null;
    }
}
