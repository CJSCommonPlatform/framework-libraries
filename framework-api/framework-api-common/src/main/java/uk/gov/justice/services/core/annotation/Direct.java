package uk.gov.justice.services.core.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.context.ApplicationScoped;

@Retention(RUNTIME)
@Target(TYPE)
@ApplicationScoped
public @interface Direct {
    String target();
}
