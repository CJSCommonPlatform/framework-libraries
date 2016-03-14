package uk.gov.justice.raml.jaxrs.maven;

import org.junit.Before;
import org.junit.Test;
import org.raml.model.Raml;
import uk.gov.justice.raml.core.Generator;
import uk.gov.justice.raml.core.GeneratorConfig;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        Generator generator = factory.create("uk.gov.justice.raml.jaxrs.maven.GeneratorFactoryTest$TestGenerator");
        assertThat(generator, is(instanceOf(TestGenerator.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfClassDoesNotExists() {
        factory.create("nonexistent.GeneratorClass");
    }

    public static class TestGenerator implements Generator {

        @Override
        public void run(Raml raml, GeneratorConfig generatorConfig) {

        }
    }
}
