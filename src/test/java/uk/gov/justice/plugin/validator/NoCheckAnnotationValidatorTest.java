package uk.gov.justice.plugin.validator;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import uk.gov.justice.plugin.domain.ValidationResult;
import uk.gov.justice.plugin.domain.ClassWithInvalidEventAnnotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NoCheckAnnotationValidatorTest {

    @Test
    public void shouldAlwaysIndicateValidationPassed() {
        final ValidationResult result = new NoCheckAnnotationValidator().validate(ClassWithInvalidEventAnnotation.class, ImmutableMap.of());
        assertThat(result.isValidationPassed(), is(true));
        assertThat(result.getValidationText(), is(""));
        assertThat(result.getClassWithAnnotation(), is(ClassWithInvalidEventAnnotation.class.getName()));
    }
}