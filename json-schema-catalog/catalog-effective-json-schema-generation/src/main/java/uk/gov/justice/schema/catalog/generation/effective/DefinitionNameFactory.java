package uk.gov.justice.schema.catalog.generation.effective;

/**
 * Creates safe JSON Schema definition names from schema ID URLs, for use when
 * inlining referenced schemas into an effective JSON schema document.
 *
 * Example: {@code http://justice.gov.uk/standards/complex_address.json}
 * becomes {@code justice_gov_uk_standards_complex_address}
 */
public class DefinitionNameFactory {

    public String createFor(final String schemaId) {
        String name = schemaId.replaceFirst("^https?://", "");
        name = name.replaceAll("[^a-zA-Z0-9]+", "_");
        name = name.replaceAll("_json$", "");
        name = name.replaceAll("^_+|_+$", "");
        return name;
    }
}
