package uk.gov.justice.services.core.featurecontrol;

import static java.util.Collections.emptyList;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

import javax.enterprise.inject.Default;

/**
 * Default implementation of FeatureFetcher to allow integration tests to run without the need
 * to have connections set up to azure.
 */
@Default
public class EmptyFeatureFetcher implements FeatureFetcher {

    @Override
    public List<Feature> fetchFeatures() {
        return emptyList();
    }
}
