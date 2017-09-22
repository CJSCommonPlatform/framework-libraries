package uk.gov.justice.generation.pojo.plugin.factory;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PluginsFromClassnameListFactoryTest {

    private static final String AN_EMPTY_STRING = "";
    @InjectMocks
    private PluginsFromClassnameListFactory pluginsFromClassnameListFactory;

    @Test
    public void shouldGetTheCommaSeparatedPluginNamesStringFromThePropertiesAndParseIntoAListOfStrings() throws Exception {

        final String pluginNames =
                "org.bloggs.fred.MyPlugin_1" + "," +
                "org.bloggs.fred.MyPlugin_2" + "," +
                "org.bloggs.fred.MyPlugin_3" + "," +
                "org.bloggs.fred.MyPlugin_4";

        final Map<String, String> generatorProperties = of("plugins", pluginNames);

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.size(), is(4));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_1"));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_2"));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_3"));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_4"));
    }

    @Test
    public void shouldReturnAnEmptyListIfThePluginsPropertyIsNull() throws Exception {

        final Map<String, String> generatorProperties = new HashMap<>();
        generatorProperties.put("plugins", null);

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnAnEmptyListIfThePluginsPropertyIsAnEmptyString() throws Exception {

        final Map<String, String> generatorProperties = of("plugins", AN_EMPTY_STRING);

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnAnEmptyListIfThePluginsPropertyDoesNotExist() throws Exception {

        final Map<String, String> generatorProperties = new HashMap<>();

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.isEmpty(), is(true));
    }
}
