package uk.gov.justice.services.core.featurecontrol;

import uk.gov.justice.services.core.featurecontrol.domain.Feature;

import java.util.List;

public interface FeatureFetcher {

    /**
     * Gets the list of current {@link Feature}s from remote storage
     *
     * @return A List of enabled/disabled {@link Feature}s
     */
    List<Feature> fetchFeatures();
}
