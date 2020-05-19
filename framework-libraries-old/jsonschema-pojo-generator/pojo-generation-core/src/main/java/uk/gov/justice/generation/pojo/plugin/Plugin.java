package uk.gov.justice.generation.pojo.plugin;

import java.util.List;

/**
 * Interface for all plugin types, provides a default implementation of
 * {@link Plugin#checkCompatibilityWith} that does nothing.  Override this method to allow
 * checking for other plugins that are incompatible with another plugin.
 */
public interface Plugin {

    /**
     * Check compatibility of a plugin with a list of other plugins set for this execution.
     *
     * @param pluginNames the list of other plugins for checking compatibility
     */
    default void checkCompatibilityWith(@SuppressWarnings("unused") final List<String> pluginNames) {
    }
}
