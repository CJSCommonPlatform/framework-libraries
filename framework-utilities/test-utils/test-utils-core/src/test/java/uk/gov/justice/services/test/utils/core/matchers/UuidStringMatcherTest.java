package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UuidStringMatcherTest {

    @Test
    public void shouldMatchAValidUuid() throws Exception {
        assertThat(UUID.randomUUID().toString(), UuidStringMatcher.isAUuid());
    }

    @Test
    public void shouldNotMatchANonValidUuid() throws Exception {

        try {
            assertThat("79d0c503-052f-4105-9b05-b49d9c4cf6a", UuidStringMatcher.isAUuid());
            fail();
        } catch (AssertionError expected) {}
    }
}