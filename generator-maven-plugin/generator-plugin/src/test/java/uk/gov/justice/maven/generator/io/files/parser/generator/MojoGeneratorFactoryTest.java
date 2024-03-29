package uk.gov.justice.maven.generator.io.files.parser.generator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.generator.generators.NonInstantiableTestGenerator;
import uk.gov.justice.maven.generator.io.files.parser.generator.generators.TestGenerator;
import uk.gov.justice.maven.generator.io.files.parser.generator.generators.TestGeneratorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link MojoGeneratorFactory} class.
 */
public class MojoGeneratorFactoryTest {

    private MojoGeneratorFactory factory;

    @BeforeEach
    public void setup() {
        factory = new MojoGeneratorFactory();
    }

    @Test
    public void shouldCreateGenerator() {
        final Generator generator = factory.instanceOf(TestGenerator.class.getName());
        assertThat(generator, is(instanceOf(TestGenerator.class)));
    }

    @Test
    public void shouldCreateGeneratorFromFactory() {
        final Generator generator = factory.instanceOf(TestGeneratorFactory.class.getName());
        assertThat(generator, is(instanceOf(TestGenerator.class)));
    }

    @Test
    public void shouldCreateOnlyOneInstanceOfGenerator() {
        final Generator generator1 = factory.instanceOf(TestGenerator.class.getName());
        final Generator generator2 = factory.instanceOf(TestGenerator.class.getName());

        assertThat(generator1, sameInstance(generator2));
    }

    @Test
    public void shouldCreateOnlyOneInstanceOfGeneratorFromFactory() {
        final Generator generator1 = factory.instanceOf(TestGeneratorFactory.class.getName());
        final Generator generator2 = factory.instanceOf(TestGeneratorFactory.class.getName());

        assertThat(generator1, sameInstance(generator2));
    }

    @Test
    public void shouldThrowExceptionIfClassDoesNotExists() {
        assertThrows(IllegalArgumentException.class, () -> factory.instanceOf("nonexistent.GeneratorClass"));
    }

    @Test
    public void shouldThrowExceptionIfThegeneratorCannotBeInstantiated() throws Exception {

        try {
            factory.instanceOf(NonInstantiableTestGenerator.class.getName());
        } catch (final IllegalArgumentException expected) {
            assertThat(expected.getMessage(), is("Could not instantiate generator " + NonInstantiableTestGenerator.class.getName()));
            assertThat(expected.getCause(), is(instanceOf(IllegalAccessException.class)));
        }
    }
}
