package uk.gov.justice.services.core.featurecontrol.domain;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public class FeatureControl {

    private final List<Feature> features;

    @JsonCreator
    public FeatureControl(final List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureControl)) return false;
        final FeatureControl that = (FeatureControl) o;
        return Objects.equals(features, that.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(features);
    }

    @Override
    public String toString() {
        return "FeatureControl{" +
                "features=" + features +
                '}';
    }
}
