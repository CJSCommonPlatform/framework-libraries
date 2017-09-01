package uk.gov.justice.generation.pojo.generators;

import com.squareup.javapoet.TypeSpec;

public interface ClassGeneratable {

    TypeSpec generate();

    String getClassName();
}
