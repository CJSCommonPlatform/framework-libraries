package uk.gov.justice.generation.pojo.integration.utils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

public class ClassInstantiator {

    public Object newInstance(final Class<?> clazz, final Object... constructorParameters) throws Exception {

        final Class[] parameterClasses = asArrayOfClasses(constructorParameters);

        final Constructor<?> constructor = clazz.getConstructor(parameterClasses);

        return constructor.newInstance(asArrayOfValues(constructorParameters));
    }

    private Class[] asArrayOfClasses(final Object[] constructorParameters) {

        return of(constructorParameters)
                .map(this::getClass)
                .collect(toList())
                .toArray(new Class[constructorParameters.length]);
    }

    private Class<?> getClass(final Object o) {

        final Class<?> aClass;
        if (o instanceof NullParameter) {
            return ((NullParameter) o).getClazz();
        } else {
            aClass = o.getClass();
        }

        if (List.class.isAssignableFrom(aClass)) {
            return List.class;
        }

        if (Map.class.isAssignableFrom(aClass)) {
            return Map.class;
        }

        return aClass;
    }

    private Object[] asArrayOfValues(final Object... constructorParameters) {

        return of(constructorParameters)
                .map(this::toValue)
                .collect(toList())
                .toArray(new Object[constructorParameters.length]);
    }

    private Object toValue(final Object o) {
        if (o instanceof NullParameter) {
            return null;
        }

        return o;
    }
}
