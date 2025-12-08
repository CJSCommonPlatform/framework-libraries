package uk.gov.justice.services.test.utils.core.messaging;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import uk.gov.justice.services.test.utils.core.random.BigDecimalGenerator;

import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class JsonObjectBuilderWrapperTest {
    public static final JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);

    private JsonObjectBuilderWrapper wrapper;

    @BeforeEach
    public void setup() {
        wrapper = new JsonObjectBuilderWrapper();
    }

    @Test
    public void testAddingJsonArrayAsAttributeToRootLevelJsonObject() {
        final JsonArray numberJsonArray = jsonBuilderFactory.createArrayBuilder().add(1).add(2).build();

        final String firstLevelAttributeName = "numbers";
        wrapper.add(numberJsonArray, firstLevelAttributeName);
        final JsonObject actual = wrapper.build();

        assertThat(actual, notNullValue());
        assertThat(actual.getJsonArray(firstLevelAttributeName), notNullValue());
        assertThat(actual.getJsonArray(firstLevelAttributeName), hasSize(2));
    }

    @Test
    public void testAddingJsonArrayAsAttributeAsNestedAttribute() {
        final JsonArray numberJsonArray = jsonBuilderFactory.createArrayBuilder().add(1).add(2).build();

        final String firstLevelAttributeName = "example";
        final String secondLevelAttributeName = "numbers";
        wrapper.add(numberJsonArray, firstLevelAttributeName, secondLevelAttributeName);
        final JsonObject actual = wrapper.build();

        assertThat(actual, notNullValue());
        final JsonObject firstLevelJsonObject = actual.getJsonObject(firstLevelAttributeName);
        assertThat(firstLevelJsonObject, notNullValue());
        assertThat(firstLevelJsonObject.getJsonArray(secondLevelAttributeName), hasSize(2));
    }

    @Test
    public void testBuildingJsonObjectWithAllTypesOfAttributes() {

        final String jsonArrayAttributeName = "jsonArray";
        final String jsonObjectAttributeName = "jsonObject";
        final String integerAttributeName = "integer";
        final String stringAttributeName = "string";
        final String booleanAttributeName = "boolean";
        final String bigDecimalAttributeName = "bigDecimal";
        final String jsonArrayBuilderAttributeName = "jsonArrayBuilder";


        final JsonArray jsonArray = jsonBuilderFactory.createArrayBuilder().add(1).add(2).build();
        final JsonArrayBuilder jsonArrayBuilder = jsonBuilderFactory.createArrayBuilder().add(3).add(4);
        final JsonObject jsonObject = jsonBuilderFactory.createObjectBuilder().add("key", "value").build();
        final int integerAttributeValue = 500;
        final String stringAttributeValue = randomAlphanumeric(10);
        final boolean booleanAttributeValue = true;
        final BigDecimal bigDecimalAttributeValue = new BigDecimalGenerator().next();

        wrapper.add(jsonArray, jsonArrayAttributeName);
        wrapper.add(jsonArrayBuilder, jsonArrayBuilderAttributeName);
        wrapper.add(jsonObject, jsonObjectAttributeName);
        wrapper.add(integerAttributeValue, integerAttributeName);
        wrapper.add(stringAttributeValue, stringAttributeName);
        wrapper.add(booleanAttributeValue, booleanAttributeName);
        wrapper.add(bigDecimalAttributeValue, bigDecimalAttributeName);


        final JsonObject actual = wrapper.build();

        assertThat(actual, notNullValue());

        final JsonArray actualJsonArray = actual.getJsonArray(jsonArrayAttributeName);
        assertThat(actualJsonArray, hasSize(2));

        final JsonArray actualJsonArrayFromBuilder = actual.getJsonArray(jsonArrayBuilderAttributeName);
        assertThat(actualJsonArrayFromBuilder, hasSize(2));

        final JsonObject actualJsonObject = actual.getJsonObject(jsonObjectAttributeName);
        assertThat(actualJsonObject, notNullValue());
        assertThat(actualJsonObject.getString("key"), is("value"));

        assertThat(actual.getInt(integerAttributeName), is(integerAttributeValue));
        assertThat(actual.getBoolean(booleanAttributeName), is(booleanAttributeValue));
        assertThat(actual.getJsonNumber(bigDecimalAttributeName).bigDecimalValue(), is(bigDecimalAttributeValue));
    }

}