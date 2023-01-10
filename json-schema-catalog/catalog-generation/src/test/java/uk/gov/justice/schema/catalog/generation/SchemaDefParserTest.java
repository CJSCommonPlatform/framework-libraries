package uk.gov.justice.schema.catalog.generation;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static uk.gov.justice.schema.catalog.CatalogContext.AN_EMPTY_STRING;

import uk.gov.justice.schema.catalog.CatalogContext;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaDefParserTest {

    @Spy
    @SuppressWarnings("unused")
    private final CatalogContext catalogContext = new CatalogContext();

    @Mock
    private SchemaIdParser schemaIdParser;

    @InjectMocks
    private SchemaDefParser schemaDefParser;

    @Test
    public void shouldParseTheSchemaUrlIntoIdGroupBaseLocationAndLocation() throws Exception {

        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/some/path/some-schema-or-other.json");

        final String schemaId = "http://justice.gov.uk/context/some-schema-or-other.json";
        when(schemaIdParser.parse(schemaFile)).thenReturn(of(new URL(schemaId).toURI()));

        final Optional<SchemaDef> schemaDefOptional = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        if (schemaDefOptional.isPresent()) {
            final SchemaDef schemaDef = schemaDefOptional.get();
            assertThat(schemaDef.getSchemaFile(), is(schemaFile));
            assertThat(schemaDef.getId().toString(), is(schemaId));
            assertThat(schemaDef.getGroupName(), is("some/path"));
            assertThat(schemaDef.getBaseLocation(), is("json/schema/some/path/"));
            assertThat(schemaDef.getLocation(), is("some-schema-or-other.json"));
        } else {
            fail();
        }
    }

    @Test
    public void shouldHandleSchemasAtTheRootOfTheJsonSchemaDirectory() throws Exception {

        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/some-schema-or-other.json");

        final String schemaId = "http://justice.gov.uk/context/some-schema-or-other.json";
        when(schemaIdParser.parse(schemaFile)).thenReturn(of(new URL(schemaId).toURI()));

        final Optional<SchemaDef> schemaDefOptional = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        if (schemaDefOptional.isPresent()) {
            final SchemaDef schemaDef = schemaDefOptional.get();
            assertThat(schemaDef.getSchemaFile(), is(schemaFile));
            assertThat(schemaDef.getId().toString(), is(schemaId));
            assertThat(schemaDef.getGroupName(), is(AN_EMPTY_STRING));
            assertThat(schemaDef.getBaseLocation(), is("json/schema/"));
            assertThat(schemaDef.getLocation(), is("some-schema-or-other.json"));
        } else {
            fail();
        }
    }

    @Test
    public void shouldParseTheSchemaUrlIntoIdGroupBaseLocationAndLocationWithSubDirectories() throws Exception {

        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/a-sub-directory/another-sub-directory/some-schema-or-other.json");

        final String schemaId = "http://justice.gov.uk/context/some-schema-or-other.json";
        when(schemaIdParser.parse(schemaFile)).thenReturn(of(new URL(schemaId).toURI()));

        final Optional<SchemaDef> schemaDefOptional = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        if (schemaDefOptional.isPresent()) {
            final SchemaDef schemaDef = schemaDefOptional.get();

            assertThat(schemaDef.getSchemaFile(), is(schemaFile));
            assertThat(schemaDef.getId().toString(), is(schemaId));
            assertThat(schemaDef.getGroupName(), is("a-sub-directory/another-sub-directory"));
            assertThat(schemaDef.getBaseLocation(), is("json/schema/a-sub-directory/another-sub-directory/"));
            assertThat(schemaDef.getLocation(), is("some-schema-or-other.json"));
        } else {
            fail();
        }
    }

    @Test
    public void shouldReturnEmptyIfTheSchemaHasNoId() throws Exception {
        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/a-sub-directory/another-sub-directory/some-schema-or-other.json");

        when(schemaIdParser.parse(schemaFile)).thenReturn(empty());

        final Optional<SchemaDef> schemaDefOptional = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        assertThat(schemaDefOptional.isPresent(), is(false));
    }
}
