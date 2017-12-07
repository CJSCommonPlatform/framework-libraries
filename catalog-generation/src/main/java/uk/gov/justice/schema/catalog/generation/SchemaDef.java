package uk.gov.justice.schema.catalog.generation;

import java.net.URI;
import java.net.URL;

/**
 * The file location, id, group name, base location and location of a json schema file.
 *
 * See {@link SchemaDefParser} for an explanation of the terms
 *
 */
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

    /**
     * @return The {@link URL} of the json schema file
     */
    public URL getSchemaFile() {
        return schemaFile;
    }

    /**
     * @return The id of the json schema file
     */
    public URI getId() {
        return id;
    }

    /**
     * @return The group name 
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @return The base location of the schema files
     */
    public String getBaseLocation() {
        return baseLocation;
    }

    /**
     * @return The location of the schema file
     */
    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "SchemaDef{" +
                "schemaFile=" + schemaFile +
                ", id=" + id +
                ", groupName='" + groupName + '\'' +
                ", baseLocation='" + baseLocation + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
