package uk.gov.justice.schema.catalog.domain;

import java.util.List;

public class Catalog {

    private final List<Group> group;
    private final String name;

    public Catalog(final String name, final List<Group> group) {
        this.group = group;
        this.name = name;
    }

    public List<Group> getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "group=" + group +
                ", name='" + name + '\'' +
                '}';
    }
}
