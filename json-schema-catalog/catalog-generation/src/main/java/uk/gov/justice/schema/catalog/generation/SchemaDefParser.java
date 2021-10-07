package uk.gov.justice.schema.catalog.generation;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static uk.gov.justice.schema.catalog.CatalogContext.AN_EMPTY_STRING;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Parses an absolute {@link URL} to a json schema file into a {@link SchemaDef} containing:
 * <ul>
 *  <li>The id of the schema file</li>
 *  <li>A group name of the scheme file (basically the path from the root of the schema locations)</li>
 *  <li>The base location (a common path from to schema location root to the schema file)</li>
 *  <li>Schema location (the relative path from base location and group to the schema file)</li>
 *  <li>Schema file (The original unparsed absolute {@link URL} of the json schema file)</li>
 * </ul>
 *
 * For example:
 * Given an absolute {@link URL} of a json file
 * <pre>file:/path/to/raml/json/schema/some/path/some-schema-or-other.json</pre>
 * and a path to the json files (a sub path to locate the root of the json schemas)
 * <pre>json/schema/</pre>
 * would give us the remaining sub path:
 * <pre>some/path/some-schema-or-other.json</pre>
 * which would give use the base location
 * <pre>json/schema/</pre>
 * and the group name
 * <pre>some/path</pre>
 * and the schema location:
 * <pre>some-schema-or-other.json</pre>
 */
public class SchemaDefParser {

    private static final char SLASH = '/';
    private final SchemaIdParser schemaIdParser;

    public SchemaDefParser(final SchemaIdParser schemaIdParser) {
        this.schemaIdParser = schemaIdParser;
    }

    /**
     * Parse an absolute {@link URL} to a json schema file into a {@link SchemaDef} using a path to
     * find the root location of the schema files
     *
     * See above for definitions of the {@link SchemaDef}
     *
     * @param schemaFile The absolute {@link URL} of the json schema file
     * @param jsonSchemaPath a path to the schema root
     * @return The parsed {@link SchemaDef}
     */
    public Optional<SchemaDef> parse(final URL schemaFile, final Path jsonSchemaPath) {

        final Optional<URI> id = schemaIdParser.parse(schemaFile);

        if (id.isPresent()) {
            final String fileUrl = schemaFile.toString();

            // Replace back slashes to support running on Windows
            final String jsonSchemaString = jsonSchemaPath.toString().replace('\\', SLASH);

            final String relativeUri = fileUrl.substring(fileUrl.indexOf(jsonSchemaString) + jsonSchemaString.length() + 1);

            final int lastSlashIndex = relativeUri.lastIndexOf(SLASH);

            final String groupName = getGroup(relativeUri, lastSlashIndex);
            final String baseLocation =  getBaseLocation(jsonSchemaString, groupName);
            final String location = relativeUri.substring(lastSlashIndex + 1);

            final SchemaDef schemaDef = new SchemaDef(schemaFile, id.get(), groupName, baseLocation, location);

            return of(schemaDef);
        }

        return empty();
    }

    private String getGroup(final String relativeUri, final int firstSlashIndex) {

        if(firstSlashIndex == -1) {
            return AN_EMPTY_STRING;
        }

        return relativeUri.substring(0, firstSlashIndex);
    }

    private String getBaseLocation(final String jsonSchemaPath, final String groupName) {

        if (groupName.isEmpty()) {
            return jsonSchemaPath + SLASH;
        }

        return jsonSchemaPath + SLASH + groupName + SLASH;
    }
}
