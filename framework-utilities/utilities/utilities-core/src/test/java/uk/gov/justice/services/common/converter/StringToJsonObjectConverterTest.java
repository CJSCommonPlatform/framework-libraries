package uk.gov.justice.services.common.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.json.JsonObject;

import org.junit.Test;

public class StringToJsonObjectConverterTest {

    private static final String ID = "861c9430-7bc6-4bf0-b549-6534394b8d65";
    private static final String NAME = "test name";
    private static final String NESTED_ID = "c3f7182b-bd20-4678-ba8b-e7e5ea8629c3";
    private static final String NESTED_NAME = "Nested Name";

    private static final String JSON_OBJECT_STRING = "{\n" +
            "  \"id\": \"861c9430-7bc6-4bf0-b549-6534394b8d65\",\n" +
            "  \"name\": \"test name\",\n" +
            "  \"nested\": {\n" +
            "    \"id\": \"c3f7182b-bd20-4678-ba8b-e7e5ea8629c3\",\n" +
            "    \"name\": \"Nested Name\"\n" +
            "  }\n" +
            "}";

    @Test
    public void shouldConvertStringToJsonObject() throws Exception {
        final StringToJsonObjectConverter stringToJsonObjectConverter = new StringToJsonObjectConverter();

        final JsonObject joTest = stringToJsonObjectConverter.convert(JSON_OBJECT_STRING);

        assertThat(joTest, notNullValue());
        assertThat(joTest.getString("id"), equalTo(ID));
        assertThat(joTest.getString("name"), equalTo(NAME));
        final JsonObject joNested = joTest.getJsonObject("nested");

        assertThat(joNested, notNullValue());
        assertThat(joNested.getString("id"), equalTo(NESTED_ID));
        assertThat(joNested.getString("name"), equalTo(NESTED_NAME));
    }
}