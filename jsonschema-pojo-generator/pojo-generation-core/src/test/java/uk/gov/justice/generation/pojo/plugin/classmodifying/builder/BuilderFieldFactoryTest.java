package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuilderFieldFactoryTest {

    @Mock
    private FieldSpecFactory fieldSpecFactory;

    @InjectMocks
    private BuilderFieldFactory builderFieldFactory;

    @Test
    public void shouldGenerateThePrivateFieldsForOurBuilder() throws Exception {

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final Definition fieldDefinition_3 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2, fieldDefinition_3);

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final FieldSpec fieldSpec_1 = FieldSpec.builder(String.class, "field_1", Modifier.PUBLIC).build();
        final FieldSpec fieldSpec_2 = FieldSpec.builder(String.class, "field_2", Modifier.PUBLIC).build();
        final FieldSpec fieldSpec_3 = FieldSpec.builder(String.class, "field_3", Modifier.PUBLIC).build();

        when(fieldSpecFactory.createFieldSpecFor(fieldDefinition_1, classNameFactory, pluginContext)).thenReturn(fieldSpec_1);
        when(fieldSpecFactory.createFieldSpecFor(fieldDefinition_2, classNameFactory, pluginContext)).thenReturn(fieldSpec_2);
        when(fieldSpecFactory.createFieldSpecFor(fieldDefinition_3, classNameFactory, pluginContext)).thenReturn(fieldSpec_3);

        final List<FieldSpec> fields = builderFieldFactory.createFields(
                fieldDefinitions,
                classNameFactory,
                pluginContext);

        assertThat(fields.size(), is(3));

        assertThat(fields.get(0), is(fieldSpec_1));
        assertThat(fields.get(1), is(fieldSpec_2));
        assertThat(fields.get(2), is(fieldSpec_3));
    }
}
