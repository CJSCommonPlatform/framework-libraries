package uk.gov.justice.generation.pojo.generators.plugin.typename;

import uk.gov.justice.generation.pojo.dom.Definition;

import com.squareup.javapoet.TypeName;

public interface TypeModifyingPlugin {

    TypeName modifyTypeName(final TypeName typeName, final Definition definition);
}
