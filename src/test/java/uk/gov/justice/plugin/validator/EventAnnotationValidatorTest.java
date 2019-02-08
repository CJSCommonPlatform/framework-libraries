package uk.gov.justice.plugin.validator;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.gov.justice.plugin.domain.ValidationResult;
import uk.gov.justice.plugin.exception.ValidationException;
import uk.gov.justice.plugin.domain.ClassWithInvalidEventAnnotation;
import uk.gov.justice.plugin.domain.ClassWithValidEventAnnotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EventAnnotationValidatorTest {

    private EventAnnotationValidator validator;

    @Before
    public void setup() {
        validator = new EventAnnotationValidator();
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionIfServiceNameNotProvided() {
        validator.validate(ClassWithValidEventAnnotation.class, ImmutableMap.of());
    }

    @Test
    public void shouldIndicateValidationFailureWhenEventNameViolatesPattern() {
        final ValidationResult result = validator.validate(ClassWithInvalidEventAnnotation.class, ImmutableMap.of("serviceName", "structure"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithInvalidEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("structure.invalid"));
        assertThat(result.isValidationPassed(), is(false));
    }

    @Test
    public void shouldIndicateValidationSuccessWhenEventNameConformsToPattern() {
        final ValidationResult result = validator.validate(ClassWithValidEventAnnotation.class, ImmutableMap.of("serviceName", "structure"));
        assertThat(result.getClassWithAnnotation(), is(ClassWithValidEventAnnotation.class.getName()));
        assertThat(result.getValidationText(), is("structure.events.valid"));
        assertThat(result.isValidationPassed(), is(true));
    }

}