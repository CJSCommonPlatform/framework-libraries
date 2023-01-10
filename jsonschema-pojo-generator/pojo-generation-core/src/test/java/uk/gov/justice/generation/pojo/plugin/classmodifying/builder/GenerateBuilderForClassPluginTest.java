package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static java.util.Arrays.stream;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;

import java.lang.reflect.Method;
import java.util.Optional;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenerateBuilderForClassPluginTest {

    @Mock
    private BuilderGeneratorFactory builderGeneratorFactory;

    @Mock
    private WithMethodGenerator withMethodGenerator;

    @InjectMocks
    private GenerateBuilderForClassPlugin generateBuilderForClassPlugin;

    @Test
    public void shouldGenerateTheBuilderAsAnInnerClassAndAddToTheMainClassTypeSpec() throws Exception {

        final String fieldName = "alcubierreDrive";

        final TypeSpec innerClassBuilder = TypeSpec.classBuilder("Builder").build();
        final MethodSpec staticGetBuilderMethod = MethodSpec.methodBuilder("alcubierreDrive").build();

        final TypeSpec.Builder outerClassBuilder = TypeSpec.classBuilder("MyClass");
        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final BuilderGenerator builderGenerator = mock(BuilderGenerator.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(pluginContext.getClassNameFactory()).thenReturn(classNameFactory);
        when(builderGeneratorFactory.create(
                classDefinition,
                classNameFactory,
                pluginContext,
                withMethodGenerator)).thenReturn(builderGenerator);

        when(builderGenerator.generate()).thenReturn(innerClassBuilder);
        when(builderGenerator.generateStaticGetBuilderMethod()).thenReturn(staticGetBuilderMethod);

        final TypeSpec.Builder builder = generateBuilderForClassPlugin.generateWith(
                outerClassBuilder,
                classDefinition,
                pluginContext);

        assertThat(builder, is(outerClassBuilder));

        final TypeSpec typeSpec = builder.build();

        assertThat(typeSpec.typeSpecs.size(), is(1));
        assertThat(typeSpec.typeSpecs, hasItem(innerClassBuilder));

        assertThat(typeSpec.methodSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs, hasItem(staticGetBuilderMethod));
    }

    @Test
    public void shouldBeInstantiableUsingItsFactoryMethod() throws Exception {

        final Class<GenerateBuilderForClassPlugin> pluginClass = GenerateBuilderForClassPlugin.class;

        final Optional<Method> methodOptional = stream(pluginClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(FactoryMethod.class))
                .findFirst();

        if (methodOptional.isPresent()) {
            final Object plugin = methodOptional.get().invoke(null);
            assertThat(plugin, is(instanceOf(pluginClass)));
        } else {
            fail();
        }
    }
}
