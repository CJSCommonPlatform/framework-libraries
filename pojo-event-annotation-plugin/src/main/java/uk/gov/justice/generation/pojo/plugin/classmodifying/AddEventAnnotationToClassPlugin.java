package uk.gov.justice.generation.pojo.plugin.classmodifying;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Plugin for adding the {@link Event} annotation to the top of the root POJO.
 * The name of the event will be taked from the json schema document
 */
public class AddEventAnnotationToClassPlugin implements ClassModifyingPlugin {

    private static final char DOT_CHARACTER = '.';
    private static final int BEGIN_INDEX = 0;

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder classBuilder,
                                         final ClassDefinition classDefinition,
                                         final PluginContext pluginContext) {

        if (classDefinition.isRoot()) {

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
