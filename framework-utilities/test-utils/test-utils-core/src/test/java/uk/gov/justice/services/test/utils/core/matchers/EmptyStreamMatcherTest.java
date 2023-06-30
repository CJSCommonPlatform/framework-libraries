package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.justice.services.test.utils.core.matchers.EmptyStreamMatcher.isEmptyStream;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class EmptyStreamMatcherTest {

    @Test
    public void shouldMatchAnEmptyStream() throws Exception {
        assertThat(Stream.empty(), isEmptyStream());
    }

    @Test
    public void shouldNotMatchANonEmptyStream() throws Exception {
        try {
            assertThat(Stream.of("test1", "test2"), isEmptyStream());
            fail();
        } catch (final AssertionError expected) {
        }
    }

}