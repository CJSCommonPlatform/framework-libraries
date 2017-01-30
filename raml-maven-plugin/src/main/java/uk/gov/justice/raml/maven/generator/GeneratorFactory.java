package uk.gov.justice.raml.maven.generator;

import uk.gov.justice.raml.core.Generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for generators
 */
public class GeneratorFactory {
    private Map<String, Generator> generators = new HashMap<>();


    /**
     * Returns an instance of a generator.
     *
     * @param generatorName fully qualified classname that implements {@link Generator}
     * @return the generator
     */
    public Generator instanceOf(final String generatorName) {

        try {
            Generator generator = generators.get(generatorName);
            if (generator == null) {
                Class<?> clazz = Class.forName(generatorName);
                Constructor<?> constructor = clazz.getConstructor();
                generator = (Generator) constructor.newInstance();
                generators.put(generatorName, generator);
            }
            return generator;

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new IllegalArgumentException(String.format("Could not instantiate generator %s", generatorName), e);

        }

    }

}
