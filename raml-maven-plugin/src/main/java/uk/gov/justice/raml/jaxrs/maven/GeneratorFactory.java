package uk.gov.justice.raml.jaxrs.maven;

import uk.gov.justice.raml.core.Generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory for generators
 */
public class GeneratorFactory {

    /**
     * Create a generator from the provided fullyb qualified classname.
     * @param generatorName fully qualified classname that implements {@link Generator}
     * @return the generator
     */
    public Generator create(final String generatorName) {

        try {

            Class<?> clazz = Class.forName(generatorName);
            Constructor<?> constructor = clazz.getConstructor();
            return (Generator) constructor.newInstance();

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new IllegalArgumentException(String.format("Could not instantiate generator %s", generatorName), e);

        }

    }

}
