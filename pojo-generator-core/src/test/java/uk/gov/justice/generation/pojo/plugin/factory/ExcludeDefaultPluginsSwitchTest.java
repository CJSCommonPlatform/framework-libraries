package uk.gov.justice.generation.pojo.plugin.factory;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcludeDefaultPluginsSwitchTest {

    @InjectMocks
    private ExcludeDefaultPluginsSwitch excludeDefaultPluginsSwitch;

    @Test
    public void shouldGetTheStateOfTheExcludeDefaultPluginsProperty() throws Exception {

        final Map<String, String> generatorPropertiesWithPluginsTrue = of(
                "property_1", "value_1",
                "property_2", "value_2",
                "excludeDefaultPlugins", "true",
                "property_3", "value_3"
        );

        final Map<String, String> generatorPropertiesWithPluginsFalse = of(
                "property_1", "value_1",
                "property_2", "value_2",
                "excludeDefaultPlugins", "false",
                "property_3", "value_3"
        );

        assertThat(excludeDefaultPluginsSwitch.shouldExcludeDefaultPlugins(generatorPropertiesWithPluginsTrue), is(true));
        assertThat(excludeDefaultPluginsSwitch.shouldExcludeDefaultPlugins(generatorPropertiesWithPluginsFalse), is(false));
    }

    @Test
    public void shouldHandleAMalformedExcludeDefaultPluginsProperty() throws Exception {

        final Map<String, String> generatorProperties = of(
                "property_1", "value_1",
                "property_2", "value_2",
                "excludeDefaultPlugins", "troo",
                "property_3", "value_3"
        );

        assertThat(excludeDefaultPluginsSwitch.shouldExcludeDefaultPlugins(generatorProperties), is(false));
    }

    @Test
    public void shouldReturnFalseIfTheExcludeDefaultPluginsPropertyIsMissing() throws Exception {

        final Map<String, String> generatorProperties = of(
                "property_1", "value_1",
                "property_2", "value_2",
                "property_3", "value_3"
        );

        assertThat(excludeDefaultPluginsSwitch.shouldExcludeDefaultPlugins(generatorProperties), is(false));
    }
}
