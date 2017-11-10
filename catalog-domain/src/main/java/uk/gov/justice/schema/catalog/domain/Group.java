package uk.gov.justice.schema.catalog.domain;

import java.util.List;

public class Group {

    private final String baseLocation;
    private final String name;
    private final List<Schema> schema;

    public Group(final String baseLocation, final String name, final List<Schema> schema) {
        this.baseLocation = baseLocation;
        this.name = name;
        this.schema = schema;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public String getName() {
        return name;
    }

    public List<Schema> getSchema() {
        return schema;
    }
}
