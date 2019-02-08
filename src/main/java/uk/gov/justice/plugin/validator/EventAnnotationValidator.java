package uk.gov.justice.plugin.validator;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.plugin.domain.ValidationResult;
import uk.gov.justice.plugin.exception.ValidationException;

import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class EventAnnotationValidator implements AnnotationValidator {

    @Override
    public ValidationResult validate(final Class<?> classWithAnnotation, final Map<String, String> validationProperties) {

        final Event annotation = classWithAnnotation.getAnnotation(Event.class);

        final String serviceName = validationProperties.get("serviceName");
        if (isBlank(serviceName)) {
            throw new ValidationException("Service name should be supplied");
        }

        final String annotationValue = annotation.value();
        if (getEventPattern(serviceName).matcher(annotationValue).matches()) {
            return new ValidationResult(classWithAnnotation.getName(), true, annotationValue);
        }

        return new ValidationResult(classWithAnnotation.getName(), false, annotationValue);
    }

    private Pattern getEventPattern(final String serviceName) {
        // example matching pattern: service.events.something-happened
        return compile(serviceName + "\\.events\\.[a-z]+.[a-z0-9\\-]+[a-z0-9]+$");
    }
}
