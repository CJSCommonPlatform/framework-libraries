package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.AnnotationSpec.builder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddEventAnnotationToClassPluginTest {

    @Mock
    private PluginContext pluginContext;

    @Test
    public void shouldAddEventAnnotationToTypeSpecIfRoot() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(ROOT, "address");

        when(pluginContext.getSourceFilename()).thenReturn("example.events.address.json");

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new AddEventAnnotationToClassPlugin()
                .generateWith(
                        typeSpecBuilder,
                        classDefinition,
                        pluginContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.name, is("ClassName"));
        assertThat(typeSpec.annotations.size(), is(1));
        assertThat(typeSpec.annotations, hasItem(builder(Event.class)
                .addMember("value", "$S", "example.events.address")
                .build()));
        assertThat(typeSpec.fieldSpecs.size(), is(0));
        assertThat(typeSpec.methodSpecs.size(), is(0));
    }

    @Test
    public void shouldNotAddEventAnnotationToNonRoot() throws Exception {
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");
        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        new AddEventAnnotationToClassPlugin()
                .generateWith(
                        typeSpecBuilder,
                        classDefinition,
                        pluginContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.name, is("ClassName"));
        assertThat(typeSpec.annotations.size(), is(0));
        assertThat(typeSpec.fieldSpecs.size(), is(0));
        assertThat(typeSpec.methodSpecs.size(), is(0));

        verifyZeroInteractions(pluginContext);
    }
}
