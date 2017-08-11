package uk.gov.justice.maven.generator.io.files.parser.generator;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;

import java.lang.reflect.Constructor;
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

            if (!generators.containsKey(generatorName)) {
                generators.put(generatorName, createGenerator(generatorName));
            }

            return generators.get(generatorName);
        }

    private Generator createGenerator(final String generatorName) {

        try {
            final Generator generator;
            final Class<?> clazz = Class.forName(generatorName);

            final Constructor<?> constructor = clazz.getConstructor();
            generator = (Generator) constructor.newInstance();
            return generator;
        } catch (final ReflectiveOperationException e) {
            throw new IllegalArgumentException(String.format("Could not instantiate generator %s", generatorName), e);
        }
    }

}
