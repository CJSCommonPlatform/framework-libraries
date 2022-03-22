package uk.gov.justice.services.healthcheck.api;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HealthcheckResult {

    private final boolean passed;
    private final String errorMessage;

    private HealthcheckResult(final boolean passed, final String errorMessage) {
        this.passed = passed;
        this.errorMessage = errorMessage;
    }

    public boolean isPassed() {
        return passed;
    }

    public Optional<String> getErrorMessage() {
        return ofNullable(errorMessage);
    }

    public static HealthcheckResult success() {
        return new HealthcheckResult(true, null);
    }

    public static HealthcheckResult failure(final String errorMessage) {
        return new HealthcheckResult(false, errorMessage);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (!(o instanceof HealthcheckResult)) return false;

        final HealthcheckResult that = (HealthcheckResult) o;

        return new EqualsBuilder().append(passed, that.passed).append(errorMessage, that.errorMessage).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(passed).append(errorMessage).toHashCode();
    }

    @Override
    public String toString() {
        return "HealthcheckResult{" +
                "passed=" + passed +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
