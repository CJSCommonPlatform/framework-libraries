package uk.gov.justice.domain.aggregate.matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link EventMatcher} class.
 */
@ExtendWith(MockitoExtension.class)
public class EventMatcherTest {

    private EventMatcher<TestClass> eventMatcher;

    @Mock
    private Rule<TestClass> rule;

    @Mock
    private Consumer<TestClass> consumer;

    @Mock
    private TestClass event;

    @BeforeEach
    public void setup() {
        eventMatcher = new EventMatcher<>(rule, consumer);
    }

    @Test
    public void shouldReturnTrueIfRuleMatches() {
        when(rule.matches(event)).thenReturn(true);
        assertThat(eventMatcher.matches(event), equalTo(true));
    }

    @Test
    public void shouldReturnFalseIfRuleDoesNotMatch() {
        when(rule.matches(event)).thenReturn(false);
        assertThat(eventMatcher.matches(event), equalTo(false));
    }

    @Test
    public void shouldConsumeEvent() {
        eventMatcher = new EventMatcher<>(rule, x -> x.isConsumed = true);
        event = new TestClass();

        eventMatcher.apply(event);

        assertThat(event.isConsumed, equalTo(true));
    }

    @Test
    public void shouldReturnEventWhenConsumed() {
        eventMatcher = new EventMatcher<>(rule, x -> x.isConsumed = true);
        event = new TestClass();

        assertThat(eventMatcher.apply(event), equalTo(event));
    }

    private static class TestClass {
        boolean isConsumed = false;
    }
}
