package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.plugin.Plugin;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProviderException;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.NameGeneratablePlugin;
import uk.gov.justice.generation.pojo.plugin.namegeneratable.RootNameGeneratorPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.everit.json.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NameGeneratingPluginFactoryTest {

    @InjectMocks
    private NameGeneratingPluginFactory nameGeneratingPluginFactory;

    @Test
    public void shouldGetTheRootFieldNameGeneratorPluginIfNoOverriddenNameGeneratingPluginSpecified() throws Exception {

        final Map<Class<?>, List<Plugin>> pluginTypes = emptyMap();

        final NameGeneratablePlugin nameGeneratablePlugin = nameGeneratingPluginFactory.create(pluginTypes);

        assertThat(nameGeneratablePlugin, is(instanceOf(RootNameGeneratorPlugin.class)));
    }

    @Test
    public void shouldGetTheOverriddenNameGeneratingPluginSpecified() throws Exception {

        final Map<Class<?>, List<Plugin>> pluginTypes = getSinglePluginTypes();

        final NameGeneratablePlugin nameGeneratablePlugin = nameGeneratingPluginFactory.create(pluginTypes);

        assertThat(nameGeneratablePlugin, is(instanceOf(MyNameGeneratablePlugin.class)));
    }

    @Test
    public void shouldFailMoreThanOneOverriddenNameGeneratingPluginSpecified() throws Exception {

        final Map<Class<?>, List<Plugin>> pluginTypes = getMultiplePluginTypes();

        try {
            nameGeneratingPluginFactory.create(pluginTypes);
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Multiple NameGeneratablePlugin identified, please supply only one. List: [MyNameGeneratablePlugin, MyOtherNameGeneratablePlugin]"));
        }
    }

    private Map<Class<?>, List<Plugin>> getSinglePluginTypes() {

        final HashMap<Class<?>, List<Plugin>> pluginTypes = new HashMap<>();

        final List<Plugin> plugins = singletonList(new MyNameGeneratablePlugin());
        pluginTypes.put(NameGeneratablePlugin.class, plugins);

        return pluginTypes;
    }

    private Map<Class<?>, List<Plugin>> getMultiplePluginTypes() {

        final HashMap<Class<?>, List<Plugin>> pluginTypes = new HashMap<>();

        final List<Plugin> plugins = asList(new MyNameGeneratablePlugin(), new MyOtherNameGeneratablePlugin());
        pluginTypes.put(NameGeneratablePlugin.class, plugins);

        return pluginTypes;
    }
}

class MyNameGeneratablePlugin implements NameGeneratablePlugin {

    @Override
    public String rootFieldNameFrom(final Schema schema, final String schemaFilename, final PluginContext pluginContext) {
        return null;
    }
}

class MyOtherNameGeneratablePlugin implements NameGeneratablePlugin {

    @Override
    public String rootFieldNameFrom(final Schema schema, final String schemaFilename, final PluginContext pluginContext) {
        return null;
    }
}
