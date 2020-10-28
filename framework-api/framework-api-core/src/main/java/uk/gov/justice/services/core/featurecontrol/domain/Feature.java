package uk.gov.justice.services.core.featurecontrol.domain;

import java.util.Objects;

public class Feature {

    private final String featureName;
    private final String description;
    private final boolean enabled;

    public Feature(final String featureName, final String description, final boolean enabled) {
        this.featureName = featureName;
        this.description = description;
        this.enabled = enabled;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Feature)) return false;
        final Feature feature = (Feature) o;
        return enabled == feature.enabled &&
                Objects.equals(featureName, feature.featureName) &&
                Objects.equals(description, feature.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureName, description, enabled);
    }

    @Override
    public String toString() {
        return "Feature{" +
                "featureName='" + featureName + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
