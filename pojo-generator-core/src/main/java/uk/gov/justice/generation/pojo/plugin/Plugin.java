package uk.gov.justice.generation.pojo.plugin;

import java.util.List;

public interface Plugin {

    default void checkCompatibilityWith(final List<Plugin> plugins)  {}
}
