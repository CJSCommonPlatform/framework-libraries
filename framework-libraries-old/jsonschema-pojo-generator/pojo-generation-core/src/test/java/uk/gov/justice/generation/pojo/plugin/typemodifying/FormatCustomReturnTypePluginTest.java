package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static java.lang.reflect.Modifier.isStatic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.INTEGER;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.annotatedMethod;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.getValueOfField;

import uk.gov.justice.generation.pojo.core.TypeMapping;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.FullyQualifiedNameToClassNameConverter;

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Predicate;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormatCustomReturnTypePluginTest {

    private static final Object IGNORED = null;

    @Mock
    private CustomReturnTypeMapper customReturnTypeMapper;

    @Mock
    private Predicate<TypeMapping> typeMappingPredicate;

    @InjectMocks
    private FormatCustomReturnTypePlugin formatCustomReturnTypePlugin;

    @Test
    public void shouldUseTypeNameFromPropertyTypeMappingForFormatValue() throws Exception {
        final String formatValue = "date-time";
        final ClassName originalTypeName = ClassName.get(String.class);
        final ClassName modifiedTypeName = ClassName.get(ZonedDateTime.class);

        final PluginContext pluginContext = mock(PluginContext.class);
        final StringDefinition stringDefinition = mock(StringDefinition.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(stringDefinition.getFormat()).thenReturn(Optional.of(formatValue));
        when(customReturnTypeMapper.customTypeFor(typeMappingPredicate, formatValue, pluginContext)).thenReturn(Optional.of(modifiedTypeName));

        final TypeName typeName = formatCustomReturnTypePlugin.modifyTypeName(originalTypeName, stringDefinition, pluginContext);

        assertThat(typeName, is(modifiedTypeName));
    }

    @Test
    public void shouldUseOriginalTypeNameIfThereIsNoPropertyTypeMappingForFormatValue() throws Exception {
        final String formatValue = "date-time";
        final ClassName originalTypeName = ClassName.get(String.class);

        final PluginContext pluginContext = mock(PluginContext.class);
        final StringDefinition stringDefinition = mock(StringDefinition.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(stringDefinition.getFormat()).thenReturn(Optional.of(formatValue));
        when(customReturnTypeMapper.customTypeFor(typeMappingPredicate, formatValue, pluginContext)).thenReturn(Optional.empty());

        final TypeName typeName = formatCustomReturnTypePlugin.modifyTypeName(originalTypeName, stringDefinition, pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @Test
    public void shouldUseOriginalTypeNameIfThereIsNoFormatInSchema() throws Exception {
        final ClassName originalTypeName = ClassName.get(String.class);

        final PluginContext pluginContext = mock(PluginContext.class);
        final StringDefinition stringDefinition = mock(StringDefinition.class);

        when(stringDefinition.type()).thenReturn(STRING);
        when(stringDefinition.getFormat()).thenReturn(Optional.empty());

        final TypeName typeName = formatCustomReturnTypePlugin.modifyTypeName(originalTypeName, stringDefinition, pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @Test
    public void shouldReturnTheOriginalTypeNameIfTheDefinitionIsNotStringDefinition() throws Exception {
        final ClassName originalTypeName = ClassName.get(String.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final Definition definition = mock(Definition.class);

        when(definition.type()).thenReturn(INTEGER);

        final TypeName typeName = formatCustomReturnTypePlugin.modifyTypeName(originalTypeName, definition, pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @Test
    public void shouldHaveAnnotatedFactoryMethodForInstantiation() throws Exception {
        final Optional<Method> factoryMethod = annotatedMethod(FormatCustomReturnTypePlugin.class, FactoryMethod.class);

        assertThat(factoryMethod.isPresent(), is(true));

        final Method method = factoryMethod.get();
        assertThat(isStatic(method.getModifiers()), is(true));

        final Object plugin = method.invoke(IGNORED);

        assertThat(plugin, is(instanceOf(FormatCustomReturnTypePlugin.class)));

        final CustomReturnTypeMapper customReturnTypeMapper = getValueOfField(
                plugin,
                "customReturnTypeMapper",
                CustomReturnTypeMapper.class);

        final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter = getValueOfField(
                customReturnTypeMapper,
                "fullyQualifiedNameToClassNameConverter",
                FullyQualifiedNameToClassNameConverter.class);

        assertThat(fullyQualifiedNameToClassNameConverter, is(notNullValue()));
    }
}
