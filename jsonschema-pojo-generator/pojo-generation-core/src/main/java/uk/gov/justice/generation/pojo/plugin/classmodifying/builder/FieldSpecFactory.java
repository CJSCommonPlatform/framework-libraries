package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static javax.lang.model.element.Modifier.PRIVATE;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class FieldSpecFactory {

    private final OptionalTypeNameUtil optionalTypeNameUtil;

    public FieldSpecFactory(final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.optionalTypeNameUtil = optionalTypeNameUtil;
    }

    public FieldSpec createFieldSpecFor(final Definition fieldDefinition,
                                        final ClassNameFactory classNameFactory,
                                        final PluginContext pluginContext) {

        final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);

        if (optionalTypeNameUtil.isOptionalType(typeName)) {
            final TypeName parameterizedType = optionalTypeNameUtil.getOptionalTypeFrom((ParameterizedTypeName) typeName);
            return FieldSpec
                    .builder(parameterizedType, fieldDefinition.getFieldName(), PRIVATE)
                    .build();
        }

        return FieldSpec
                .builder(typeName, fieldDefinition.getFieldName(), PRIVATE)
                .build();
    }
}
