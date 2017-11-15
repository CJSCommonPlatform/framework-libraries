package uk.gov.justice.schema.catalog.domain;

import java.util.List;

public class Catalog {

    private final List<Group> group;
    private final String name;

    public Catalog(final List<Group> group, final String name) {
        this.group = group;
        this.name = name;
    }

    public List<Group> getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

}
