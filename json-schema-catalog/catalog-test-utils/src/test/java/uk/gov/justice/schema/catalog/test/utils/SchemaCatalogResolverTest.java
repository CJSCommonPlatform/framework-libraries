package uk.gov.justice.schema.catalog.test.utils;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.schema.catalog.test.utils.SchemaCatalogResolver.schemaCatalogResolver;

import java.io.IOException;
import java.util.List;

import com.google.common.io.Resources;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class SchemaCatalogResolverTest {

    @Test
    public void shouldResolveSchemaFromJsonObjectWithReferences() throws Exception {
        final Schema schema = schemaCatalogResolver()
                .loadSchema(loadJsonObject("json/schema/example.add-recipe.json"));

        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> schema.validate(loadJsonObject("json/add-recipe.json")));
        final List<String> allMessages = validationException.getAllMessages();

        assertThat(allMessages.size(), is(1));
        assertThat(allMessages.get(0), is("#/ingredients/0/quantity: expected type: Number, found: String"));
    }

    @Test
    public void shouldResolveSchemaFromJsonStringWithReferences() throws Exception {
            final Schema schema = schemaCatalogResolver()
                    .loadSchema(loadStringResource("json/schema/example.add-recipe.json"));

            final ValidationException validationException = assertThrows(
                    ValidationException.class,
                    () -> schema.validate(loadJsonObject("json/add-recipe.json")));
            final List<String> allMessages = validationException.getAllMessages();

            assertThat(allMessages.size(), is(1));
            assertThat(allMessages.get(0), is("#/ingredients/0/quantity: expected type: Number, found: String"));
    }

    private JSONObject loadJsonObject(final String resourcePath) throws IOException {
        return new JSONObject(loadStringResource(resourcePath));
    }

    private String loadStringResource(final String resourcePath) throws IOException {
        return Resources.toString(getResource(resourcePath), UTF_8);
    }
}