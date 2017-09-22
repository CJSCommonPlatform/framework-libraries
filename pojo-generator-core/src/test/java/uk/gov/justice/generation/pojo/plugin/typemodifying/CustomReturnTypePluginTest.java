package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;

import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;

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
    private ReferenceToClassNameConverter referenceToClassNameConverter;

    @InjectMocks
    private CustomReturnTypePlugin customReturnTypePlugin;

    @Test
    public void shouldUseTheClassNameInTheJsonSchemaReferenceValueAsTheTypeName() throws Exception {

        final String referenceValue = "#/definitions/java.time.ZonedDateTime";

        final ClassName originalTypeName = ClassName.get(String.class);
        final ClassName modifiedTypeName = ClassName.get(ZonedDateTime.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        when(referenceDefinition.type()).thenReturn(REFERENCE);
        when(referenceDefinition.getReferenceValue()).thenReturn(referenceValue);
        when(referenceToClassNameConverter.get(referenceValue)).thenReturn(modifiedTypeName);

        final TypeName typeName = customReturnTypePlugin.modifyTypeName(originalTypeName, referenceDefinition);

        assertThat(typeName, is(modifiedTypeName));
    }

    @Test
    public void shouldReturnTheOriginalTypeNameIfTheDefinitinIsNotAReferenceDefinition() throws Exception {

        final ClassName originalTypeName = ClassName.get(String.class);

        final ReferenceDefinition referenceDefinition = mock(ReferenceDefinition.class);
        when(referenceDefinition.type()).thenReturn(STRING);

        final TypeName typeName = customReturnTypePlugin.modifyTypeName(originalTypeName, referenceDefinition);

        assertThat(typeName, is(originalTypeName));
    }

    @Test
    public void shouldHaveAFactoryMethodForInstantiation() throws Exception {

        final List<Method> methods = asList(CustomReturnTypePlugin.class.getDeclaredMethods());

        final Method factoryMethod = getFactoryMethod(methods);
        final Object plugin = factoryMethod.invoke(null);

        assertThat(plugin, is(instanceOf(CustomReturnTypePlugin.class)));

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
}
