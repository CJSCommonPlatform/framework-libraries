package uk.gov.justice.schema.catalog.generation;

import java.net.URI;
import java.net.URL;

public class SchemaDef {

    private final URL schemaFile;
    private final URI id;
    private final String groupName;
    private final String baseLocation;
    private final String location;

    public SchemaDef(
            final URL schemaFile,
            final URI id,
            final String groupName,
            final String baseLocation,
            final String location) {
        this.schemaFile = schemaFile;
        this.id = id;
        this.groupName = groupName;
        this.baseLocation = baseLocation;
        this.location = location;
    }

    public URL getSchemaFile() {
        return schemaFile;
    }

    public URI getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public String getLocation() {
        return location;
    }
}
