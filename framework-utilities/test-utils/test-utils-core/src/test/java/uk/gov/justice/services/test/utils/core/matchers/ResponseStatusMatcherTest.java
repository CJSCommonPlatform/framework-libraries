package uk.gov.justice.services.test.utils.core.matchers;

import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.test.utils.core.matchers.ResponseStatusMatcher.status;

import uk.gov.justice.services.test.utils.core.http.ResponseData;

import org.junit.jupiter.api.Test;

public class ResponseStatusMatcherTest {

    @Test
    public void shouldMatchForStatusFromResponse() throws Exception {
        assertThat(new ResponseData(ACCEPTED, null,null), status().is(ACCEPTED));
    }

    @Test
    public void shouldFailWhenResponseStatusDoesNotMatch() {

        final AssertionError assertionError = assertThrows(AssertionError.class, () ->
                assertThat(new ResponseData(NOT_FOUND, null, null), status().is(ACCEPTED))
        );

        assertThat(assertionError.getMessage(), is("\nExpected: Status <Accepted>\n     but: got <Not Found>"));
    }
}