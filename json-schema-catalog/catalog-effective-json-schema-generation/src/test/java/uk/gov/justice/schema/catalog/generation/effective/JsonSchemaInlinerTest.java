package uk.gov.justice.schema.catalog.generation.effective;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JsonSchemaInlinerTest {

    @Mock
    private DefinitionNameFactory definitionNameFactory;

    @InjectMocks
    private JsonSchemaInliner jsonSchemaInliner;

    @Test
    public void shouldReturnSchemaUnchangedIfNoExternalRefs() {
        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "type": "object",
                  "properties": {
                    "name": { "type": "string" }
                  }
                }
                """);

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of());

        assertThat(result.getString("id"), is("http://example.com/person.json"));
        assertThat(result.getString("type"), is("object"));
        assertThat(result.has("definitions"), is(false));
    }

    @Test
    public void shouldLeaveLocalRefUnchanged() {
        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "type": "object",
                  "properties": {
                    "address": { "$ref": "#/definitions/address" }
                  },
                  "definitions": {
                    "address": { "type": "object" }
                  }
                }
                """);

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of());

        assertThat(result.getJSONObject("properties").getJSONObject("address").getString("$ref"),
                is("#/definitions/address"));
    }

    @Test
    public void shouldInlineExternalRefAndRewriteToLocalDefinition() {
        final String addressSchemaJson = """
                {
                  "id": "http://example.com/address.json",
                  "type": "object",
                  "properties": {
                    "city": { "type": "string" }
                  }
                }
                """;

        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "type": "object",
                  "properties": {
                    "homeAddress": { "$ref": "http://example.com/address.json" }
                  }
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/address.json"))
                .thenReturn("example_com_address");

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of(
                "http://example.com/address.json", addressSchemaJson));

        assertThat(result.getJSONObject("properties").getJSONObject("homeAddress").getString("$ref"),
                is("#/definitions/example_com_address"));
        assertThat(result.getJSONObject("definitions").has("example_com_address"), is(true));
        assertThat(result.getJSONObject("definitions")
                .getJSONObject("example_com_address").getString("type"), is("object"));
    }

    @Test
    public void shouldInlineExternalRefWithFragment() {
        final String addressSchemaJson = """
                {
                  "id": "http://example.com/address.json",
                  "type": "object",
                  "definitions": {
                    "postal": { "type": "object", "properties": { "postcode": { "type": "string" } } }
                  }
                }
                """;

        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "type": "object",
                  "properties": {
                    "address": { "$ref": "http://example.com/address.json#/definitions/postal" }
                  }
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/address.json"))
                .thenReturn("example_com_address");

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of(
                "http://example.com/address.json", addressSchemaJson));

        assertThat(result.getJSONObject("properties").getJSONObject("address").getString("$ref"),
                is("#/definitions/example_com_address/definitions/postal"));
        assertThat(result.getJSONObject("definitions").has("example_com_address"), is(true));
    }

    @Test
    public void shouldLeaveRefUnchangedWhenSchemaNotFoundInCatalog() {
        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "type": "object",
                  "properties": {
                    "address": { "$ref": "http://example.com/unknown.json" }
                  }
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/unknown.json"))
                .thenReturn("example_com_unknown");

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of());

        assertThat(result.getJSONObject("properties").getJSONObject("address").getString("$ref"),
                is("http://example.com/unknown.json"));
        assertThat(result.has("definitions"), is(false));
    }

    @Test
    public void shouldMergeInlinedDefinitionsWithExistingDefinitions() {
        final String addressSchemaJson = """
                {
                  "id": "http://example.com/address.json",
                  "type": "object"
                }
                """;

        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "type": "object",
                  "properties": {
                    "address": { "$ref": "http://example.com/address.json" }
                  },
                  "definitions": {
                    "existing": { "type": "string" }
                  }
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/address.json"))
                .thenReturn("example_com_address");

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of(
                "http://example.com/address.json", addressSchemaJson));

        assertThat(result.getJSONObject("definitions").has("existing"), is(true));
        assertThat(result.getJSONObject("definitions").has("example_com_address"), is(true));
    }

    @Test
    public void shouldHandleCircularReferencesWithoutInfiniteRecursion() {
        final String schemaAJson = """
                {
                  "id": "http://example.com/a.json",
                  "type": "object",
                  "properties": {
                    "b": { "$ref": "http://example.com/b.json" }
                  }
                }
                """;

        final String schemaBJson = """
                {
                  "id": "http://example.com/b.json",
                  "type": "object",
                  "properties": {
                    "a": { "$ref": "http://example.com/a.json" }
                  }
                }
                """;

        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/root.json",
                  "properties": {
                    "a": { "$ref": "http://example.com/a.json" }
                  }
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/a.json")).thenReturn("example_com_a");
        when(definitionNameFactory.createFor("http://example.com/b.json")).thenReturn("example_com_b");

        final Map<String, String> allSchemas = Map.of(
                "http://example.com/a.json", schemaAJson,
                "http://example.com/b.json", schemaBJson);

        final JSONObject result = jsonSchemaInliner.inline(schema, allSchemas);

        assertThat(result.getJSONObject("definitions").has("example_com_a"), is(true));
        assertThat(result.getJSONObject("definitions").has("example_com_b"), is(true));
    }

    @Test
    public void shouldInlineRefsNestedInsideArrays() {
        final String addressSchemaJson = """
                { "id": "http://example.com/address.json", "type": "object" }
                """;

        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "allOf": [
                    { "$ref": "http://example.com/address.json" }
                  ]
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/address.json"))
                .thenReturn("example_com_address");

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of(
                "http://example.com/address.json", addressSchemaJson));

        assertThat(result.getJSONArray("allOf").getJSONObject(0).getString("$ref"),
                is("#/definitions/example_com_address"));
    }

    @Test
    public void shouldNotAddDefinitionEntryTwiceForSameRef() {
        final String addressSchemaJson = """
                { "id": "http://example.com/address.json", "type": "object" }
                """;

        final JSONObject schema = new JSONObject("""
                {
                  "id": "http://example.com/person.json",
                  "properties": {
                    "home": { "$ref": "http://example.com/address.json" },
                    "work": { "$ref": "http://example.com/address.json" }
                  }
                }
                """);

        when(definitionNameFactory.createFor("http://example.com/address.json"))
                .thenReturn("example_com_address");

        final JSONObject result = jsonSchemaInliner.inline(schema, Map.of(
                "http://example.com/address.json", addressSchemaJson));

        assertThat(result.getJSONObject("definitions").length(), is(1));
    }
}
