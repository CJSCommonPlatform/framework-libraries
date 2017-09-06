package uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuilderPluginTest {

    @Mock
    private BuilderGeneratorFactory builderGeneratorFactory;

    @InjectMocks
    private BuilderPlugin builderPlugin;

    @Test
    public void shouldGenerateTheBuilderAsAnInnerClassAndAddToTheMainClassTypeSpec() throws Exception {

        final String fieldName = "alcubierreDrive";
        final String packageName = "org.bloggs.fred";

        final TypeSpec innerClassBuilder = TypeSpec.classBuilder("Builder").build();
        final MethodSpec staticGetBuilderMethod = MethodSpec.methodBuilder("alcubierreDrive").build();

        final TypeSpec.Builder outerClassBuilder = TypeSpec.classBuilder("MyClass");
        final ClassDefinition classDefinition = mock(ClassDefinition.class);
        final JavaGeneratorFactory javaGeneratorFactory = mock(JavaGeneratorFactory.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final GenerationContext generationContext = mock(GenerationContext.class);
        final BuilderGenerator builderGenerator = mock(BuilderGenerator.class);

        when(classDefinition.getFieldName()).thenReturn(fieldName);
        when(generationContext.getPackageName()).thenReturn(packageName);
        when(builderGeneratorFactory.create(
                classDefinition,
                classNameFactory,
                generationContext)).thenReturn(builderGenerator);

        when(builderGenerator.generate()).thenReturn(innerClassBuilder);
        when(builderGenerator.generateStaticGetBuilderMethod()).thenReturn(staticGetBuilderMethod);

        final TypeSpec.Builder builder = builderPlugin.generateWith(
                outerClassBuilder,
                classDefinition,
                javaGeneratorFactory,
                classNameFactory,
                generationContext
        );

        assertThat(builder, is(outerClassBuilder));

        final TypeSpec typeSpec = builder.build();

        assertThat(typeSpec.typeSpecs.size(), is(1));
        assertThat(typeSpec.typeSpecs, hasItem(innerClassBuilder));

        assertThat(typeSpec.methodSpecs.size(), is(1));
        assertThat(typeSpec.methodSpecs, hasItem(staticGetBuilderMethod));
    }
}
