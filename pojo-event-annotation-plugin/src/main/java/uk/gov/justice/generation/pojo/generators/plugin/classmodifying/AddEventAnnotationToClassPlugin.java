package uk.gov.justice.generation.pojo.generators.plugin.classmodifying;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.ROOT;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

public class AddEventAnnotationToClassPlugin implements ClassModifyingPlugin {

    private static final char DOT_CHARACTER = '.';
    private static final int BEGIN_INDEX = 0;

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder classBuilder,
                                         final ClassDefinition classDefinition,
                                         final PluginContext pluginContext) {

        if (ROOT.equals(classDefinition.type())) {

            final String eventName = removeFileExtensionFrom(pluginContext.getSourceFilename());

            classBuilder.addAnnotation(AnnotationSpec.builder(Event.class)
                    .addMember("value", "$S", eventName)
                    .build());
        }

        return classBuilder;
    }

    private String removeFileExtensionFrom(final String fileName) {
        return fileName.substring(BEGIN_INDEX, fileName.lastIndexOf(DOT_CHARACTER));
    }
}
