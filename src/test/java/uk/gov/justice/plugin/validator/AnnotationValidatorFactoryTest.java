package uk.gov.justice.plugin.validator;

import org.junit.Test;
import uk.gov.justice.domain.annotation.Event;

import javax.inject.Inject;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.plugin.validator.AnnotationValidatorFactory.getValidator;


public class AnnotationValidatorFactoryTest {

    @Test
    public void getValidatorForEventAnnotationType() {
        final AnnotationValidator validator = getValidator(Event.class.getName());
        assertThat(validator, instanceOf(EventAnnotationValidator.class));
    }

    @Test
    public void getValidatorForOtherTypes() {
        final AnnotationValidator validator = getValidator(Inject.class.getName());
        assertThat(validator, instanceOf(NoCheckAnnotationValidator.class));
    }
}