package uk.gov.justice.generation.pojo.plugin.classmodifying.properties;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

public class AdditionalPropertiesDeterminer {

    public boolean shouldAddAdditionalProperties(
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {

        return classDefinition.allowAdditionalProperties() &&
                pluginContext.isPluginInUse(AddAdditionalPropertiesToClassPlugin.class);
    }
}
