package uk.gov.justice.schema.catalog.generation;

import static uk.gov.justice.schema.catalog.generation.CatalogGenerationContext.AN_EMPTY_STRING;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public class SchemaDefParser {

    private final SchemaIdParser schemaIdParser;

    public SchemaDefParser(final SchemaIdParser schemaIdParser) {
        this.schemaIdParser = schemaIdParser;
    }

    public SchemaDef parse(final URL schemaFile, final Path jsonSchemaPath) {

        final URI id = schemaIdParser.parse(schemaFile);
        final String fileUrl = schemaFile.toString();

        final String jsonSchemaPathString = jsonSchemaPath.toString();
        final String relativeUri = fileUrl.substring(fileUrl.indexOf(jsonSchemaPathString) + jsonSchemaPathString.length() + 1);

        final int firstSlashIndex = relativeUri.indexOf('/');

        final String groupName = getGroup(relativeUri, firstSlashIndex);
        final String baseLocation = getBaseLocation(groupName, firstSlashIndex);
        final String location = relativeUri.substring(firstSlashIndex + 1);

        return new SchemaDef(schemaFile, id, groupName, baseLocation, location);
    }

    private String getGroup(final String relativeUri, final int firstSlashIndex) {

        if(firstSlashIndex == -1) {
            return AN_EMPTY_STRING;
        }

        return relativeUri.substring(0, firstSlashIndex);
    }

    private String getBaseLocation(final String groupName, final int firstSlashIndex) {

        if(firstSlashIndex == -1) {
            return AN_EMPTY_STRING;
        }

        return groupName + "/";
    }
}
