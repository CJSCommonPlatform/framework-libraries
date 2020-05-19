package uk.gov.justice.domain.aggregate.matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.match;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the {@link EventSwitcher} class.
 */
public class EventSwitcherTest {

    private EventSwitcher eventSwitcher;

    private TestClass event;

    @Before
    public void setup() {
        event = new TestClass();
        eventSwitcher = match(event);
    }

    @Test
    public void shouldBuildClassRuleForCorrectClass() {
        ClassRule<TestClass> rule = EventSwitcher.when(TestClass.class);

        assertThat(rule.matches(event), equalTo(true));
        assertThat(rule.matches("test"), equalTo(false));
    }

    @Test
    public void shouldMatchAgainstMatchingRule() {
        EventMatcher<?> eventMatcher = mock(EventMatcher.class);
        when(eventMatcher.matches(event)).thenReturn(true);
        when(eventMatcher.apply(event)).thenReturn(event);

        assertThat(eventSwitcher.with(eventMatcher), equalTo(event));
    }

    @Test
    public void shouldThrowExceptionAndNotApplyIfNoMatchingRule() {
        EventMatcher<?> eventMatcher = mock(EventMatcher.class);
        when(eventMatcher.matches(event)).thenReturn(false);

        try {
            eventSwitcher.with(eventMatcher);
            fail();
        } catch (Exception ex) {
            verify(eventMatcher, times(0)).apply(any());
            assertThat(ex, instanceOf(RuntimeException.class));
        }
    }

    @Test
    public void shouldMatchAgainstFirstMatchingRuleOnly() {
        EventMatcher<?> eventMatcherA = mock(EventMatcher.class);
        when(eventMatcherA.matches(event)).thenReturn(true);
        when(eventMatcherA.apply(event)).thenReturn(event);
        EventMatcher<?> eventMatcherB = mock(EventMatcher.class);
        when(eventMatcherB.matches(event)).thenReturn(true);
        when(eventMatcherB.apply(event)).thenReturn(event);

        Object result = eventSwitcher.with(eventMatcherA, eventMatcherB);
        assertThat(result, equalTo(event));
        verify(eventMatcherA).apply(event);
        Mockito.verifyZeroInteractions(eventMatcherB);
    }

    @Test
    public void shouldMatchAgainstSecondRuleIfFirstDoesNotMatch() {
        EventMatcher<?> eventMatcherA = mock(EventMatcher.class);
        when(eventMatcherA.matches(event)).thenReturn(false);
        when(eventMatcherA.apply(event)).thenReturn(event);
        EventMatcher<?> eventMatcherB = mock(EventMatcher.class);
        when(eventMatcherB.matches(event)).thenReturn(true);
        when(eventMatcherB.apply(event)).thenReturn(event);

        Object result = eventSwitcher.with(eventMatcherA, eventMatcherB);
        assertThat(result, equalTo(event));
        verify(eventMatcherA, times(0)).apply(any());
        verify(eventMatcherB).apply(event);
    }

    @Test
    public void shouldCreateAnOtherwiseDoNothingMatchAllRule() {
        EventMatcher<?> eventMatcher = EventSwitcher.otherwiseDoNothing();

        assertThat(eventMatcher.matches(event), equalTo(true));
        assertThat(eventMatcher.matches("test"), equalTo(true));
    }

    @Test
    public void shouldCreateAnOtherwiseMatchAllRule() throws Exception {
        assertThat(EventSwitcher.otherwise().matches("test"), equalTo(true));
    }

    @Test
    public void shouldApplyDoNothingConsumer() throws Exception {
        try {
            EventSwitcher.doNothing().accept("Test");
        } catch (Exception ex) {
            throw new AssertionError("Exception thrown and not expected.", ex);
        }
    }

    private static class TestClass {
        boolean isConsumed = false;
    }
}
