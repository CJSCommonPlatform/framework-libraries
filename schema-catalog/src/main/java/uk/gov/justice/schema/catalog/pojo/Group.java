package uk.gov.justice.schema.catalog.pojo;

import java.lang.String;
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

  public static Builder group() {
    return new Group.Builder();
  }

  public static class Builder {
    private String baseLocation;

    private String name;

    private List<Schema> schema;

    public Builder withBaseLocation(final String baseLocation) {
      this.baseLocation = baseLocation;
      return this;
    }

    public Builder withName(final String name) {
      this.name = name;
      return this;
    }

    public Builder withSchema(final List<Schema> schema) {
      this.schema = schema;
      return this;
    }

    public Group build() {
      return new Group(baseLocation, name, schema);
    }
  }
}
