package uk.gov.justice.generation.pojo.generators;

/**
 * Exception for thowing if threre is an error during the generation of *.java files
 */
public class GenerationException extends RuntimeException {

    public GenerationException(final String message) {
        super(message);
    }
}
