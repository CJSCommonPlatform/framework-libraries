package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.classmodifying.GenerateBuilderForClassPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.PluginContext;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenerateBuilderForClassPluginTest {

    @Mock
    private BuilderGeneratorFactory builderGeneratorFactory;

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
        when(classDefinition.getFieldName()).thenReturn(fieldName);
        when(builderGeneratorFactory.create(
                classDefinition,
                classNameFactory)).thenReturn(builderGenerator);

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
}
