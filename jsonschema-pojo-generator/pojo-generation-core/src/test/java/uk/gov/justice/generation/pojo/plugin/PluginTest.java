package uk.gov.justice.generation.pojo.plugin;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class PluginTest {

    @Test
    public void shouldTestAVoidEmptyDefaultInterfaceJustToStopCoverallsFromWhinging() throws Exception {

        final Plugin plugin = new Plugin() {};

        plugin.checkCompatibilityWith(new ArrayList<>());
    }
}
