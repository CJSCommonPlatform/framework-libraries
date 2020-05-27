package uk.gov.justice.services.common.json;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultJsonParserTest {

    @InjectMocks
    private DefaultJsonParser defaultJsonParser;

    @Test
    public void shouldConvertFromAJsonStringToAPojo() throws Exception {

        final JsonPojo jsonPojo = new JsonPojo("the-name", true, 23);

        final String json = defaultJsonParser.fromObject(jsonPojo);

        assertThat(defaultJsonParser.toObject(json, JsonPojo.class), is(jsonPojo));
    }

    @Test
    public void shouldThrowARuntimeExceptionIfTheObjectCannotBeParsedIntoJson() throws Exception {

        try {
            final String s = defaultJsonParser.fromObject(new Object());
            fail();
        } catch (RuntimeException expected) {
            assertThat(expected.getCause(), is(instanceOf(IOException.class)));
            assertThat(expected.getMessage(), is("Failed to convert java.lang.Object to json"));
        }
    }

    @Test
    public void shouldThrowARuntimeExceptionIfTheJsonCannotBeParsedIntoAnObject() throws Exception {

        try {
            defaultJsonParser.toObject("You shall not parse!!!", JsonPojo.class);
            fail();
        } catch (RuntimeException expected) {
            assertThat(expected.getCause(), is(instanceOf(IOException.class)));
            assertThat(expected.getMessage(), is("Failed to convert json 'You shall not parse!!!' to uk.gov.justice.services.common.json.DefaultJsonParserTest$JsonPojo"));
        }
    }

    public static class JsonPojo {
        private final String name;
        private final boolean cool;
        private final Integer howCool;

        public JsonPojo(final String name, final boolean cool, final Integer howCool) {
            this.name = name;
            this.cool = cool;
            this.howCool = howCool;
        }

        public String getName() {
            return name;
        }

        public boolean isCool() {
            return cool;
        }

        public Integer getHowCool() {
            return howCool;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final JsonPojo that = (JsonPojo) o;
            return isCool() == that.isCool() &&
                    Objects.equals(getName(), that.getName()) &&
                    Objects.equals(getHowCool(), that.getHowCool());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), isCool(), getHowCool());
        }
    }
}
