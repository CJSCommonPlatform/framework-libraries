package uk.gov.justice.services.validator.domain;

import uk.gov.justice.domain.annotation.Event;

@Event(value = "service.invalid")
public class ClassWithInvalidEventAnnotation {
}
