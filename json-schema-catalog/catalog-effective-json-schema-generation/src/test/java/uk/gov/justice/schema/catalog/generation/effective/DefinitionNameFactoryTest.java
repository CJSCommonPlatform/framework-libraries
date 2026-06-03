package uk.gov.justice.schema.catalog.generation.effective;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class DefinitionNameFactoryTest {

    private final DefinitionNameFactory definitionNameFactory = new DefinitionNameFactory();

    @Test
    public void shouldConvertHttpUrlToSafeDefinitionName() {
        final String result = definitionNameFactory.createFor("http://justice.gov.uk/standards/address.json");
        assertThat(result, is("justice_gov_uk_standards_address"));
    }

    @Test
    public void shouldConvertHttpsUrlToSafeDefinitionName() {
        final String result = definitionNameFactory.createFor("https://justice.gov.uk/standards/address.json");
        assertThat(result, is("justice_gov_uk_standards_address"));
    }

    @Test
    public void shouldHandleNestedPathSegments() {
        final String result = definitionNameFactory.createFor("http://justice.gov.uk/standards/complex_address.json");
        assertThat(result, is("justice_gov_uk_standards_complex_address"));
    }

    @Test
    public void shouldCollapseConsecutiveNonAlphanumericCharacters() {
        final String result = definitionNameFactory.createFor("http://justice.gov.uk/context/person.json");
        assertThat(result, is("justice_gov_uk_context_person"));
    }

    @Test
    public void shouldNotProduceLeadingOrTrailingUnderscores() {
        final String result = definitionNameFactory.createFor("http://example.com/thing.json");
        assertThat(result.charAt(0) != '_', is(true));
        assertThat(result.charAt(result.length() - 1) != '_', is(true));
    }

    @Test
    public void shouldStripTrailingJsonSuffix() {
        final String result = definitionNameFactory.createFor("http://example.com/my-schema.json");
        assertThat(result.endsWith("json"), is(false));
    }
}
