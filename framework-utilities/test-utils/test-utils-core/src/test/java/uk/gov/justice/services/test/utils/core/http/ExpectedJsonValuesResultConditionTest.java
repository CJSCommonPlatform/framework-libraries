package uk.gov.justice.services.test.utils.core.http;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class ExpectedJsonValuesResultConditionTest {

    @InjectMocks
    private ExpectedJsonValuesResultCondition expectedJsonValuesResultCondition;

    @Test
    public void shouldReturnTrueIfAllValuesAreFoundAtTheRootOfTheJson() throws Exception {

        final String json = "{ \"name_1\": \"value_1\", \"name_2\": \"value_2\"}";

        final Map<String, String> values = new HashMap<>();
        values.put("name_1", "value_1");
        values.put("name_2", "value_2");

        final ExpectedJsonValuesResultCondition expectedJsonValuesResultCondition =
                new ExpectedJsonValuesResultCondition(values);

        assertThat(expectedJsonValuesResultCondition.test(json), is(true));
    }

    @Test
    public void shouldReturnTrueIfSomeValuesAreFoundAtTheRootOfTheJson() throws Exception {

        final String json = "{ \"name_1\": \"value_1\", \"name_2\": \"value_2\"}";

        final Map<String, String> values = new HashMap<>();
        values.put("name_1", "value_1");

        final ExpectedJsonValuesResultCondition expectedJsonValuesResultCondition =
                new ExpectedJsonValuesResultCondition(values);

        assertThat(expectedJsonValuesResultCondition.test(json), is(true));
    }

    @Test
    public void shouldReturnFalseIfNoneOfTheValuesAreFoundInTheJson() throws Exception {

        final String json = "{ \"name_1\": \"value_1\", \"name_2\": \"value_2\"}";

        final Map<String, String> values = new HashMap<>();
        values.put("name_1", "value_3");
        values.put("name_2", "value_4");

        final ExpectedJsonValuesResultCondition expectedJsonValuesResultCondition =
                new ExpectedJsonValuesResultCondition(values);

        assertThat(expectedJsonValuesResultCondition.test(json), is(false));
    }

    @Test
    public void shouldReturnFalseIfSomeOfTheValuesAreNotFoundInTheJson() throws Exception {

        final String json = "{ \"name_1\": \"value_1\", \"name_2\": \"value_2\"}";

        final Map<String, String> values = new HashMap<>();
        values.put("name_1", "value_1");
        values.put("name_2", "value_3");

        final ExpectedJsonValuesResultCondition expectedJsonValuesResultCondition =
                new ExpectedJsonValuesResultCondition(values);

        assertThat(expectedJsonValuesResultCondition.test(json), is(false));
    }

    @Test
    public void shouldReturnFalseIfSomeOfTheNamesAreNotFoundInTheJson() throws Exception {

        final String json = "{ \"name_1\": \"value_1\", \"name_2\": \"value_2\"}";

        final Map<String, String> values = new HashMap<>();
        values.put("name_1", "value_1");
        values.put("name_3", "value_2");

        final ExpectedJsonValuesResultCondition expectedJsonValuesResultCondition =
                new ExpectedJsonValuesResultCondition(values);

        assertThat(expectedJsonValuesResultCondition.test(json), is(false));
    }
}
