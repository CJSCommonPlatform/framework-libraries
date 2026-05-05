package uk.gov.justice.schema.catalog.generation.effective;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EffectiveJsonSchemaObjectFactoryTest {

    @InjectMocks
    private EffectiveJsonSchemaObjectFactory effectiveJsonSchemaObjectFactory;

    @Test
    public void shouldCreateDefinitionNameFactory() {
        assertThat(effectiveJsonSchemaObjectFactory.definitionNameFactory(), is(notNullValue()));
    }

    @Test
    public void shouldCreateJsonSchemaInliner() {
        assertThat(effectiveJsonSchemaObjectFactory.jsonSchemaInliner(), is(notNullValue()));
    }

    @Test
    public void shouldCreateEffectiveJsonSchemaWriter() {
        assertThat(effectiveJsonSchemaObjectFactory.effectiveJsonSchemaWriter(), is(notNullValue()));
    }

    @Test
    public void shouldCreateEffectiveJsonSchemaGenerator() {
        assertThat(effectiveJsonSchemaObjectFactory.effectiveJsonSchemaGenerator(), is(notNullValue()));
    }

    @Test
    public void shouldCreateCatalogJsonSchemaLoader() {
        assertThat(effectiveJsonSchemaObjectFactory.catalogJsonSchemaLoader(), is(notNullValue()));
    }
}
