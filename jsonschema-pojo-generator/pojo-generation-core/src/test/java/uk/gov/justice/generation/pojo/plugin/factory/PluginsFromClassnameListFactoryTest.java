package uk.gov.justice.generation.pojo.plugin.factory;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.utils.PojoGeneratorPropertiesBuilder.pojoGeneratorPropertiesBuilder;

import uk.gov.justice.generation.pojo.core.PojoGeneratorProperties;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PluginsFromClassnameListFactoryTest {

    @InjectMocks
    private PluginsFromClassnameListFactory pluginsFromClassnameListFactory;

    @Test
    public void shouldGetTheCommaSeparatedPluginNamesStringFromThePropertiesAndParseIntoAListOfStrings() throws Exception {

        final List<String> pluginNames = asList(
                "org.bloggs.fred.MyPlugin_1",
                "org.bloggs.fred.MyPlugin_2",
                "org.bloggs.fred.MyPlugin_3",
                "org.bloggs.fred.MyPlugin_4");

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withPlugins(pluginNames)
                .build();

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.size(), is(4));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_1"));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_2"));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_3"));
        assertThat(pluginNamesList, hasItem("org.bloggs.fred.MyPlugin_4"));
    }

    @Test
    public void shouldReturnAnEmptyListIfThePluginsPropertyIsNull() throws Exception {

        final PojoGeneratorProperties generatorProperties = new PojoGeneratorProperties();

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnAnEmptyListIfThePluginsPropertyIsAnEmptyString() throws Exception {

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder()
                .withPlugins(emptyList())
                .build();

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.isEmpty(), is(true));
    }

    @Test
    public void shouldReturnAnEmptyListIfThePluginsPropertyDoesNotExist() throws Exception {

        final PojoGeneratorProperties generatorProperties = pojoGeneratorPropertiesBuilder().build();

        final List<String> pluginNamesList = pluginsFromClassnameListFactory.parsePluginNames(generatorProperties);

        assertThat(pluginNamesList.isEmpty(), is(true));
    }
}
