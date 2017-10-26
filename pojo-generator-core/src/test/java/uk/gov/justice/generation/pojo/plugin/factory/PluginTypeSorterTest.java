package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.PluginProviderException;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.TypeModifyingPlugin;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PluginTypeSorterTest {

    @InjectMocks
    private PluginTypeSorter pluginTypeSorter;

    @Test
    public void shouldSortTheListOfPluginsByTheirPluginType() throws Exception {

        final ClassModifyingPlugin classModifyingPlugin_1 = mock(ClassModifyingPlugin.class);
        final ClassModifyingPlugin classModifyingPlugin_2 = mock(ClassModifyingPlugin.class);
        final ClassModifyingPlugin classModifyingPlugin_3 = mock(ClassModifyingPlugin.class);

        final TypeModifyingPlugin typeModifyingPlugin_1 = mock(TypeModifyingPlugin.class);
        final TypeModifyingPlugin typeModifyingPlugin_2 = mock(TypeModifyingPlugin.class);
        final TypeModifyingPlugin typeModifyingPlugin_3 = mock(TypeModifyingPlugin.class);

        final List<Plugin> plugins = asList(
                classModifyingPlugin_1,
                typeModifyingPlugin_1,
                classModifyingPlugin_2,
                typeModifyingPlugin_2,
                classModifyingPlugin_3,
                typeModifyingPlugin_3
        );

        final Map<Class<?>, List<Plugin>> pluginsByType = pluginTypeSorter.sortByType(plugins);

        assertThat(pluginsByType.size(), is(2));

        assertThat(pluginsByType.containsKey(ClassModifyingPlugin.class), is(true));
        assertThat(pluginsByType.containsKey(TypeModifyingPlugin.class), is(true));

        final List<Plugin> classModifyingPlugins = pluginsByType.get(ClassModifyingPlugin.class);
        final List<Plugin> typeModifyingPlugins = pluginsByType.get(TypeModifyingPlugin.class);

        assertThat(classModifyingPlugins, hasItem(classModifyingPlugin_1));
        assertThat(classModifyingPlugins, hasItem(classModifyingPlugin_2));
        assertThat(classModifyingPlugins, hasItem(classModifyingPlugin_3));

        assertThat(typeModifyingPlugins, hasItem(typeModifyingPlugin_1));
        assertThat(typeModifyingPlugins, hasItem(typeModifyingPlugin_2));
        assertThat(typeModifyingPlugins, hasItem(typeModifyingPlugin_3));
    }

    @Test
    public void shouldFailIfTheClassIsNotAPluginClass() throws Exception {

        try {
            final Plugin plugin = new MyDodgyPlugin();

            pluginTypeSorter.sortByType(singletonList(plugin));
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Incorrect Class Type, Class name: uk.gov.justice.generation.pojo.plugin.factory.MyDodgyPlugin, does not implement ClassModifyingPlugin or TypeModifyingPlugin."));
        }
    }

}

class MyDodgyPlugin implements Plugin {

}
