package uk.gov.justice.raml.jaxrs.maven;

import org.apache.commons.lang3.tuple.Pair;
import org.raml.model.Raml;
import uk.gov.justice.raml.core.GeneratorConfig;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Captures execution arguments of the DummyGenerator.
 * Single threaded, will fail if tests were configured to run in parallel.
 */
public class DummyGeneratorCaptor {
    private List<Pair<Raml, GeneratorConfig>> capturedArgs = new LinkedList<>();

    private static final DummyGeneratorCaptor instance = new DummyGeneratorCaptor();

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
