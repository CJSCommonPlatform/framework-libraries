package uk.gov.justice.services.test.utils.core.matchers;

import java.io.InputStream;
import java.net.URL;

import com.google.common.io.Resources;
import com.jayway.restassured.path.json.JsonPath;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matcher for checking json against a json schema.
 */
public class JsonPathSchemaMatcher extends TypeSafeMatcher<JsonPath> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonPathSchemaMatcher.class);

    private String schemaLocation;
    private Class<?> type;

    public JsonPathSchemaMatcher(String schemaLocation, Class<?> type) {
        this.schemaLocation = schemaLocation;
        this.type = type;
    }

    @Override
    protected boolean matchesSafely(final JsonPath jsonPath) {
        final URL resource = Resources.getResource(this.schemaLocation);
        try (InputStream inputStream = resource.openStream()) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            String jsonString = jsonPath.prettify();
            if (type == JSONArray.class) {
                schema.validate(new JSONArray(jsonString));
            } else {
                schema.validate(new JSONObject(jsonString));
            }
            return true;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("json to be valid according to the schema at " + schemaLocation);
    }

    /**
     *
     * @param schemaLocation Fully qualified class location of schema.
     * @param type JsonObject or JsonArray.
     * @return
     */
    @Factory
    public static Matcher<JsonPath> matchesSchema(String schemaLocation, Class<?> type) {
        return new JsonPathSchemaMatcher(schemaLocation, type);
    }

}
