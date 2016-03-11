package uk.gov.justice.raml.core;

import org.raml.model.Raml;

import java.util.Set;

public interface Generator {

    void run(Raml raml, GeneratorConfig generatorConfig);

}
