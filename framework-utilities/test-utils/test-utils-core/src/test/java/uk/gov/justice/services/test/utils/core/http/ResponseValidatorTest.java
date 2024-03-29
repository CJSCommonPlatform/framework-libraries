package uk.gov.justice.services.test.utils.core.http;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.http.PollingRequestParamsBuilder.pollingRequestParams;

import java.util.function.Predicate;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResponseValidatorTest {

    @InjectMocks
    private ResponseValidator responseValidator;

    @Test
    public void shouldReturnTrueIfTheExpectedStatusMatchesAndTheConditionSucceeds() throws Exception {

        final Status status = OK;
        final String responseBody = "some json";
        final Predicate<String> successfulResponseBodyCondition = json -> true;

        final PollingRequestParams pollingRequestParams = pollingRequestParams("a url", "a media type")
                .withResponseBodyCondition(successfulResponseBodyCondition)
                .withExpectedResponseStatus(status)
                .build();


        assertThat(responseValidator.isValid(responseBody, status, pollingRequestParams), is(true));
    }

    @Test
    public void shouldReturnFalseIfTheStatusDoesNotMatch() throws Exception {

        final Status expectedStatus = OK;
        final Status actualStatus = NOT_FOUND;
        final String responseBody = "some json";
        final Predicate<String> successfulResponseBodyCondition = json -> true;

        final PollingRequestParams pollingRequestParams = pollingRequestParams("a url", "a media type")
                .withResponseBodyCondition(successfulResponseBodyCondition)
                .withExpectedResponseStatus(expectedStatus)
                .build();


        assertThat(responseValidator.isValid(responseBody, actualStatus, pollingRequestParams), is(false));
    }

    @Test
    public void shouldReturnFalseIfTheResponseBodyConditionFailsButTheStatusMatches() throws Exception {

        final Status status = OK;
        final String responseBody = "some json";
        final Predicate<String> successfulResponseBodyCondition = json -> false;

        final PollingRequestParams pollingRequestParams = pollingRequestParams("a url", "a media type")
                .withResponseBodyCondition(successfulResponseBodyCondition)
                .withExpectedResponseStatus(status)
                .build();


        assertThat(responseValidator.isValid(responseBody, status, pollingRequestParams), is(false));
    }

    @Test
    public void shouldReturnFalseIfTheExpectedStatusDoesNotMatchAndTheRespnseBodyConditionFails() throws Exception {

        final Status expectedStatus = OK;
        final Status actualStatus = NOT_FOUND;
        final String responseBody = "some json";
        final Predicate<String> successfulResponseBodyCondition = json -> false;

        final PollingRequestParams pollingRequestParams = pollingRequestParams("a url", "a media type")
                .withResponseBodyCondition(successfulResponseBodyCondition)
                .withExpectedResponseStatus(expectedStatus)
                .build();


        assertThat(responseValidator.isValid(responseBody, actualStatus, pollingRequestParams), is(false));
    }
}
