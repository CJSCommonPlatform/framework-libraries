package uk.gov.justice.services.core.featurecontrol;

public interface FeatureControlGuard {

    /**
     * Will return true if the named feature is enabled. If the named feature cannot be found
     * false is returned by default.
     *
     * @param featureName The name of the feature
     * @return true if enabled false if disabled or cannot be found
     */
    boolean isFeatureEnabled(final String featureName);
}
