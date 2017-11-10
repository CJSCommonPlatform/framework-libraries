package uk.gov.justice.schema.catalog.domain;

public class Schema {
    private final String id;

    private final String location;

    public Schema(final String id, final String location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }
}
