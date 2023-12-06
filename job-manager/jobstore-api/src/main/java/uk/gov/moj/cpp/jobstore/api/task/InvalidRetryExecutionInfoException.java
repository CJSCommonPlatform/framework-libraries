package uk.gov.moj.cpp.jobstore.api.task;

/**
 * Exception thrown when there's error accessing database
 */
public class InvalidRetryExecutionInfoException extends RuntimeException {

    private static final long serialVersionUID = 5934757852541630746L;

    public InvalidRetryExecutionInfoException(String message) {
        super(message);
    }
}
