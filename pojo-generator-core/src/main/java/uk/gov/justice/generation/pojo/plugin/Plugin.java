package uk.gov.justice.generation.pojo.plugin;

import java.util.List;

public interface Plugin {

    default void checkCompatibilityWith(@SuppressWarnings("unused") final List<String> pluginNames)  {}
}
