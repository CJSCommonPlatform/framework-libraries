package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;

import uk.gov.justice.generation.pojo.dom.Definition;

import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class DefinitionToTypeNameConverter {

    public TypeName getTypeName(final Definition definition) {

        final ClassName className = get(
                definition.getClassName().getPackageName(),
                definition.getClassName().getSimpleName()
        );

        final Optional<uk.gov.justice.generation.pojo.dom.ClassName> genericTypeOptional = definition.getGenericType();

        if(genericTypeOptional.isPresent()) {

            final uk.gov.justice.generation.pojo.dom.ClassName genericType = genericTypeOptional.get();

            final ClassName type = ClassName.get(genericType.getPackageName(), genericType.getSimpleName());
            return ParameterizedTypeName.get(className, type);
        }

        return className;
    }
}
