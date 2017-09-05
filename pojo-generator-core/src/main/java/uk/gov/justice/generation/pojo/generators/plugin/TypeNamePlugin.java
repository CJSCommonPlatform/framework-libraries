package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.generation.pojo.dom.Definition;

import com.squareup.javapoet.TypeName;

public interface TypeNamePlugin {

    TypeName modifyTypeName(final TypeName typeName, final Definition definition);
}
