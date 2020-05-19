package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AllPluginsInstantiatorTest {

    @Mock
    private PluginInstantiator pluginInstantiator;

    @InjectMocks
    private AllPluginsInstantiator allPluginsInstantiator;

    @Test
    public void shouldInstantiateAllTheListOfPluginNames() throws Exception {

        final String pluginName_1 = "org.bloggs.fred.Plugin_1";
        final String pluginName_2 = "org.bloggs.fred.Plugin_2";
        final String pluginName_3 = "org.bloggs.fred.Plugin_3";

        final Plugin plugin_1 = mock(ClassModifyingPlugin.class);
        final Plugin plugin_2 = mock(ClassModifyingPlugin.class);
        final Plugin plugin_3 = mock(ClassModifyingPlugin.class);

        final List<String> pluginNames = asList(
                pluginName_1,
                pluginName_2,
                pluginName_3
        );

        when(pluginInstantiator.instantiate(pluginName_1)).thenReturn(plugin_1);
        when(pluginInstantiator.instantiate(pluginName_2)).thenReturn(plugin_2);
        when(pluginInstantiator.instantiate(pluginName_3)).thenReturn(plugin_3);

        final List<Plugin> plugins = allPluginsInstantiator.instantiate(pluginNames);

        assertThat(plugins.size(), is(3));
        assertThat(plugins, hasItem(plugin_1));
        assertThat(plugins, hasItem(plugin_2));
        assertThat(plugins, hasItem(plugin_3));
    }
}
