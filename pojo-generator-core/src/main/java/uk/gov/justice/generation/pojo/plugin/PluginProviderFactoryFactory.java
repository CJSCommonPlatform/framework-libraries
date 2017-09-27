package uk.gov.justice.generation.pojo.plugin;

import uk.gov.justice.generation.pojo.plugin.factory.AllPluginsInstantiator;
import uk.gov.justice.generation.pojo.plugin.factory.ClassModifyingPluginsSelector;
import uk.gov.justice.generation.pojo.plugin.factory.DefaultPluginsProvider;
import uk.gov.justice.generation.pojo.plugin.factory.Instantiator;
import uk.gov.justice.generation.pojo.plugin.factory.NameGeneratingPluginFactory;
import uk.gov.justice.generation.pojo.plugin.factory.PluginInstantiator;
import uk.gov.justice.generation.pojo.plugin.factory.PluginTypeSorter;
import uk.gov.justice.generation.pojo.plugin.factory.PluginsFromClassnameListFactory;
import uk.gov.justice.generation.pojo.plugin.factory.TypeModifyingPluginsSelector;

public class PluginProviderFactoryFactory {

    public PluginProviderFactory create() {

        final DefaultPluginsProvider defaultPluginsProvider = new DefaultPluginsProvider();

        return new PluginProviderFactory(
                new NameGeneratingPluginFactory(),
                new ClassModifyingPluginsSelector(defaultPluginsProvider),
                new TypeModifyingPluginsSelector(),
                new PluginTypeSorter(),
                new AllPluginsInstantiator(new PluginInstantiator(new Instantiator())),
                new PluginsFromClassnameListFactory(),
                new PluginVerifier()
        );
    }
}
