package uk.gov.justice.schema.catalog.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.schema.catalog.CatalogUpdater;
import uk.gov.justice.schema.catalog.JsonSchemaFileLoader;
import uk.gov.justice.schema.catalog.RawCatalog;
import uk.gov.justice.schema.catalog.SchemaCatalogException;
import uk.gov.justice.schema.catalog.exception.InvalidJsonFileException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.json.stream.JsonParsingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogUpdaterTest {

    @Mock
    private JsonSchemaFileLoader jsonSchemaFileLoader;

    @InjectMocks
    private RawCatalog rawCatalog;

    @InjectMocks
    private CatalogUpdater catalogUpdater;

    @Test
    public void shouldCacheFileWithReferenceToAnotherSchema() {

        final Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();
        final Path basePath = Paths.get((""));

        schemaIdsToRawJsonSchemaCache.put("http://justice.gov.uk/standards/address.json", "json schema");

        rawCatalog.initialize();

        final Collection<Path> paths = new ArrayList<>();

        try {
            final File aliasJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.alias.json").toURI());
            final File personJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.person-updated.json").toURI());
            paths.add(Paths.get(aliasJson.toURI()));
            paths.add(Paths.get(personJson.toURI()));
            catalogUpdater.updateRawCatalog(schemaIdsToRawJsonSchemaCache, basePath, paths);
            assertThat(schemaIdsToRawJsonSchemaCache.size(), greaterThan(1));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldFailIfFileWithReferenceToAnotherSchemaHasNoId() throws Exception {

        final Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();
        final Path basePath = Paths.get((""));

        schemaIdsToRawJsonSchemaCache.put("http://justice.gov.uk/standards/address.json", "json schema");

        rawCatalog.initialize();

        final Collection<Path> paths = new ArrayList<>();

        final File aliasJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.alias.missing.id.json").toURI());

        paths.add(Paths.get(aliasJson.toURI()));
        catalogUpdater.updateRawCatalog(schemaIdsToRawJsonSchemaCache, basePath, paths);

        assertThat(schemaIdsToRawJsonSchemaCache.size(), is(1));
    }

    @Test
    public void shouldFailIfLoadingFileWithReferenceToAnotherSchemaThrowsAnIOException() throws Exception {

        final Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();
        final Path basePath = Paths.get((""));

        schemaIdsToRawJsonSchemaCache.put("http://justice.gov.uk/standards/address.json", "json schema");

        rawCatalog.initialize();

        final Collection<Path> paths = new ArrayList<>();

        final URL aliasJson = new URL("file:/this/file/does/not/exist.json");

        paths.add(Paths.get(aliasJson.toURI()));

        try {
            catalogUpdater.updateRawCatalog(schemaIdsToRawJsonSchemaCache, basePath, paths);
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(instanceOf(IOException.class)));
            assertThat(expected.getMessage(), is("Failed to extract id from schema file 'file:/this/file/does/not/exist.json'"));
        }
    }

    @Test
    public void shouldThrowExceptionWhenParsingInvalidJsonFile() throws URISyntaxException {

        final Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();
        final Path basePath = Paths.get((""));

        schemaIdsToRawJsonSchemaCache.put("http://justice.gov.uk/standards/address.json", "json schema");

        rawCatalog.initialize();

        final Collection<Path> paths = new ArrayList<>();

        final File incompleteJson = new File(this.getClass().getClassLoader().getResource("json/dodgy-schemas/incomplete-schema-missing-closing-brace.json").toURI());
        paths.add(Paths.get(incompleteJson.toURI()));
        try {
            catalogUpdater.updateRawCatalog(schemaIdsToRawJsonSchemaCache, basePath, paths);
            fail("Should have thrown an exception for parsing invalid json file");
        } catch (final InvalidJsonFileException e) {
            assertThat(e.getCause(), instanceOf(JsonParsingException.class));
            assertThat(e.getMessage(), containsString("json/dodgy-schemas/incomplete-schema-missing-closing-brace.json"));
        }

    }
}
