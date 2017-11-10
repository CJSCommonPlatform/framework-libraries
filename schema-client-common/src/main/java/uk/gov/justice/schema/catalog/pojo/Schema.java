package uk.gov.justice.schema.catalog.pojo;

public class Schema {
    private final String id;

    private final String location;

    public Schema(final String id, final String location) {
        this.id = id;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public static Builder schema() {
        return new Builder();
    }

    public static class Builder {
        private String id;

        private String location;

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withLocation(final String location) {
            this.location = location;
            return this;
        }

        public Schema build() {
            return new Schema(id, location);
        }
    }
}
