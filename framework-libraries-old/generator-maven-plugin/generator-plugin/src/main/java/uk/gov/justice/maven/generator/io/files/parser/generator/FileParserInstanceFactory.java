package uk.gov.justice.maven.generator.io.files.parser.generator;

import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import uk.gov.justice.maven.generator.io.files.parser.FileParser;
import uk.gov.justice.maven.generator.io.files.parser.FileParserFactory;

public class FileParserInstanceFactory {

    public FileParser newInstanceOf(final String parserName) {

        try {
            final Object instance = forName(parserName).newInstance();

            if (isFileParserFactory(instance)) {
                final FileParserFactory fileParserFactory = (FileParserFactory) instance;
                return fileParserFactory.create();
            }

            return (FileParser) instance;

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new FileParserInstantiationException(format("Failed to instantiate file parser: %s", parserName), ex);
        }
    }

    private boolean isFileParserFactory(final Object instance) {
        return asList(instance.getClass().getInterfaces()).contains(FileParserFactory.class);
    }
}
