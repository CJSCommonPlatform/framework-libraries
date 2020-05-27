package uk.gov.justice.schema.catalog.generation;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generates a json {@link Catalog} from a list of json schema files
 */
public class CatalogObjectGenerator {

    private final SchemaDefParser schemaDefParser;

    public CatalogObjectGenerator(final SchemaDefParser schemaDefParser) {
        this.schemaDefParser = schemaDefParser;
    }

    /**
     * Generate a new json {@link Catalog} from a list of json schema files
     * @param catalogName The name of the catalog
     * @param schemaFiles A list of json schema files
     * @param jsonSchemaPath A default root location in the schemas path
     * @return A {@link Catalog} of the json schema files
     */
    public Catalog generate(final String catalogName, final List<URL> schemaFiles, final Path jsonSchemaPath) {

        final List<SchemaDef> schemaDefs = schemaFiles.stream()
                .map(schemaFile -> schemaDefParser.parse(schemaFile, jsonSchemaPath))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

        final List<Group> groups = asGroups(schemaDefs);

        return new Catalog(catalogName, groups);
    }

    private List<Group> asGroups(final List<SchemaDef> schemaDefs) {

        final Map<String, Group> groups = new HashMap<>();

        schemaDefs.forEach(schemaDef -> createGroups(schemaDef, groups));

        return new ArrayList<>(groups.values());
    }

    private void createGroups(final SchemaDef schemaDef, final Map<String, Group> groups) {

        final String groupName = schemaDef.getGroupName();
        if (!groups.containsKey(groupName)) {

            final Group group = new Group(
                    schemaDef.getBaseLocation(),
                    groupName,
                    new ArrayList<>());

            groups.put(groupName, group);
        }

        final Group group = groups.get(groupName);

        final List<Schema> schemas = group.getSchemas();

        schemas.add(new Schema(schemaDef.getId().toString(), schemaDef.getLocation()));
    }
}
