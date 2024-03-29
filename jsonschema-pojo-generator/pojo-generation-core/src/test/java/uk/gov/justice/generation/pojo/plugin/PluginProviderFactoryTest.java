package uk.gov.justice.generation.pojo.plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.factory.AllPluginsInstantiator;
import uk.gov.justice.generation.pojo.plugin.factory.ClassModifyingPluginsSelector;
import uk.gov.justice.generation.pojo.plugin.factory.PluginTypeSorter;
import uk.gov.justice.generation.pojo.plugin.factory.PluginsFromClassnameListFactory;
import uk.gov.justice.generation.pojo.plugin.factory.TypeModifyingPluginsSelector;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PluginProviderFactoryTest {

    @Mock
    private ClassModifyingPluginsSelector classModifyingPluginsSelector;

    @Mock
    private TypeModifyingPluginsSelector typeModifyingPluginsSelector;

    @Mock
    private PluginTypeSorter pluginTypeSorter;

    @Mock
    private AllPluginsInstantiator allPluginsInstantiator;

    @Mock
    private PluginsFromClassnameListFactory parsePluginNames;

    @Mock
    private PluginVerifier pluginVerifier;

    @InjectMocks
    private PluginProviderFactory pluginProviderFactory;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindAllPluginsTheInstantiateAndReturnThemInThePluginProvider() throws Exception {

        final PojoGeneratorProperties generatorProperties = mock(PojoGeneratorProperties.class);
        final List<String> pluginNames = mock(List.class);
        final List<Plugin> allPlugins = mock(List.class);
        final Map<Class<?>, List<Plugin>> pluginTypes = mock(Map.class);
        final List<ClassModifyingPlugin> classModifyingPlugins = mock(List.class);
        final List<TypeModifyingPlugin> typeModifyingPlugins = mock(List.class);

        when(parsePluginNames.parsePluginNames(generatorProperties)).thenReturn(pluginNames);
        when(allPluginsInstantiator.instantiate(pluginNames)).thenReturn(allPlugins);
        when(pluginTypeSorter.sortByType(allPlugins)).thenReturn(pluginTypes);

        when(classModifyingPluginsSelector.selectFrom(pluginTypes, generatorProperties)).thenReturn(classModifyingPlugins);
        when(typeModifyingPluginsSelector.selectFrom(pluginTypes)).thenReturn(typeModifyingPlugins);

        final PluginProvider pluginProvider = pluginProviderFactory.createFor(generatorProperties);

        assertThat(pluginProvider.classModifyingPlugins(), is(classModifyingPlugins));
        assertThat(pluginProvider.typeModifyingPlugins(), is(typeModifyingPlugins));

        pluginVerifier.verifyCompatibility(allPlugins, pluginNames);
    }
}
