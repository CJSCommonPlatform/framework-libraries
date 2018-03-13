package uk.gov.justice.domain;

import uk.gov.justice.domain.subscriptiondescriptor.Location;

public final class LocationBuilder {
    
    private String jmsUri;
    private String restUri;

    private LocationBuilder() {
    }

    public static LocationBuilder location() {
        return new LocationBuilder();
    }

    public LocationBuilder withJmsUri(final String jmsUri) {
        this.jmsUri = jmsUri;
        return this;
    }

    public LocationBuilder withRestUri(final String restUri) {
        this.restUri = restUri;
        return this;
    }

    public Location build() {
        return new Location(jmsUri, restUri);
    }
}
