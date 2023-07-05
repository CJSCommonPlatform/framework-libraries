package uk.gov.justice.generation.pojo.plugin;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PluginVerifierTest {

    @InjectMocks
    private PluginVerifier pluginVerifier;

    @Test
    public void shouldEnsureEachPluginIsCompatibleWithTheListOfPluginNames() throws Exception {

        final Plugin plugin_1 = mock(Plugin.class);
        final Plugin plugin_2 = mock(Plugin.class);
        final Plugin plugin_3 = mock(Plugin.class);

        final List<Plugin> allPlugins = asList(plugin_1, plugin_2, plugin_3);
        final List<String> pluginNames = asList(
                "org.fred.Plugin_1",
                "org.fred.Plugin_2",
                "org.fred.Plugin_3");

        pluginVerifier.verifyCompatibility(allPlugins, pluginNames);

        verify(plugin_1).checkCompatibilityWith(pluginNames);
        verify(plugin_2).checkCompatibilityWith(pluginNames);
        verify(plugin_3).checkCompatibilityWith(pluginNames);
    }
}
