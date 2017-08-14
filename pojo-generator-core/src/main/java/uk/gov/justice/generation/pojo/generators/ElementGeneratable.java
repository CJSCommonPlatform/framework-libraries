package uk.gov.justice.generation.pojo.generators;

import java.util.stream.Stream;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public interface ElementGeneratable {

    FieldSpec generateField();

    Stream<MethodSpec> generateMethods();
}
