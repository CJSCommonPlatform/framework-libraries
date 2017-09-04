package uk.gov.justice.generation.pojo.generators.plugin;

import static java.lang.String.format;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.Map;

public class PluginProviderFactory {

    private static final String POJO_PLUGIN_PROVIDER = "pojo-plugin-provider";

    public PluginProvider createFor(final GeneratorConfig generatorConfig) {
        final Map<String, String> generatorProperties = generatorConfig.getGeneratorProperties();

        if (generatorProperties.containsKey(POJO_PLUGIN_PROVIDER)) {
            final String className = generatorProperties.get(POJO_PLUGIN_PROVIDER);

            try {
                return (PluginProvider) Class.forName(className).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
                throw new PluginProviderException(format("Unable to create instance of pojo plugin provider with class name %s", className), e);
            }
        }

        return new DefaultPluginProvider();
    }
}
