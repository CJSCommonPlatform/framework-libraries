package uk.gov.justice.services.core.featurecontrol.lookup;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.Optional;

public interface FeatureStore {

    /**
     * Lookup the Feature by name
     * @param featureName The name of the Feature
     *
     * @return The Optional Feature. Optional.empty() if not found
     */
    Optional<Feature> lookup(String featureName);
}
