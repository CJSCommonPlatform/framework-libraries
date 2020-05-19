package uk.gov.justice.schema.catalog.domain;

/**
 * Object representation of a Schema in a json catalog file
 */
public class Schema {
    private final String id;

    private final String location;

    public Schema(final String id, final String location) {
        this.id = id;
        this.location = location;
    }

    /**
     * @return The id of the Schema
     */
    public String getId() {
        return id;
    }

    /**
     * @return The Schema's location
     */
    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
