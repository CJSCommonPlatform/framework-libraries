package uk.gov.justice.generation.pojo.plugin;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a public static method on a plugin as a factory method for creating an instance
 * of the plugin
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface FactoryMethod {
}
