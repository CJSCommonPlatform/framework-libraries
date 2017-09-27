package uk.gov.justice.generation.pojo.plugin;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddFieldsAndMethodsToClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.factory.AllPluginsInstantiator;
import uk.gov.justice.generation.pojo.plugin.factory.ClassModifyingPluginsSelector;
import uk.gov.justice.generation.pojo.plugin.factory.NameGeneratingPluginFactory;
import uk.gov.justice.generation.pojo.plugin.factory.PluginTypeSorter;
import uk.gov.justice.generation.pojo.plugin.factory.PluginsFromClassnameListFactory;
import uk.gov.justice.generation.pojo.plugin.factory.TypeModifyingPluginsSelector;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.List;
import java.util.Map;

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

    private final NameGeneratingPluginFactory nameGeneratingPluginFactory;
    private final ClassModifyingPluginsSelector classModifyingPluginsSelector;
    private final TypeModifyingPluginsSelector typeModifyingPluginsSelector;
    private final PluginTypeSorter pluginTypeSorter;
    private final AllPluginsInstantiator allPluginsInstantiator;
    private final PluginsFromClassnameListFactory parsePluginNames;
    private final PluginVerifier pluginVerifier;

    public PluginProviderFactory(
            final NameGeneratingPluginFactory nameGeneratingPluginFactory,
            final ClassModifyingPluginsSelector classModifyingPluginsSelector,
            final TypeModifyingPluginsSelector typeModifyingPluginsSelector,
            final PluginTypeSorter pluginTypeSorter,
            final AllPluginsInstantiator allPluginsInstantiator,
            final PluginsFromClassnameListFactory parsePluginNames,
            final PluginVerifier pluginVerifier) {
        this.nameGeneratingPluginFactory = nameGeneratingPluginFactory;
        this.classModifyingPluginsSelector = classModifyingPluginsSelector;
        this.typeModifyingPluginsSelector = typeModifyingPluginsSelector;
        this.pluginTypeSorter = pluginTypeSorter;
        this.allPluginsInstantiator = allPluginsInstantiator;
        this.parsePluginNames = parsePluginNames;
        this.pluginVerifier = pluginVerifier;
    }

    /**
     * Create a {@link PluginProvider} using the settings from the {@link GeneratorConfig}
     *
     * @param generatorProperties the generatorProperties1 supplying the generatorProperties
     * @return a {@link PluginProvider} instance
     */
    public PluginProvider createFor(final PojoGeneratorProperties generatorProperties) {

        final List<String> pluginNames = parsePluginNames.parsePluginNames(generatorProperties);
        final List<Plugin> allPlugins = allPluginsInstantiator.instantiate(pluginNames);

        pluginVerifier.verifyCompatibility(allPlugins, pluginNames);

        final Map<Class<?>, List<Plugin>> pluginTypes = pluginTypeSorter.sortByType(allPlugins);

        return new ModifyingPluginProvider(
                classModifyingPluginsSelector.selectFrom(pluginTypes, generatorProperties),
                typeModifyingPluginsSelector.selectFrom(pluginTypes),
                nameGeneratingPluginFactory.create(pluginTypes));
    }
}
