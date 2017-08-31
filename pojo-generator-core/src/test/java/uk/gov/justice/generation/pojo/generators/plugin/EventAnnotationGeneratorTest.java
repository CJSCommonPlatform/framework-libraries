package uk.gov.justice.generation.pojo.generators.plugin;

import static com.squareup.javapoet.AnnotationSpec.builder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.ClassName;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventAnnotationGeneratorTest {

    @Test
    public void shouldAddEventAnnotationToTypeSpec() throws Exception {
        final JavaGeneratorFactory generatorFactory = mock(JavaGeneratorFactory.class);
        final ClassDefinition classDefinition = new ClassDefinition("address", mock(ClassName.class), "example.events.address");

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new EventAnnotationGenerator().generateWith(typeSpecBuilder, classDefinition, generatorFactory);

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
