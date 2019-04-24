package uk.gov.justice.json.jolt;

import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.ValidationException;

public class ValidationExceptionHandler {

    public List<String> handleValidationException(ValidationException e) {
        final ArrayList<String> errorMessages = new ArrayList<>();

        if (e.getCausingExceptions().size() == 0) {
            errorMessages.add(e.getMessage());
            return errorMessages;
        }
        return causingExceptionInfo(e.getCausingExceptions(), errorMessages);
    }

    private static List<String> causingExceptionInfo(final List<ValidationException> causingExceptions,
                                                     final List<String> errorMessages) {

        for (final ValidationException causingException : causingExceptions) {
            errorMessages.add(causingException.getMessage());

            final List<ValidationException> subExceptions = causingException.getCausingExceptions();
            if (subExceptions.size() > 0) {
                causingExceptionInfo(subExceptions, errorMessages);
            }
        }
        return errorMessages;
    }
}
