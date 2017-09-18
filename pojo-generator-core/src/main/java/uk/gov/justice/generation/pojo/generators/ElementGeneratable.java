package uk.gov.justice.generation.pojo.generators;

import java.util.stream.Stream;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

/**
 * Interface that defines all of the generators whether they be a {@link FieldGenerator} or
 * an {@link ElementGenerator}
 */
public interface ElementGeneratable {

    /**
     * Generate this element as a field
     * @return This element as a field
     */
    FieldSpec generateField();

    /**
     * Generate this elements methods (i.e. getters and setters)
     * @return
     */
    Stream<MethodSpec> generateMethods();
}
