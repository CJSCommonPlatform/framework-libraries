package uk.gov.justice.generation.pojo.generators.plugin.classgenerator;

import static com.squareup.javapoet.AnnotationSpec.builder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventAnnotationPluginTest {

    @Mock
    private JavaGeneratorFactory generatorFactory;

    @Mock
    private ClassNameFactory classNameFactory;

    @Mock
    private GenerationContext generationContext;

    @Test
    public void shouldAddEventAnnotationToTypeSpec() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(ROOT, "address");

        when(generationContext.getSourceFilename()).thenReturn("example.events.address.json");

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new EventAnnotationPlugin()
                .generateWith(
                        typeSpecBuilder,
                        classDefinition,
                        generatorFactory,
                        classNameFactory,
                        generationContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.annotations.size(), is(1));
        assertThat(typeSpec.annotations, hasItem(builder(Event.class)
                .addMember("value", "$S", "example.events.address")
                .build()));
        assertThat(typeSpec.fieldSpecs.size(), is(0));
        assertThat(typeSpec.methodSpecs.size(), is(0));

        verifyZeroInteractions(generatorFactory);
    }
}
