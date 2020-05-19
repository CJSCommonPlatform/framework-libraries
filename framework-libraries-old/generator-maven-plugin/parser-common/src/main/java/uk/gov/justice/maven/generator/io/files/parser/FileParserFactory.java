package uk.gov.justice.maven.generator.io.files.parser;

public interface FileParserFactory<T> {

    FileParser<T> create();
}
