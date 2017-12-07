package uk.gov.justice.schema.catalog.domain;

import java.util.List;

/**
 * An Object representation of a group in a json catalog file
 */
public class Group {

    private final String baseLocation;
    private final String name;
    private final List<Schema> schemas;

    public Group(final String baseLocation, final String name, final List<Schema> schemas) {
        this.baseLocation = baseLocation;
        this.name = name;
        this.schemas = schemas;
    }

    /**
     * @return The base location of the group
     */
    public String getBaseLocation() {
        return baseLocation;
    }

    /**
     * @return The name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * @return A {@link List} of all {@link Schema}s in the group
     */
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
