package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class GetterStatementGeneratorTest {

    @Mock
    private OptionalTypeNameUtil optionalTypeNameUtil;

    @InjectMocks
    private GetterStatementGenerator getterStatementGenerator;

    @Test
    public void shouldGenerateAGetterStatementThatGetsAFieldFromThePojo() throws Exception {

        final String fieldName = "firstName";
        final String parameterName = "alcubierreDrive";

        final boolean isOptionalType = false;
        final TypeName fieldTypeName = mock(TypeName.class);

        final Definition fieldDefinition = mock(Definition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(fieldDefinition.getFieldName()).thenReturn(fieldName);
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(fieldTypeName);
        when(optionalTypeNameUtil.isOptionalType(fieldTypeName)).thenReturn(isOptionalType);

        final String getterStatement = getterStatementGenerator.generateGetterStatement(
                classNameFactory,
                pluginContext,
                parameterName,
                fieldDefinition);

        assertThat(getterStatement, is("alcubierreDrive.getFirstName()"));
    }

    @Test
    public void shouldUnwrapIfFieldIsOptional() throws Exception {
        final String fieldName = "firstName";
        final String parameterName = "alcubierreDrive";

        final boolean isOptionalType = true;
        final TypeName fieldTypeName = mock(TypeName.class);

        final Definition fieldDefinition = mock(Definition.class);
        final ClassNameFactory classNameFactory = mock(ClassNameFactory.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(fieldDefinition.getFieldName()).thenReturn(fieldName);
        when(classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext)).thenReturn(fieldTypeName);
        when(optionalTypeNameUtil.isOptionalType(fieldTypeName)).thenReturn(isOptionalType);

        final String getterStatement = getterStatementGenerator.generateGetterStatement(
                classNameFactory,
                pluginContext,
                parameterName,
                fieldDefinition);

        assertThat(getterStatement, is("alcubierreDrive.getFirstName().orElse(null)"));
    }
}