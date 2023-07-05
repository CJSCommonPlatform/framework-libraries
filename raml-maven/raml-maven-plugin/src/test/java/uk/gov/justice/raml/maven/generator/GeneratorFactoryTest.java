package uk.gov.justice.raml.maven.generator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.maven.generator.generators.NonInstantiableTestGenerator;
import uk.gov.justice.raml.maven.generator.generators.TestGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link GeneratorFactory} class.
 */
public class GeneratorFactoryTest {

    private GeneratorFactory factory;

    @BeforeEach
    public void setup() {
        factory = new GeneratorFactory();
    }

    @Test
    public void shouldCreateGenerator() {
        final Generator generator = factory.instanceOf(TestGenerator.class.getName());
        assertThat(generator, is(instanceOf(TestGenerator.class)));
    }

    @Test
    public void shouldCreateOnlyOneInstanceOfGenerator() {
        final Generator generator1 = factory.instanceOf(TestGenerator.class.getName());
        final Generator generator2 = factory.instanceOf(TestGenerator.class.getName());

        assertThat(generator1, sameInstance(generator2));
    }

    @Test
    public void shouldThrowExceptionIfClassDoesNotExists() {
        assertThrows(IllegalArgumentException.class, () -> factory.instanceOf("nonexistent.GeneratorClass"));
    }

    @Test
    public void shouldThrowAnIllegalArgumentExceptionIfThegeneratorCannotBeInstantiated() throws Exception {

        try {
            factory.instanceOf(NonInstantiableTestGenerator.class.getName());
        } catch (final IllegalArgumentException expected) {
            assertThat(expected.getMessage(), is("Could not instantiate generator " + NonInstantiableTestGenerator.class.getName()));
            assertThat(expected.getCause(), is(instanceOf(NoSuchMethodException.class)));
        }
    }
}
