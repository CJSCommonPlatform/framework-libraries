package uk.gov.justice.maven.generator.io.files.parser.core;

public interface GeneratorFactory<T> {

    Generator<T> create();
}
