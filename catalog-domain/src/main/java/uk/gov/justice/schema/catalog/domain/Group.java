package uk.gov.justice.schema.catalog.domain;

import java.util.List;

public class Group {

    private final String baseLocation;
    private final String name;
    private final List<Schema> schemas;

    public Group(final String baseLocation, final String name, final List<Schema> schemas) {
        this.baseLocation = baseLocation;
        this.name = name;
        this.schemas = schemas;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public String getName() {
        return name;
    }

    public List<Schema> getSchemas() {
        return schemas;
    }

    @Override
    public String toString() {
        return "Group{" +
                "baseLocation='" + baseLocation + '\'' +
                ", name='" + name + '\'' +
                ", schemas=" + schemas +
                '}';
    }
}
