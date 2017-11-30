package uk.gov.justice.schema.catalog.domain;

import java.util.List;

public class Catalog {

    private final List<Group> groups;
    private final String name;

    public Catalog(final String name, final List<Group> groups) {
        this.groups = groups;
        this.name = name;
    }

    public List<Group> getGroups() {
        return groups;
    }

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
