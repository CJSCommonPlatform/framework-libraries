package uk.gov.justice.raml.core;

import org.raml.model.Raml;

public interface Generator {

    void run(Raml raml, GeneratorConfig generatorConfig);

}
