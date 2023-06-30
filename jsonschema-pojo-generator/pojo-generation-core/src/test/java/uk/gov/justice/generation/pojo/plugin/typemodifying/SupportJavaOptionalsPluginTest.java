package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.ARRAY;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.plugin.IncompatiblePluginException;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.AddHashcodeAndEqualsPlugin;
import uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class SupportJavaOptionalsPluginTest {

    @InjectMocks
    private SupportJavaOptionalsPlugin optionalTypeNamePlugin;

    @Test
    public void shouldWrapTypeWithJavaOptionalIfFieldIsNotRequired() throws Exception {

        final TypeName originalTypeName = ClassName.get(String.class);

        final Definition definition = mock(Definition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(definition.isRequired()).thenReturn(false);
        when(definition.type()).thenReturn(STRING);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(
                originalTypeName,
                definition,
                pluginContext);

        assertThat(typeName.toString(), is("java.util.Optional<java.lang.String>"));
    }

    @Test
    public void shouldNotWrapTypeWithJavaOptionalIfFieldIsRequired() throws Exception {

        final TypeName originalTypeName = ClassName.get(String.class);

        final Definition definition = mock(Definition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(definition.isRequired()).thenReturn(true);
        when(definition.type()).thenReturn(STRING);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(
                originalTypeName,
                definition,
                pluginContext);

        assertThat(typeName.toString(), is("java.lang.String"));
    }

    @Test
    public void shouldNotWrapTypeWithJavaOptionalIfClassIsAnArray() throws Exception {

        final TypeName originalTypeName = ClassName.get(List.class);

        final Definition definition = mock(Definition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(definition.type()).thenReturn(ARRAY);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(
                originalTypeName,
                definition,
                pluginContext);

        assertThat(typeName.toString(), is("java.util.List"));
    }

    @Test
    public void shouldNotWrapTypeWithJavaOptionalIfClassIsAnArrayEvenIfTheFiledIsRequired() throws Exception {

        final TypeName originalTypeName = ClassName.get(List.class);

        final Definition definition = mock(Definition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(definition.type()).thenReturn(ARRAY);

        final TypeName typeName = optionalTypeNamePlugin.modifyTypeName(
                originalTypeName,
                definition,
                pluginContext);

        assertThat(typeName.toString(), is("java.util.List"));
    }

    @Test
    public void shouldBeIncompatibleWithSerializablePlugin() throws Exception {

        final List<String> allPluginNames = new ArrayList<>();

        allPluginNames.add(AddHashcodeAndEqualsPlugin.class.getName());
        allPluginNames.add(ReferenceCustomReturnTypePlugin.class.getName());

        optionalTypeNamePlugin.checkCompatibilityWith(allPluginNames);

        allPluginNames.add(MakeClassSerializablePlugin.class.getName());

        try {
            optionalTypeNamePlugin.checkCompatibilityWith(allPluginNames);
            fail();
        } catch (final IncompatiblePluginException expected) {
            assertThat(expected.getMessage(), is(
                    "Plugin 'uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin' " +
                    "is incompatible with plugin " +
                    "'uk.gov.justice.generation.pojo.plugin.classmodifying.MakeClassSerializablePlugin' " +
                    "and should not be run together"));
        }
    }
}
