package uk.gov.justice.maven.generator.io.files.parser.generator.parser;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.FileParserFactory;

public class TestFileParserFactory implements FileParserFactory<String> {

    public FileParser<String> create() {
        return new TestFileParser();
    }
}
