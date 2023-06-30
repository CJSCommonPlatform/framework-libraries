package uk.gov.justice.domain.aggregate.matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ClassRule} class.
 */
public class ClassRuleTest {

    private ClassRule<TestClass> classRule;

    @BeforeEach
    public void setup() {
        classRule = new ClassRule<>(TestClass.class);
    }

    @Test
    public void shouldMatchTestClass() {
        assertThat(classRule.matches(new TestClass()), equalTo(true));
    }

    @Test
    public void shouldMatchSubClass() {
        assertThat(classRule.matches(new SubTestClass()), equalTo(true));
    }

    @Test
    public void shouldNotMatchDifferentClass() {
        assertThat(classRule.matches("test"), equalTo(false));
    }

    @Test
    public void shouldBuildEventMatcherWithSuppliedConsumer() {
        EventMatcher<TestClass> eventMatcher = classRule.apply(x -> x.isConsumed = true);

        TestClass event = new TestClass();
        eventMatcher.apply(event);

        assertThat(event.isConsumed, equalTo(true));
    }

    private static class TestClass {
        boolean isConsumed = false;
    }

    private static class SubTestClass extends TestClass {
    }
}
