package uk.gov.justice.schema.catalog.pojo;

import java.lang.String;
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

  public static Builder catalog() {
    return new Catalog.Builder();
  }

  public static class Builder {
    private List<Group> group;

    private String name;

    public Builder withGroup(final List<Group> group) {
      this.group = group;
      return this;
    }

    public Builder withName(final String name) {
      this.name = name;
      return this;
    }

    public Catalog build() {
      return new Catalog(group, name);
    }
  }
}
