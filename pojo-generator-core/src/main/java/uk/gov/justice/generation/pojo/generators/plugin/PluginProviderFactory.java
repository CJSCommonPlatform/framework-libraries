package uk.gov.justice.generation.pojo.generators.plugin;

import static java.lang.String.format;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.generators.plugin.typemodifying.TypeModifyingPlugin;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Creates a {@link PluginProvider} for both {@link ClassModifyingPlugin} and {@link
 * TypeModifyingPlugin}.  ClassModifyingPlugins consist of a merge of default ClassModifyingPlugins
 * and any user defined plugins added to the Maven Plugin generatorProperties tag.
 * TypeModifyingPlugins consist of only plugins added to the Maven Plugin generatorProperties tag.
 *
 * The default {@link ClassModifyingPlugin} are:
 *
 * {@link AddFieldsAndMethodsToClassPlugin}
 * {@link GenerateBuilderForClassPlugin}
 *
 * For example the following adds the MakeClassSerializable to the default plugins and adds three
 * TypeModifyingPlugins:
 *
 * <pre>
 *     {@code
 *
 *      <configuration>
 *          ...
 *          <generatorProperties>
 *              <plugins>
 *                  uk.gov.justice.generation.pojo.generators.plugin.classgenerator.MakeClassSerializablePlugin,
 *                  uk.gov.justice.generation.pojo.generators.plugin.typename.SupportJavaOptionalsPlugin,
 *                  uk.gov.justice.generation.pojo.generators.plugin.typename.SupportUuidsPlugin,
 *                  uk.gov.justice.generation.pojo.generators.plugin.typename.SupportZonedDateTimePlugin
 *              </plugins>
 *          </generatorProperties>
 *          ...
 *      </configuration>
 *
 *     }
 * </pre>
 *
 * If the default ClassModifyingPlugins are not required and other plugins can be used instead, the
 * then the excludeDefaultPlugins tag can be set to true, for example:
 *
 * <pre>
 *     {@code
 *
 *      <configuration>
 *          ...
 *          <generatorProperties>
 *              <excludeDefaultPlugins>true</excludeDefaultPlugins>
 *              <plugins>
 *                  ...
 *              </plugins>
 *          </generatorProperties>
 *          ...
 *      </configuration>
 *
 *     }
 * </pre>
 */
public class PluginProviderFactory {

    private static final String EXCLUDE_DEFAULT_PROPERTY = "excludeDefaultPlugins";
    private static final String PLUGINS_PROPERTY = "plugins";

    private static final boolean CLASS_MODIFYING_PLUGINS = true;
    private static final boolean TYPE_MODIFYING_PLUGINS = false;

    private static final String UNABLE_TO_CREATE_INSTANCE_MESSAGE = "Unable to create instance of pojo plugin with class name %s";
    private static final String INCORRECT_CLASS_TYPE_MESSAGE = "Incorrect Class Type, Class name: %s, does not implement ClassModifyingPlugin or TypeModifyingPlugin.";

    /**
     * Create a {@link PluginProvider} using the settings from the {@link GeneratorConfig}
     *
     * @param generatorConfig the generatorConfig suppying the generatorProperties
     * @return a {@link PluginProvider} instance
     */
    public PluginProvider createFor(final GeneratorConfig generatorConfig) {
        final Map<String, String> generatorProperties = generatorConfig.getGeneratorProperties();
        final Map<Boolean, List<Object>> pluginTypes = partitionPluginsAccordingToType(generatorProperties);

        return new ModifyingPluginProvider(
                defaultAndCustomClassModifyingPlugins(generatorProperties, pluginTypes),
                typeModifyingPlugins(pluginTypes));
    }

    private Map<Boolean, List<Object>> partitionPluginsAccordingToType(final Map<String, String> generatorProperties) {
        return allInstancesOfPluginsFrom(generatorProperties)
                .collect(partitioningBy(plugin -> plugin instanceof ClassModifyingPlugin));
    }

    private List<ClassModifyingPlugin> defaultAndCustomClassModifyingPlugins(final Map<String, String> generatorProperties,
                                                                             final Map<Boolean, List<Object>> pluginTypes) {
        return Stream
                .concat(
                        defaultClassModifyingPlugins(generatorProperties),
                        userDefinedClassModifyingPlugins(pluginTypes))
                .collect(toList());
    }

    private List<TypeModifyingPlugin> typeModifyingPlugins(final Map<Boolean, List<Object>> pluginTypes) {
        return pluginTypes.get(TYPE_MODIFYING_PLUGINS)
                .stream()
                .map(this::castToTypeModifyingPlugin)
                .collect(toList());
    }

    private TypeModifyingPlugin castToTypeModifyingPlugin(final Object plugin) {
        try {
            return (TypeModifyingPlugin) plugin;
        } catch (final ClassCastException e) {
            throw new PluginProviderException(format(INCORRECT_CLASS_TYPE_MESSAGE, plugin.getClass().getName()), e);
        }
    }

    private Stream<ClassModifyingPlugin> userDefinedClassModifyingPlugins(final Map<Boolean, List<Object>> pluginTypes) {
        return pluginTypes.get(CLASS_MODIFYING_PLUGINS)
                .stream()
                .map(plugin -> (ClassModifyingPlugin) plugin);
    }

    private Stream<ClassModifyingPlugin> defaultClassModifyingPlugins(final Map<String, String> generatorProperties) {
        if (isExcludeDefaultPlugins(generatorProperties)) {
            return Stream.empty();
        }

        return Stream.of(
                new AddFieldsAndMethodsToClassPlugin(),
                new GenerateBuilderForClassPlugin(new BuilderGeneratorFactory()));
    }

    private boolean isExcludeDefaultPlugins(final Map<String, String> generatorProperties) {
        if (generatorProperties.containsKey(EXCLUDE_DEFAULT_PROPERTY)) {
            return Boolean.valueOf(generatorProperties.get(EXCLUDE_DEFAULT_PROPERTY));
        }

        return false;
    }

    private Stream<Object> allInstancesOfPluginsFrom(final Map<String, String> generatorProperties) {
        if (generatorProperties.containsKey(PLUGINS_PROPERTY)) {
            final String pluginValues = generatorProperties.get(PLUGINS_PROPERTY);

            if (!pluginValues.isEmpty()) {
                final String[] plugins = pluginValues.split(",");

                return Stream.of(plugins)
                        .map(this::pluginInstance);
            }
        }

        return Stream.empty();
    }

    private Object pluginInstance(final String className) {
        try {
            return Class.forName(className.trim()).newInstance();
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new PluginProviderException(format(UNABLE_TO_CREATE_INSTANCE_MESSAGE, className), e);
        }
    }
}
