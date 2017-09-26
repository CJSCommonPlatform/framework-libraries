package uk.gov.justice.generation.pojo.plugin.namegeneratable;


import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import org.everit.json.schema.Schema;

/**
 * Interface for all plugins that produce a root field name for the root schema from a file.  The
 * plugin can decide to parse the schema for a description field or parse the schema filename to
 * create an appropriate name for the schema.  Note: the field name is converted to produce the
 * Class name at generation time, by uppercasing the first character of the field name.
 */
public interface NameGeneratablePlugin extends Plugin {

    /**
     * Generates a schema field name for the root schema from the {@link Schema} and schema
     * filename.
     *
     * @param schema         the {@link Schema} to produce a field name for
     * @param schemaFilename the schema filename to produce a field name for
     * @return the root field name
     */
    String rootFieldNameFrom(final Schema schema,
                             final String schemaFilename,
                             final PluginContext pluginContext);
}
