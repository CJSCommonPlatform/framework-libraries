package uk.gov.justice.generation.pojo.plugin.classmodifying.properties;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;

/**
 * Class for determining whether additional properties. Should return true if both
 * {@code "additionalProperties": true} is specified in the json schema and the
 * {@link AddAdditionalPropertiesToClassPlugin} is in use
 */
public class AdditionalPropertiesDeterminer {

    /**
     * Should return true if both
     * {@code "additionalProperties": true} is specified in the json schema and the
     * {@link AddAdditionalPropertiesToClassPlugin} is in use. This is to allow
     * Plugins that need to refer to additional properties fields
     * (e.g. {@link uk.gov.justice.generation.pojo.plugin.classmodifying.AddToStringMethodToClassPlugin})
     *
     * @param classDefinition The current class definition
     * @param pluginContext The {@link PluginContext}
     * @return true if additionalProperties should be generated
     */
    public boolean shouldAddAdditionalProperties(
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {

        return classDefinition.allowAdditionalProperties() &&
                pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class);
    }
}
