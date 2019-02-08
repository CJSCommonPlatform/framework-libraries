package uk.gov.justice.plugin.validator;

import uk.gov.justice.domain.annotation.Event;

public class AnnotationValidatorFactory {

    public static AnnotationValidator getValidator(final String annotationClass) {
        if (Event.class.getName().equalsIgnoreCase(annotationClass)) {
            return new EventAnnotationValidator();
        } else {
            // empty validator
            return new NoCheckAnnotationValidator();
        }

    }
}
