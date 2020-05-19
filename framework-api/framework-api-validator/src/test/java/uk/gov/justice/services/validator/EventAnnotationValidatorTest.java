package uk.gov.justice.services.validator;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.maven.annotation.domain.ValidationResult;
import uk.gov.justice.maven.annotation.exception.ValidationException;
import uk.gov.justice.services.validator.domain.ClassWithInvalidEventAnnotation;
import uk.gov.justice.services.validator.domain.ClassWithValidEventAnnotation;

import org.junit.Before;
import org.junit.Test;

public class EventAnnotationValidatorTest {

    private EventAnnotationValidator validator;

    @Before
    public void setup() {
        validator = new EventAnnotationValidator();
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionIfServiceNameNotProvided() {
        validator.validate(ClassWithValidEventAnnotation.class, of());
    }

    @Test
    public void shouldIndicateValidationFailureWhenEventNameViolatesPattern() {
        final ValidationResult result = validator.validate(ClassWithInvalidEventAnnotation.class, of("serviceName", "service"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithInvalidEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("service.invalid"));
        assertThat(result.isValidationPassed(), is(false));
    }

    @Test
    public void shouldIndicateValidationSuccessWhenEventNameConformsToPattern() {
        final ValidationResult result = validator.validate(ClassWithValidEventAnnotation.class, of("serviceName", "service"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithValidEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("service.events.valid"));
        assertThat(result.isValidationPassed(), is(true));
    }

}