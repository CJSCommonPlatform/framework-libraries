package uk.gov.justice.generation.pojo.plugin.factory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import uk.gov.justice.generation.pojo.plugin.PluginProviderException;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class InstantiatorTest {

    @InjectMocks
    private Instantiator instantiator;

    @Test
    public void shouldCreateANewInstanceOfAClass() throws Exception {
        final Object myClass = instantiator.instantiate(MyClass.class);

        assertThat(myClass, is(instanceOf(MyClass.class)));
    }

    @Test
    public void shouldFailIfTheClassCannotBeInstantiated() throws Exception {
        try {
            instantiator.instantiate(MyAbstractClass.class);
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Unable to create new instance of class uk.gov.justice.generation.pojo.plugin.factory.MyAbstractClass"));
            assertThat(expected.getCause(), is(instanceOf(InstantiationException.class)));
        }
    }

    @Test
    public void shouldFailIfTheClassConstructorCannotBeAccessed() throws Exception {
        try {
            instantiator.instantiate(MyPrivateConstructorClass.class);
            fail();
        } catch (final PluginProviderException expected) {
            assertThat(expected.getMessage(), is("Unable to create new instance of class uk.gov.justice.generation.pojo.plugin.factory.MyPrivateConstructorClass"));
            assertThat(expected.getCause(), is(instanceOf(IllegalAccessException.class)));
        }
    }

    @Test
    public void shouldInvokeAStaticMethod() throws Exception {

        final Method method = MyClassWithStaticMethod.class.getDeclaredMethod("getFred");

        assertThat(instantiator.invokeStaticMethod(method, MyClassWithStaticMethod.class), is("Fred"));
    }

    @Test
    public void shouldFailWhenInvokingAPrivateStaticMethod() throws Exception {

        final Method method = MyClassWithPrivateStaticMethod.class.getDeclaredMethod("getFred");

        try {
            instantiator.invokeStaticMethod(method, MyClassWithPrivateStaticMethod.class);
        } catch (final PluginProviderException expected) {
            assertThat(expected.getCause(), is(instanceOf(IllegalAccessException.class)));
            assertThat(expected.getMessage(), is("Unable to invoke method getFred on class uk.gov.justice.generation.pojo.plugin.factory.MyClassWithPrivateStaticMethod"));
        }
    }

    @Test
    public void shouldCreateAClassByName() throws Exception {

        final String className = "uk.gov.justice.generation.pojo.plugin.factory.MyClass";
        final Class<?> aClass = instantiator.classForName(className);

        assertThat(aClass, is(equalTo(MyClass.class)));
    }

    @Test
    public void shouldAlwaysTrimAClassName() throws Exception {

        final String className = "\n\n           uk.gov.justice.generation.pojo.plugin.factory.MyClass           \n\n";
        final Class<?> aClass = instantiator.classForName(className);

        assertThat(aClass, is(equalTo(MyClass.class)));
    }

    @Test
    public void shouldFailIfTheClassIsNotFound() throws Exception {
        final String className = "uk.gov.justice.does.not.exist.MyMissingClass";
        try {
            instantiator.classForName(className);
            fail();
        } catch (final PluginProviderException expected) {
           assertThat(expected.getMessage(), is("Unable to create class uk.gov.justice.does.not.exist.MyMissingClass"));
           assertThat(expected.getCause(), is(instanceOf(ClassNotFoundException.class)));
        }
    }
}

class MyClass {
    
}

abstract class MyAbstractClass {

}

class MyPrivateConstructorClass {
    private MyPrivateConstructorClass() {
    }
}

class MyClassWithStaticMethod {
    public static String getFred() {
        return "Fred";
    }
}

class MyClassWithPrivateStaticMethod {
    private static String getFred() {
        return "Fred";
    }
}

class MyClassWithFailingStaticMethod {
    public static String getFred() throws IOException {
        throw new IOException("Ooops");
    }
}
