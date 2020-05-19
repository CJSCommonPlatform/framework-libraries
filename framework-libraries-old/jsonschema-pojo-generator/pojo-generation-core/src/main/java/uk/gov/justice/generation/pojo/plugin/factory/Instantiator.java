package uk.gov.justice.generation.pojo.plugin.factory;

import static java.lang.String.format;

import uk.gov.justice.generation.pojo.plugin.PluginProviderException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Instantiator {

    public Object instantiate(final Class<?> aClass)  {
        try {
            return aClass.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new PluginProviderException(format("Unable to create new instance of class %s", aClass.getName()), e);
        }
    }

    public Object invokeStaticMethod(final Method method, final Class<?> owner) {
        try {
            return method.invoke(null);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new PluginProviderException(format("Unable to invoke method %s on class %s", method.getName(), owner.getName()), e);
        }
    }

    public Class<?> classForName(final String className) {
        try {
            final String trim = className.trim();
            return Class.forName(trim);
        } catch (final ClassNotFoundException e) {
            throw new PluginProviderException(format("Unable to create class %s", className), e);
        }
    }
}
