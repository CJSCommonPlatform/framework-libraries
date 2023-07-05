package uk.gov.justice.domain.aggregate.condition;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.domain.aggregate.condition.Precondition.assertPrecondition;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Precondition} class.
 */
public class PreconditionTest {

    @Test
    public void shouldSucceedForRawAssertion() {
        assertPrecondition(true).orElseThrow("Test");
    }

    @Test
    public void shouldThrowExceptionForFailingRawAssertion() {
        assertThrows(RuntimeException.class, () -> assertPrecondition(false).orElseThrow("Test"));
    }

    @Test
    public void shouldSucceedForSupplierAssertion() {
        assertPrecondition(() -> true).orElseThrow("Test");
    }

    @Test
    public void shouldThrowExceptionForFailingSupplierAssertion() {
        assertThrows(RuntimeException.class, () -> assertPrecondition(() -> false).orElseThrow("Test"));
    }

    @Test
    public void shouldThrowSupplierExceptionForFailingRawAssertion() {
        assertThrows(IllegalStateException.class, () -> assertPrecondition(false).orElseThrow(() -> new IllegalStateException("What?!")));
    }
}
