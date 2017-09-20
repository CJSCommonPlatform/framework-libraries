package uk.gov.justice.generation.pojo.plugin;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGeneratorFactory;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.FieldNameFromFileNameGeneratorPlugin;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;
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
 * {@link AddFieldsAndMethodsToClassPlugin} {@link GenerateBuilderForClassPlugin}
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
 *                  uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin,
 *                  uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin,
 *                  uk.gov.justice.generation.pojo.plugin.typemodifying.CustomReturnTypePlugin
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
 * <pre><blockquote>
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
 * </blockquote></pre>
 */
public class PluginProviderFactory {

    private static final String EXCLUDE_DEFAULT_PROPERTY = "excludeDefaultPlugins";
    private static final String PLUGINS_PROPERTY = "plugins";

    private static final Class<ClassModifyingPlugin> CLASS_MODIFYING_PLUGIN = ClassModifyingPlugin.class;
    private static final Class<TypeModifyingPlugin> TYPE_MODIFYING_PLUGIN = TypeModifyingPlugin.class;
    private static final Class<NameGeneratablePlugin> NAME_GENERATABLE_PLUGIN = NameGeneratablePlugin.class;

    private static final String UNABLE_TO_CREATE_INSTANCE_MESSAGE = "Unable to create instance of pojo plugin with class name %s";
    private static final String INCORRECT_CLASS_TYPE_MESSAGE = "Incorrect Class Type, Class name: %s, does not implement ClassModifyingPlugin or TypeModifyingPlugin or NameGeneratablePlugin.";
    private static final int FIRST_INDEX = 0;

    /**
     * Create a {@link PluginProvider} using the settings from the {@link GeneratorConfig}
     *
     * @param generatorConfig the generatorConfig suppying the generatorProperties
     * @return a {@link PluginProvider} instance
     */
    public PluginProvider createFor(final GeneratorConfig generatorConfig) {
        final Map<String, String> generatorProperties = generatorConfig.getGeneratorProperties();
        final Map<Class<?>, List<Object>> pluginTypes = partitionPluginsAccordingToType(generatorProperties);

        return new ModifyingPluginProvider(
                defaultAndUserDefinedClassModifyingPlugins(generatorProperties, pluginTypes),
                typeModifyingPlugins(pluginTypes),
                defaultOrUserDefinedNameGeneratablePlugin(pluginTypes));
    }

    private NameGeneratablePlugin defaultOrUserDefinedNameGeneratablePlugin(final Map<Class<?>, List<Object>> pluginTypes) {
        if (pluginTypes.containsKey(NAME_GENERATABLE_PLUGIN)) {
            final List<Object> nameGeneratablePlugins = pluginTypes.get(NAME_GENERATABLE_PLUGIN);

            if (nameGeneratablePlugins.size() > 1) {
                final List<String> pluginNames = nameGeneratablePlugins.stream().map(plugin -> plugin.getClass().getSimpleName()).collect(toList());
                throw new PluginProviderException(format("Multiple NameGeneratablePlugin identified, please supply only one. List: %s", pluginNames));
            }

            return (NameGeneratablePlugin) nameGeneratablePlugins.get(FIRST_INDEX);
        }

        return new FieldNameFromFileNameGeneratorPlugin();
    }

    private Map<Class<?>, List<Object>> partitionPluginsAccordingToType(final Map<String, String> generatorProperties) {
        return allInstancesOfPluginsFrom(generatorProperties)
                .collect(groupingBy(this::groupByPluginInterface));
    }

    private Class<?> groupByPluginInterface(final Object plugin) {

        final List<Class<?>> classList = asList(plugin.getClass().getInterfaces());

        if (classList.contains(CLASS_MODIFYING_PLUGIN)) {
            return CLASS_MODIFYING_PLUGIN;
        }

        if (classList.contains(TYPE_MODIFYING_PLUGIN)) {
            return TYPE_MODIFYING_PLUGIN;
        }

        if (classList.contains(NAME_GENERATABLE_PLUGIN)) {
            return NAME_GENERATABLE_PLUGIN;
        }

        throw new PluginProviderException(format(INCORRECT_CLASS_TYPE_MESSAGE, plugin.getClass().getName()));
    }

    private List<ClassModifyingPlugin> defaultAndUserDefinedClassModifyingPlugins(final Map<String, String> generatorProperties,
                                                                                  final Map<Class<?>, List<Object>> pluginTypes) {
        return Stream
                .concat(
                        defaultClassModifyingPlugins(generatorProperties),
                        userDefinedClassModifyingPlugins(pluginTypes))
                .collect(toList());
    }

    private List<TypeModifyingPlugin> typeModifyingPlugins(final Map<Class<?>, List<Object>> pluginTypes) {
        if (pluginTypes.containsKey(TYPE_MODIFYING_PLUGIN)) {
            return pluginTypes.get(TYPE_MODIFYING_PLUGIN)
                    .stream()
                    .map(plugin -> (TypeModifyingPlugin) plugin)
                    .collect(toList());
        }

        return emptyList();
    }

    private Stream<ClassModifyingPlugin> userDefinedClassModifyingPlugins(final Map<Class<?>, List<Object>> pluginTypes) {
        if (pluginTypes.containsKey(CLASS_MODIFYING_PLUGIN)) {
            return pluginTypes.get(CLASS_MODIFYING_PLUGIN)
                    .stream()
                    .map(plugin -> (ClassModifyingPlugin) plugin);
        }

        return Stream.empty();
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
