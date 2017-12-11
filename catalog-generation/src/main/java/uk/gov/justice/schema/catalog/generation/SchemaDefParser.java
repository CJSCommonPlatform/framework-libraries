package uk.gov.justice.schema.catalog.generation;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static uk.gov.justice.schema.catalog.generation.CatalogGenerationContext.AN_EMPTY_STRING;

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
 * For example:<br/>
 * Given an absolute {@link URL} of a json file
 * <pre>file:/path/to/raml/json/schema/group/some/path/some-schema-or-other.json</pre>
 * and a path to the json files (a sub path to locate the root of the json schemas)
 * <pre>json/schema/</pre>
 * would give us the remaining sub path:
 * <pre>group/some/path/some-schema-or-other.json</pre>
 * which would give use the group name
 * <pre>group</pre>
 * and the base location
 * <pre>group/</pre>
 * and the schema location:
 * <pre>some/path/some-schema-or-other.json</pre>
 *
 *
 */
public class SchemaDefParser {

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

            final String jsonSchemaPathString = jsonSchemaPath.toString();
            final String relativeUri = fileUrl.substring(fileUrl.indexOf(jsonSchemaPathString) + jsonSchemaPathString.length() + 1);

            final int firstSlashIndex = relativeUri.indexOf('/');

            final String groupName = getGroup(relativeUri, firstSlashIndex);
            final String baseLocation = getBaseLocation(groupName, firstSlashIndex);
            final String location = relativeUri.substring(firstSlashIndex + 1);

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

    private String getBaseLocation(final String groupName, final int firstSlashIndex) {

        if(firstSlashIndex == -1) {
            return AN_EMPTY_STRING;
        }

        return groupName + "/";
    }
}
