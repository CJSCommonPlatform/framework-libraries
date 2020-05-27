package uk.gov.justice.maven.generator.io.files.parser.generator;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.maven.generator.io.files.parser.core.GeneratorConfig;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.raml.model.Raml;

/**
 * Captures execution arguments of the DummyGenerator. Single threaded, will fail if tests were
 * configured to run in parallel.
 */
public class DummyGeneratorCaptor {
    private static final DummyGeneratorCaptor instance = new DummyGeneratorCaptor();
    private List<Pair<Raml, GeneratorConfig>> capturedArgs = new LinkedList<>();

    public static DummyGeneratorCaptor getInstance() {
        return instance;
    }

    public void init() {
        capturedArgs.clear();
    }

    public void capture(Raml raml, GeneratorConfig generatorConfig) {
        capturedArgs.add(Pair.of(raml, generatorConfig));
    }

    public List<Pair<Raml, GeneratorConfig>> capturedArgs() {
        return capturedArgs;
    }

    public List<Raml> capturedRamls() {
        return capturedArgs.stream().map(Pair::getKey).collect(toList());
    }

}
