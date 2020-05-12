package uk.gov.justice.maven.generator.io.files.parser.core;

public interface Generator<T> {

    void run(T source, GeneratorConfig generatorConfig);
}
