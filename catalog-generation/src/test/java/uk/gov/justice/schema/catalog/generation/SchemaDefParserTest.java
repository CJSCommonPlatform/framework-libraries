package uk.gov.justice.schema.catalog.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.justice.schema.catalog.generation.CatalogGenerationContext.AN_EMPTY_STRING;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaDefParserTest {

    @Spy
    @SuppressWarnings("unused")
    private final CatalogGenerationContext catalogGenerationContext = new CatalogGenerationContext();

    @Mock
    private SchemaIdParser schemaIdParser;

    @InjectMocks
    private SchemaDefParser schemaDefParser;

    @Test
    public void shouldParseTheSchemaUrlIntoIdGroupBaseLocationAndLocation() throws Exception {

        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/a-sub-directory/some-schema-or-other.json");

        final String schemaId = "http://justice.gov.uk/context/some-schema-or-other.json";
        when(schemaIdParser.parse(schemaFile)).thenReturn(new URL(schemaId).toURI());

        final SchemaDef schemaDef = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        assertThat(schemaDef.getSchemaFile(), is(schemaFile));
        assertThat(schemaDef.getId().toString(), is(schemaId));
        assertThat(schemaDef.getGroupName(), is("a-sub-directory"));
        assertThat(schemaDef.getBaseLocation(), is("a-sub-directory/"));
        assertThat(schemaDef.getLocation(), is("some-schema-or-other.json"));
    }

    @Test
    public void shouldHandleSchemasAtTheRootOfTheJsonSchemaDirectory() throws Exception {

        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/some-schema-or-other.json");

        final String schemaId = "http://justice.gov.uk/context/some-schema-or-other.json";
        when(schemaIdParser.parse(schemaFile)).thenReturn(new URL(schemaId).toURI());

        final SchemaDef schemaDef = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        assertThat(schemaDef.getSchemaFile(), is(schemaFile));
        assertThat(schemaDef.getId().toString(), is(schemaId));
        assertThat(schemaDef.getGroupName(), is(AN_EMPTY_STRING));
        assertThat(schemaDef.getBaseLocation(), is(AN_EMPTY_STRING));
        assertThat(schemaDef.getLocation(), is("some-schema-or-other.json"));
    }

    @Test
    public void shouldParseTheSchemaUrlIntoIdGroupBaseLocationAndLocationWithSubDirectories() throws Exception {

        final Path jsonSchemaPath = Paths.get("json/schema/");
        final URL schemaFile = new URL("file:/path/to/raml/json/schema/a-sub-directory/another-sub-directory/some-schema-or-other.json");

        final String schemaId = "http://justice.gov.uk/context/some-schema-or-other.json";
        when(schemaIdParser.parse(schemaFile)).thenReturn(new URL(schemaId).toURI());

        final SchemaDef schemaDef = schemaDefParser.parse(schemaFile, jsonSchemaPath);

        assertThat(schemaDef.getSchemaFile(), is(schemaFile));
        assertThat(schemaDef.getId().toString(), is(schemaId));
        assertThat(schemaDef.getGroupName(), is("a-sub-directory"));
        assertThat(schemaDef.getBaseLocation(), is("a-sub-directory/"));
        assertThat(schemaDef.getLocation(), is("another-sub-directory/some-schema-or-other.json"));
    }
}
