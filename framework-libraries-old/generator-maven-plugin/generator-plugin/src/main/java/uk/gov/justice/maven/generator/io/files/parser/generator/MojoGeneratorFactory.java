package uk.gov.justice.maven.generator.io.files.parser.generator;

import static java.util.Arrays.asList;

import uk.gov.justice.maven.generator.io.files.parser.core.Generator;
import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for generators
 */
public class MojoGeneratorFactory {

    private final Map<String, Generator> generators = new HashMap<>();

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
            final Class<?> generatorClass = Class.forName(generatorName);

            if (isGeneratorFactory(generatorClass)) {
                final GeneratorFactory generatorFactory = (GeneratorFactory) generatorClass.newInstance();

                return generatorFactory.create();
            }

            return (Generator) generatorClass.newInstance();
        } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException(String.format("Could not instantiate generator %s", generatorName), e);
        }
    }

    private boolean isGeneratorFactory(final Class<?> generatorClass) {
        return asList(generatorClass.getInterfaces()).contains(GeneratorFactory.class);
    }
}
