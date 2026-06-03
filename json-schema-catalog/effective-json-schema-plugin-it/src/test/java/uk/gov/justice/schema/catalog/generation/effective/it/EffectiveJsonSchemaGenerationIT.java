package uk.gov.justice.schema.catalog.generation.effective.it;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Integration test that verifies the effective-json-schema-maven-plugin correctly generates
 * effective JSON schema files with all external {@code $ref} values inlined.
 *
 * The plugin runs at {@code process-sources} phase and writes output to
 * {@code target/effective-json-schemas/}. This test is deliberately placed in the
 * test phase (after process-sources) so the generated files are already present.
 */
@ExtendWith(MockitoExtension.class)
public class EffectiveJsonSchemaGenerationIT {

    private static final Path EFFECTIVE_SCHEMAS_DIR =
            Paths.get("target/effective-json-schemas");

    @Test
    public void shouldGenerateEffectiveJsonSchemaForSimpleSchemaWithNoExternalRefs() throws Exception {
        final Path effectiveAddressSchema = EFFECTIVE_SCHEMAS_DIR.resolve("standards/address.json");

        assertThat("Effective schema file should exist: " + effectiveAddressSchema,
                Files.exists(effectiveAddressSchema), is(true));

        final JSONObject schema = new JSONObject(Files.readString(effectiveAddressSchema));

        assertThat(schema.getString("id"), is("http://justice.gov.uk/standards/address.json"));
        assertThat(schema.getString("type"), is("object"));
        assertThat(schema.getJSONObject("properties").has("city"), is(true));
    }

    @Test
    public void shouldGenerateEffectiveJsonSchemaForPersonWithExternalRefsInlined() throws Exception {
        final Path effectivePersonSchema = EFFECTIVE_SCHEMAS_DIR.resolve("context/person.json");

        assertThat("Effective schema file should exist: " + effectivePersonSchema,
                Files.exists(effectivePersonSchema), is(true));

        final JSONObject schema = new JSONObject(Files.readString(effectivePersonSchema));

        assertThat(schema.getString("id"), is("http://justice.gov.uk/context/person.json"));
        assertThat(schema.getString("type"), is("object"));
    }

    @Test
    public void shouldInlineAddressSchemaDefinitionIntoPersonEffectiveSchema() throws Exception {
        final Path effectivePersonSchema = EFFECTIVE_SCHEMAS_DIR.resolve("context/person.json");
        final JSONObject schema = new JSONObject(Files.readString(effectivePersonSchema));

        final JSONObject definitions = schema.getJSONObject("definitions");

        assertThat("Effective person schema should contain inlined address definition",
                definitions.has("justice_gov_uk_standards_address"), is(true));

        final JSONObject inlinedAddress = definitions.getJSONObject("justice_gov_uk_standards_address");
        assertThat(inlinedAddress.getString("type"), is("object"));
        assertThat(inlinedAddress.getJSONObject("properties").has("city"), is(true));
        assertThat(inlinedAddress.getJSONObject("properties").has("postcode"), is(true));
    }

    @Test
    public void shouldInlineComplexAddressSchemaDefinitionIntoPersonEffectiveSchema() throws Exception {
        final Path effectivePersonSchema = EFFECTIVE_SCHEMAS_DIR.resolve("context/person.json");
        final JSONObject schema = new JSONObject(Files.readString(effectivePersonSchema));

        final JSONObject definitions = schema.getJSONObject("definitions");

        assertThat("Effective person schema should contain inlined complex_address definition",
                definitions.has("justice_gov_uk_standards_complex_address"), is(true));
    }

    @Test
    public void shouldRewriteExternalRefToLocalDefinitionInPersonEffectiveSchema() throws Exception {
        final Path effectivePersonSchema = EFFECTIVE_SCHEMAS_DIR.resolve("context/person.json");
        final JSONObject schema = new JSONObject(Files.readString(effectivePersonSchema));

        final String homeAddressRef = schema.getJSONObject("properties")
                .getJSONObject("home_address")
                .getString("$ref");

        assertThat("home_address $ref should point to a local definition",
                homeAddressRef.startsWith("#/definitions/"), is(true));
        assertThat(homeAddressRef, is("#/definitions/justice_gov_uk_standards_address"));
    }

    @Test
    public void shouldRewriteFragmentRefToLocalDefinitionInPersonEffectiveSchema() throws Exception {
        final Path effectivePersonSchema = EFFECTIVE_SCHEMAS_DIR.resolve("context/person.json");
        final JSONObject schema = new JSONObject(Files.readString(effectivePersonSchema));

        final String correspondenceRef = schema.getJSONObject("properties")
                .getJSONObject("correspondence_address")
                .getString("$ref");

        assertThat("correspondence_address $ref should point to a local definition with fragment path",
                correspondenceRef.startsWith("#/definitions/"), is(true));
        assertThat(correspondenceRef,
                is("#/definitions/justice_gov_uk_standards_complex_address/definitions/complex_address"));
    }

    @Test
    public void shouldPreserveRelativeDirectoryStructureOfOutputFiles() {
        assertThat(Files.exists(EFFECTIVE_SCHEMAS_DIR.resolve("standards/address.json")), is(true));
        assertThat(Files.exists(EFFECTIVE_SCHEMAS_DIR.resolve("standards/complex_address.json")), is(true));
        assertThat(Files.exists(EFFECTIVE_SCHEMAS_DIR.resolve("context/person.json")), is(true));
    }
}
