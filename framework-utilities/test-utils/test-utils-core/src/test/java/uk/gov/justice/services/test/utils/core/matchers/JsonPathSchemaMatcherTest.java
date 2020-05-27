package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.matchers.JsonPathSchemaMatcher.matchesSchema;

import com.jayway.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class JsonPathSchemaMatcherTest {

    @Test
    public void shouldValidatAgainstValidObjectSchema() throws  Exception {
        String jsonLocation = "uk/gov/justive/service/test/utils/core/matchers/JsonPathSchemaMatcher-valid.json";
        String schemaLocation = "uk/gov/justive/service/test/utils/core/matchers/JsonPathSchemaMatcher-valid-schema.json";

        JsonPath json = new JsonPath(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(jsonLocation));

        assertThat(json, matchesSchema(schemaLocation, JSONObject.class));
    }

    @Test
    public void shouldNotValidateAgainstInvalidSchema() throws Exception {
        String jsonLocation = "uk/gov/justive/service/test/utils/core/matchers/JsonPathSchemaMatcher-valid.json";
        String schemaLocation = "uk/gov/justive/service/test/utils/core/matchers/JsonPathSchemaMatcher-invalid-schema.json";

        JsonPath json = new JsonPath(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(jsonLocation));

        assertThat(json, not(matchesSchema(schemaLocation, JSONObject.class)));
    }

    @Test
    public void shouldValidatAgainstValidObjectSchemaWhenSchemaArray() throws  Exception {
        String jsonLocation = "uk/gov/justive/service/test/utils/core/matchers/JsonPathSchemaMatcher-valid-array.json";
        String schemaLocation = "uk/gov/justive/service/test/utils/core/matchers/JsonPathSchemaMatcher-valid-array-schema.json";

        JsonPath json = new JsonPath(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(jsonLocation));

        assertThat(json, matchesSchema(schemaLocation, JSONArray.class));
    }
}
