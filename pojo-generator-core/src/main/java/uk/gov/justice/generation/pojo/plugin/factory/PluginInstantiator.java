package uk.gov.justice.generation.pojo.plugin.factory;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginProviderException;

import java.lang.reflect.Method;
import java.util.List;

public class PluginInstantiator {

    private final Instantiator instantiator;

    public PluginInstantiator(final Instantiator instantiator) {
        this.instantiator = instantiator;
    }

    public Object instantiate(final String className) {

        final Class<?> aClass = instantiator.classForName(className);

        final List<Method> methods = stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(FactoryMethod.class))
                .collect(toList());

        if (methods.isEmpty()) {
            return instantiator.instantiate(aClass);
        }

        if (methods.size() > 1) {
            throw new PluginProviderException(format("Plugin '%s' has more than one factory method annotated with '@%s'", aClass.getSimpleName(), FactoryMethod.class.getSimpleName()));
        }

        final Method method = methods.get(0);

        if (notPublic(method)) {
            throw new PluginProviderException(format("Failed to instantiate Plugin '%s': Factory method '%s' is not public", aClass.getSimpleName(), method.getName()));
        }

        if (notStatic(method)) {
            throw new PluginProviderException(format("Failed to instantiate Plugin '%s': Factory method '%s' is not static", aClass.getSimpleName(), method.getName()));
        }

        if (hasParameters(method)) {
            throw new PluginProviderException(format("Failed to instantiate Plugin '%s': Factory method '%s' should have no parameters", aClass.getSimpleName(), method.getName()));
        }

        return instantiator.invokeStaticMethod(method, aClass);
    }

    private boolean notPublic(final Method method) {
        return (method.getModifiers() & PUBLIC) == 0;
    }

    private boolean notStatic(final Method method) {
        return (method.getModifiers() & STATIC) == 0;
    }

    private boolean hasParameters(final Method method) {
        return method.getParameterCount() != 0;
    }
}
