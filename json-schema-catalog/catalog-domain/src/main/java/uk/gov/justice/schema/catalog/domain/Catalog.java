package uk.gov.justice.schema.catalog.domain;

import java.util.List;

/**
 * An Object representation of a json Catalog file
 */
public class Catalog {

    private final List<Group> groups;
    private final String name;

    public Catalog(final String name, final List<Group> groups) {
        this.groups = groups;
        this.name = name;
    }

    /**
     * @return The list of all {@link Group}s in the Catalog
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * @return The name of the Catalog
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "groups=" + groups +
                ", name='" + name + '\'' +
                '}';
    }
}
