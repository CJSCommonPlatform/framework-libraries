package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.FullyQualifiedNameToClassNameConverter;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomReturnTypePluginTest {

    @Mock
    private CustomReturnTypeMapper customReturnTypeMapper;

    @InjectMocks
    private CustomReturnTypePlugin customReturnTypePlugin;

    @Test
    public void shouldUseTheClassNameInTheJsonSchemaReferenceValueAsTheTypeName() throws Exception {

        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", "date-time");

        final ClassName originalTypeName = ClassName.get(String.class);
        final ClassName modifiedTypeName = ClassName.get(ZonedDateTime.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn(referenceValue);
        when(customReturnTypeMapper.customType(referenceValue, pluginContext)).thenReturn(of(modifiedTypeName));

        final TypeName typeName = customReturnTypePlugin.modifyTypeName(
                originalTypeName,
                referenceDefinition,
                pluginContext);

        assertThat(typeName, is(modifiedTypeName));
    }

    @Test
    public void shouldReturnTheOriginalTypeNameIfTheDefinitionIsNotAReferenceDefinition() throws Exception {

        final ClassName originalTypeName = ClassName.get(String.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        when(referenceDefinition.type()).thenReturn(STRING);

        final TypeName typeName = customReturnTypePlugin.modifyTypeName(
                originalTypeName,
                referenceDefinition,
                pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @Test
    public void shouldReturnTheOriginalTypeNameIfTheCustomReturnTypeMapperReturnsEmpty() throws Exception {

        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", "date-time");

        final ClassName originalTypeName = ClassName.get(String.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn(referenceValue);
        when(customReturnTypeMapper.customType(referenceValue, pluginContext)).thenReturn(empty());

        final TypeName typeName = customReturnTypePlugin.modifyTypeName(
                originalTypeName,
                referenceDefinition,
                pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldHaveAFactoryMethodForInstantiation() throws Exception {

        final List<Method> methods = asList(CustomReturnTypePlugin.class.getDeclaredMethods());

        final Method factoryMethod = getFactoryMethod(methods);
        final Object plugin = factoryMethod.invoke(null);

        assertThat(plugin, is(instanceOf(CustomReturnTypePlugin.class)));

        final CustomReturnTypeMapper customReturnTypeMapper = getFieldFrom(
                plugin,
                "customReturnTypeMapper",
                CustomReturnTypeMapper.class);

        assertThat(customReturnTypeMapper, is(notNullValue()));

        final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter = getFieldFrom(
                customReturnTypeMapper,
                "fullyQualifiedNameToClassNameConverter",
                FullyQualifiedNameToClassNameConverter.class);

        assertThat(fullyQualifiedNameToClassNameConverter, is(notNullValue()));

    }

    private Method getFactoryMethod(final List<Method> methods) {
        final Optional<Method> factoryMethod = methods.stream()
                .filter(method -> method.isAnnotationPresent(FactoryMethod.class))
                .findFirst();

        if (factoryMethod.isPresent()) {
            return factoryMethod.get();
        }

        fail();

        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldFrom(final Object object, final String fieldName, final Class<T> clazz) throws Exception {

        final Field declaredField = object.getClass().getDeclaredField(fieldName);

        assertThat(declaredField, is(notNullValue()));

        declaredField.setAccessible(true);

        return (T) declaredField.get(object);
    }
}
