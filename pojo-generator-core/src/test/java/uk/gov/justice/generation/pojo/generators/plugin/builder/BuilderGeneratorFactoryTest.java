package uk.gov.justice.generation.pojo.generators.plugin.builder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuilderGeneratorFactoryTest {

    @InjectMocks
    private BuilderGeneratorFactory builderGeneratorFactory;

    @Test
    public void shouldCreateANewBuilderGeneratorWithTheCorrectDependencies() throws Exception {

        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final GenerationContext generationContext = mock(GenerationContext.class);

        final BuilderGenerator builderGenerator = builderGeneratorFactory.create(
                classDefinition,
                classNameFactory,
                generationContext
        );

        assertThat(getPrivateFieldFrom(builderGenerator, "classDefinition"), is(classDefinition));
        assertThat(getPrivateFieldFrom(builderGenerator, "classNameFactory"), is(classNameFactory));
        assertThat(getPrivateFieldFrom(builderGenerator, "generationContext"), is(generationContext));

        assertThat(getPrivateFieldFrom(builderGenerator, "builderFieldFactory"), is(instanceOf(BuilderFieldFactory.class)));
        assertThat(getPrivateFieldFrom(builderGenerator, "builderMethodFactory"), is(instanceOf(BuilderMethodFactory.class)));
    }

    private Object getPrivateFieldFrom(final Object containerObject, final String fieldName) throws NoSuchFieldException, IllegalAccessException {

        final Field declaredField = containerObject.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);

        return declaredField.get(containerObject);
    }
}
