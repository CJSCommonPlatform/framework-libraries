package uk.gov.justice.generation.pojo.plugin.factory;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.PluginProviderException;
import uk.gov.justice.generation.pojo.plugin.classmodifying.ClassModifyingPlugin;

import java.lang.reflect.Method;

import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class PluginInstantiatorTest {

    @Mock
    private Instantiator instantiator;

    @InjectMocks
    private PluginInstantiator pluginInstantiator;

    @Test
    public void shouldInstantiateAPluginUsingItsFactoryMethod() throws Exception {

        final PluginWithFactoryMethod pluginWithFactoryMethod = PluginWithFactoryMethod.createNew();
        final Class pluginWithFactoryMethodClass = pluginWithFactoryMethod.getClass();
        final Method factoryMethod = pluginWithFactoryMethodClass.getDeclaredMethod("createNew");

        when(instantiator.classForName(pluginWithFactoryMethodClass.getName())).thenReturn(pluginWithFactoryMethodClass);
        when(instantiator.invokeStaticMethod(factoryMethod, pluginWithFactoryMethodClass)).thenReturn(pluginWithFactoryMethod);

        assertThat(pluginInstantiator.instantiate(pluginWithFactoryMethodClass.getName()), is(pluginWithFactoryMethod));
    }

    @Test
    public void shouldInstantiateAClassWithoutAFactoryMethodUsingItsDefaultConstructor() throws Exception {

        final PluginWithoutFactoryMethod pluginWithoutFactoryMethod = new PluginWithoutFactoryMethod();
        final Class pluginWithoutFactoryMethodClass = pluginWithoutFactoryMethod.getClass();
        when(instantiator.classForName(PluginWithoutFactoryMethod.class.getName())).thenReturn(pluginWithoutFactoryMethodClass);
        when(instantiator.instantiate(pluginWithoutFactoryMethodClass)).thenReturn(pluginWithoutFactoryMethod);

        final Object plugin = pluginInstantiator.instantiate(PluginWithoutFactoryMethod.class.getName());

        assertThat(plugin, is(instanceOf(PluginWithoutFactoryMethod.class)));
    }

    @Test
    public void shouldFailInstantiationIfThePluginDoesNotImplementThePluginInterface() throws Exception {

        final Class notAPluginClass = String.class;

        when(instantiator.classForName(notAPluginClass.getName())).thenReturn(notAPluginClass);

        try {
            pluginInstantiator.instantiate(notAPluginClass.getName());
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Cannot instantiate plugin 'java.lang.String'. Class does not implement 'uk.gov.justice.generation.pojo.plugin.Plugin'"));
        }
    }

    @Test
    public void shouldFailIfThePluginHasMoreThanOneFactoryMethod() throws Exception {

        final Class pluginWithTwoFactoryMethodsClass = PluginWithTwoFactoryMethods.class;
        when(instantiator.classForName(pluginWithTwoFactoryMethodsClass.getName())).thenReturn(pluginWithTwoFactoryMethodsClass);

        try {
            pluginInstantiator.instantiate(pluginWithTwoFactoryMethodsClass.getName());
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Plugin 'PluginWithTwoFactoryMethods' has more than one factory method annotated with '@FactoryMethod'"));
        }
    }

    @Test
    public void shouldFailIfThePluginFactoryMethodIsNotStatic() throws Exception {

        final Class pluginWithNonStaticFactoryMethodClass = PluginWithNonStaticFactoryMethod.class;
        when(instantiator.classForName(pluginWithNonStaticFactoryMethodClass.getName())).thenReturn(pluginWithNonStaticFactoryMethodClass);

        try {
            pluginInstantiator.instantiate(pluginWithNonStaticFactoryMethodClass.getName());
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Failed to instantiate Plugin 'PluginWithNonStaticFactoryMethod': Factory method 'createUsingNonStaticMethod' is not static"));
        }
    }

    @Test
    public void shouldFailIfThePluginFactoryMethodIsNotPublic() throws Exception {

        final Class pluginWithNonPublicFactoryMethodClass = PluginWithNonPublicFactoryMethod.class;
        when(instantiator.classForName(pluginWithNonPublicFactoryMethodClass.getName())).thenReturn(pluginWithNonPublicFactoryMethodClass);

        try {
            pluginInstantiator.instantiate(pluginWithNonPublicFactoryMethodClass.getName());
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Failed to instantiate Plugin 'PluginWithNonPublicFactoryMethod': Factory method 'createUsingNonPublicMethod' is not public"));
        }
    }

    @Test
    public void shouldFailIfThePluginFactoryMethodHasParameters() throws Exception {

        final Class pluginWithFactoryMethodWithParametersClass = PluginWithFactoryMethodWithParameters.class;
        when(instantiator.classForName(pluginWithFactoryMethodWithParametersClass.getName())).thenReturn(pluginWithFactoryMethodWithParametersClass);
        try {
            pluginInstantiator.instantiate(pluginWithFactoryMethodWithParametersClass.getName());
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Failed to instantiate Plugin 'PluginWithFactoryMethodWithParameters': Factory method 'createUsingMethodWithParameters' should have no parameters"));
        }
    }
}

class PluginWithFactoryMethod implements ClassModifyingPlugin {

    private PluginWithFactoryMethod() {
    }

    @FactoryMethod
    @SuppressWarnings("unused")
    public static PluginWithFactoryMethod createNew() {
        return new PluginWithFactoryMethod();
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {
        return null;
    }
}

class PluginWithoutFactoryMethod implements ClassModifyingPlugin {

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {
        return null;
    }
}

class PluginWithTwoFactoryMethods implements ClassModifyingPlugin {

    @FactoryMethod
    @SuppressWarnings("unused")
    public static PluginWithTwoFactoryMethods factoryMethodOne() {
        return new PluginWithTwoFactoryMethods();
    }

    @FactoryMethod
    @SuppressWarnings("unused")
    public static PluginWithTwoFactoryMethods factoryMethodTwo() {
        return new PluginWithTwoFactoryMethods();
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {
        return null;
    }
}

class PluginWithNonStaticFactoryMethod implements ClassModifyingPlugin {

    private PluginWithNonStaticFactoryMethod() {
    }

    @FactoryMethod
    @SuppressWarnings("unused")
    public PluginWithNonStaticFactoryMethod createUsingNonStaticMethod() {
        return new PluginWithNonStaticFactoryMethod();
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {
        return null;
    }
}

class PluginWithNonPublicFactoryMethod implements ClassModifyingPlugin {

    private PluginWithNonPublicFactoryMethod() {
    }

    @FactoryMethod
    @SuppressWarnings("unused")
    static PluginWithNonPublicFactoryMethod createUsingNonPublicMethod() {
        return new PluginWithNonPublicFactoryMethod();
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {
        return null;
    }
}

class PluginWithFactoryMethodWithParameters implements ClassModifyingPlugin {

    @FactoryMethod
    @SuppressWarnings("unused")
    public static PluginWithFactoryMethodWithParameters createUsingMethodWithParameters(final int someValue) {
        return new PluginWithFactoryMethodWithParameters();
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder classBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {
        return null;
    }
}
