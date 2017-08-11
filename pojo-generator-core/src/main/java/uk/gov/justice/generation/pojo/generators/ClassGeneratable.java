package uk.gov.justice.generation.pojo.generators;

import uk.gov.justice.generation.pojo.dom.ClassName;

import com.squareup.javapoet.TypeSpec;

public interface ClassGeneratable {

    TypeSpec generate();

    ClassName getClassName();
}
