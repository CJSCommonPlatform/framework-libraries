package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.TypeName.get;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.PRIVATE;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

import java.util.List;

import com.squareup.javapoet.FieldSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuilderFieldFactoryTest {

    @InjectMocks
    private BuilderFieldFactory builderFieldFactory;

    @Test
    public void shouldGenerateThePrivateFieldsForOurBuilder() throws Exception {

        final Definition fieldDefinition_1 = mock(Definition.class);
        final Definition fieldDefinition_2 = mock(Definition.class);
        final List<Definition> fieldDefinitions = asList(fieldDefinition_1, fieldDefinition_2);

        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);

        when(fieldDefinition_1.getFieldName()).thenReturn("fieldDefinition_1");
        when(fieldDefinition_2.getFieldName()).thenReturn("fieldDefinition_2");

        when(classNameFactory.createTypeNameFrom(fieldDefinition_1)).thenReturn(get(String.class));
        when(classNameFactory.createTypeNameFrom(fieldDefinition_2)).thenReturn(get(Integer.class));

        final List<FieldSpec> fields = builderFieldFactory.createFields(fieldDefinitions, classNameFactory);

        assertThat(fields.size(), is(2));

        assertThat(fields.get(0).name, is("fieldDefinition_1"));
        assertThat(fields.get(0).modifiers.size(), is(1));
        assertThat(fields.get(0).modifiers, hasItem(PRIVATE));
        assertThat(fields.get(0).type.toString(), is("java.lang.String"));

        assertThat(fields.get(1).name, is("fieldDefinition_2"));
        assertThat(fields.get(1).modifiers.size(), is(1));
        assertThat(fields.get(1).modifiers, hasItem(PRIVATE));
        assertThat(fields.get(1).type.toString(), is("java.lang.Integer"));
    }
}
