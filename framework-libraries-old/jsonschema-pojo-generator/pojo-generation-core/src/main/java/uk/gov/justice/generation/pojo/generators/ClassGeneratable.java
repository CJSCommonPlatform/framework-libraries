package uk.gov.justice.generation.pojo.generators;

import com.squareup.javapoet.TypeSpec;

/**
 * Interface of all generators that will generate a java class, namely
 * {@link ClassGenerator} and {@link EnumGenerator}. It is also implemented
 * by {@link uk.gov.justice.generation.pojo.plugin.classmodifying.builder.BuilderGenerator}
 * as the class's builder is a static inner class in it's parent POJO
 */
public interface ClassGeneratable {

    /**
     * Generates a java POJO class
     *
     * @return A fully specified java poet {@link TypeSpec}
     */
    TypeSpec generate();

    /**
     * Gets the simple name of the POJO that is to be generated
     *
     * @return The simple name of the POJO
     */
    String getSimpleClassName();

    /**
     * Gets the package name of the POJO that is to be generated
     *
     * @return The package name of the POJO
     */
    String getPackageName();
}
