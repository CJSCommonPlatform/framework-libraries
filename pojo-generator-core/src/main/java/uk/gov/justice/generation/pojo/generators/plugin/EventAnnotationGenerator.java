package uk.gov.justice.generation.pojo.generators.plugin;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.JavaGeneratorFactory;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

public class EventAnnotationGenerator implements PluginClassGeneratable {

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                         final ClassDefinition classDefinition,
                                         final JavaGeneratorFactory javaGeneratorFactory) {

        classDefinition.getEventName().ifPresent(eventName ->
                typeSpecBuilder.addAnnotation(AnnotationSpec.builder(Event.class)
                        .addMember("value", "$S", eventName)
                        .build()));

        return typeSpecBuilder;
    }
}
