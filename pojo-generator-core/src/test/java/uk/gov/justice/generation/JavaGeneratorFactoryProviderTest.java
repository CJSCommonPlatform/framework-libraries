package uk.gov.justice.generation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.generation.utils.ReflectionUtil.fieldValue;

import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import org.junit.Test;

public class JavaGeneratorFactoryProviderTest {

    @Test
    public void shouldProvideJavaGeneratorFactory() throws Exception {
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        final JavaGeneratorFactory javaGeneratorFactory = new JavaGeneratorFactoryProvider().create(classNameFactory);

        assertThat(fieldValue(javaGeneratorFactory, "classNameFactory"), is(classNameFactory));
    }
}
