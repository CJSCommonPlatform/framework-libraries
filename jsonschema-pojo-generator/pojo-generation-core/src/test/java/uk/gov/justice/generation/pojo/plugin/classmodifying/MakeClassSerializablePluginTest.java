package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.CLASS;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.IncompatiblePluginException;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.ReferenceCustomReturnTypePlugin;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MakeClassSerializablePluginTest {

    @Mock
    private PluginContext pluginContext;

    @Test
    public void shouldAddSerializationToTypeSpec() throws Exception {

        final long serialVersionUID = 23L;
        final ClassDefinition classDefinition = new ClassDefinition(CLASS, "address");

        final TypeSpec.Builder typeSpecBuilder = classBuilder("ClassName");

        when(pluginContext.getSerialVersionUID()).thenReturn(serialVersionUID);

        new MakeClassSerializablePlugin().generateWith(typeSpecBuilder, classDefinition, pluginContext);

        final TypeSpec typeSpec = typeSpecBuilder.build();

        assertThat(typeSpec.superinterfaces.size(), is(1));
        assertThat(typeSpec.superinterfaces, hasItem(TypeName.get(Serializable.class)));
        assertThat(typeSpec.fieldSpecs, hasItem(FieldSpec
                .builder(TypeName.LONG, "serialVersionUID", PRIVATE, STATIC, FINAL)
                .initializer("23L").build()));

    }

    @Test
    public void shouldBeIncompatibleWithSerializablePlugin() throws Exception {

        final MakeClassSerializablePlugin makeClassSerializablePlugin = new MakeClassSerializablePlugin();

        final List<String> allPluginNames = new ArrayList<>();

        allPluginNames.add(AddHashcodeAndEqualsPlugin.class.getName());
        allPluginNames.add(ReferenceCustomReturnTypePlugin.class.getName());

        makeClassSerializablePlugin.checkCompatibilityWith(allPluginNames);

        allPluginNames.add(SupportJavaOptionalsPlugin.class.getName());

        try {
            makeClassSerializablePlugin.checkCompatibilityWith(allPluginNames);
            fail();
        } catch (final IncompatiblePluginException expected) {
            assertThat(expected.getMessage(), is(
                    "Plugin " +
                            "'uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin' " +
                            "is incompatible with plugin " +
                            "'uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin' " +
                            "and should not be run together"));
        }
    }
}
