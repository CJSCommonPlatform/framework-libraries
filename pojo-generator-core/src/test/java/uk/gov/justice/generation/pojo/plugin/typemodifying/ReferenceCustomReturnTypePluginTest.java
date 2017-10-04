package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;
import static uk.gov.justice.generation.utils.ReflectionUtil.annotatedMethod;
import static uk.gov.justice.generation.utils.ReflectionUtil.fieldValue;

import uk.gov.justice.generation.pojo.core.TypeMapping;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.FullyQualifiedNameToClassNameConverter;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

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
public class ReferenceCustomReturnTypePluginTest {

    private static final Object IGNORED = null;

    @Mock
    private CustomReturnTypeMapper customReturnTypeMapper;

    @Mock
    private Predicate<TypeMapping> typeMappingPredicate;

    @InjectMocks
    private ReferenceCustomReturnTypePlugin referenceCustomReturnTypePlugin;

    @Test
    public void shouldUseTheClassNameInTheJsonSchemaReferenceValueAsTheTypeName() throws Exception {

        final String referenceValueName = "date-time";
        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", referenceValueName);

        final ClassName originalTypeName = ClassName.get(String.class);
        final ClassName modifiedTypeName = ClassName.get(ZonedDateTime.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn(referenceValue);
        when(customReturnTypeMapper.customTypeFor(typeMappingPredicate, referenceValueName, pluginContext)).thenReturn(of(modifiedTypeName));

        final TypeName typeName = referenceCustomReturnTypePlugin.modifyTypeName(
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

        final TypeName typeName = referenceCustomReturnTypePlugin.modifyTypeName(
                originalTypeName,
                referenceDefinition,
                pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @Test
    public void shouldReturnTheOriginalTypeNameIfTheCustomReturnTypeMapperReturnsEmpty() throws Exception {

        final String referenceValueName = "date-time";
        final ReferenceValue referenceValue = new ReferenceValue("#/definitions", referenceValueName);

        final ClassName originalTypeName = ClassName.get(String.class);
        final PluginContext pluginContext = mock(PluginContext.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn(referenceValue);
        when(customReturnTypeMapper.customTypeFor(typeMappingPredicate, referenceValueName, pluginContext)).thenReturn(empty());

        final TypeName typeName = referenceCustomReturnTypePlugin.modifyTypeName(
                originalTypeName,
                referenceDefinition,
                pluginContext);

        assertThat(typeName, is(originalTypeName));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldHaveAnnotatedFactoryMethodForInstantiation() throws Exception {

        final Optional<Method> factoryMethod = annotatedMethod(ReferenceCustomReturnTypePlugin.class, FactoryMethod.class);

        assertThat(factoryMethod.isPresent(), is(true));

        final Method method = factoryMethod.get();
        assertThat(isStatic(method.getModifiers()), is(true));

        final Object plugin = method.invoke(IGNORED);

        assertThat(plugin, is(instanceOf(ReferenceCustomReturnTypePlugin.class)));

        final CustomReturnTypeMapper customReturnTypeMapper = fieldValue(
                plugin,
                "customReturnTypeMapper",
                CustomReturnTypeMapper.class);

        assertThat(customReturnTypeMapper, is(notNullValue()));

        final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter = fieldValue(
                customReturnTypeMapper,
                "fullyQualifiedNameToClassNameConverter",
                FullyQualifiedNameToClassNameConverter.class);

        assertThat(fullyQualifiedNameToClassNameConverter, is(notNullValue()));
    }
}
