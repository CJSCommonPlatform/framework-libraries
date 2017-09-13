package uk.gov.justice.generation.pojo.generators.plugin.testplugins;

import uk.gov.justice.generation.pojo.generators.plugin.namegeneratable.NameGeneratablePlugin;

import org.everit.json.schema.Schema;

public class UserDefinedNameGeneratorPlugin implements NameGeneratablePlugin {

    @Override
    public String rootFieldNameFrom(final Schema schema, final String schemaFilename) {
        return null;
    }
}
