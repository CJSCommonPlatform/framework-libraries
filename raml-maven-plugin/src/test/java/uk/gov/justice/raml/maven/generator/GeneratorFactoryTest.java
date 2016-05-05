package uk.gov.justice.raml.maven.generator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;

import org.junit.Before;
import org.junit.Test;
import org.raml.model.Raml;

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
        Generator generator = factory.instanceOf("uk.gov.justice.raml.maven.generator.GeneratorFactoryTest$TestGenerator");
        assertThat(generator, is(instanceOf(TestGenerator.class)));
    }

    @Test
    public void shouldCreateOnlyOneInstanceOfGenerator() {
        Generator generator1 = factory.instanceOf("uk.gov.justice.raml.maven.generator.GeneratorFactoryTest$TestGenerator");
        Generator generator2 = factory.instanceOf("uk.gov.justice.raml.maven.generator.GeneratorFactoryTest$TestGenerator");

        assertThat(generator1, sameInstance(generator2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfClassDoesNotExists() {
        factory.instanceOf("nonexistent.GeneratorClass");
    }

    public static class TestGenerator implements Generator {

        @Override
        public void run(Raml raml, GeneratorConfig generatorConfig) {

        }
    }
}
