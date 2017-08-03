package uk.gov.justice.raml.core;

public interface Generator<T> {

    void run(T source, GeneratorConfig generatorConfig);
}
