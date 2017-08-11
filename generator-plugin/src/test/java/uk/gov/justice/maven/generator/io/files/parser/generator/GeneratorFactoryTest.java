package uk.gov.justice.maven.generator.io.files.parser.generator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.generator.generators.NonInstantiableTestGenerator;
import uk.gov.justice.maven.generator.io.files.parser.generator.generators.TestGenerator;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link GeneratorFactory} class.
 */
public class GeneratorFactoryTest {

    private GeneratorFactory factory;

    @Before
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfClassDoesNotExists() {
        factory.instanceOf("nonexistent.GeneratorClass");
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
