package uk.gov.justice.services.common.converter;

import static javax.json.JsonValue.FALSE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import net.minidev.json.JSONValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.internal.runners.JUnit44RunnerImpl;

@RunWith(JUnit4.class)
public class JsonValueFactoryTest {
    private JsonValueFactory jsonValueFactory = new JsonValueFactory();

    @Test
    public void shouldReturnString() {
        final String myString = "myString";
        JsonValue myValue = jsonValueFactory.build(myString);

        assertTrue(myValue instanceof JsonString);
        assertThat(((JsonString)myValue).getString(), is("myString"));
    }

    @Test
    public void shouldReturnNumberFromInteger() {
        final int myInt = 5;
        JsonValue myValue = jsonValueFactory.build(myInt);

        assertTrue(myValue instanceof JsonNumber);
        assertThat(((JsonNumber)myValue).longValue(), is(5l));
    }

    @Test
    public void shouldReturnNumberFromLong() {
        final long myLong = 5l;
        JsonValue myValue = jsonValueFactory.build(myLong);

        assertTrue(myValue instanceof JsonNumber);
        assertThat(((JsonNumber)myValue).longValue(), is(myLong));
    }

    @Test
    public void shouldReturnNumberFromBigDecimal() {
        final BigDecimal myBigDecimal = new BigDecimal(6);
        final JsonValue myValue = jsonValueFactory.build(myBigDecimal);

        assertTrue(myValue instanceof JsonNumber);
        assertThat(((JsonNumber)myValue).bigDecimalValue(), is(myBigDecimal));
    }

    @Test
    public void shouldReturnBooleanFalse() {
        final boolean myBoolean = false;
        final JsonValue myValue = jsonValueFactory.build(myBoolean);

        assertThat(myValue, is(FALSE));
    }
}