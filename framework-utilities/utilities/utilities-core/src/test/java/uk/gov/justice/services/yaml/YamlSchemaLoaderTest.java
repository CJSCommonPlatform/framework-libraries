package uk.gov.justice.services.yaml;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import org.everit.json.schema.Schema;
import org.junit.jupiter.api.Test;

public class YamlSchemaLoaderTest {

    private static final String SUBSCRIPTION_SCHEMA_SCHEMA_PATH = "/schema/subscription-schema.json";

    @Test
    public void shouldCreateSchema() throws Exception {
        final YamlSchemaLoader yamlSchemaLoader = new YamlSchemaLoader();
        final Schema schema = yamlSchemaLoader.loadSchema(SUBSCRIPTION_SCHEMA_SCHEMA_PATH);

        assertThat(schema, is(notNullValue()));
    }
}