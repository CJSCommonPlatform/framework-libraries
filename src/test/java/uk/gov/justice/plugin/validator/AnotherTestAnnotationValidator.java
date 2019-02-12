package uk.gov.justice.plugin.validator;


import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

import uk.gov.justice.plugin.domain.AnotherTestAnnotation;
import uk.gov.justice.plugin.domain.ValidationResult;
import uk.gov.justice.plugin.exception.ValidationException;

import java.util.Map;

public class AnotherTestAnnotationValidator implements AnnotationValidator<AnotherTestAnnotation> {

    @Override
    public ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) throws ValidationException {
        final AnotherTestAnnotation annotation = classWithAnnotation.getAnnotation(AnotherTestAnnotation.class);

        final String annotationValue = annotation.value();
        if (isBlank(annotationValue)) {
            final String validationMessageText = defaultIfBlank(validationProperties.get("validationMessageText"), "");
            return new ValidationResult(classWithAnnotation.getName(), false, validationMessageText);
        }

        return new ValidationResult(classWithAnnotation.getName(), true, annotationValue);
    }

}
