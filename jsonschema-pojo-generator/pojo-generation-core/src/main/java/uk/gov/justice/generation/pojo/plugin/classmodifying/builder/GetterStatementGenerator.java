package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import com.squareup.javapoet.TypeName;

public class GetterStatementGenerator {

    private final OptionalTypeNameUtil optionalTypeNameUtil;

    public GetterStatementGenerator(final OptionalTypeNameUtil optionalTypeNameUtil) {
        this.optionalTypeNameUtil = optionalTypeNameUtil;
    }

    public String generateGetterStatement(
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext,
            final String parameterName,
            final Definition fieldDefinition) {

        final TypeName typeName = classNameFactory.createTypeNameFrom(fieldDefinition, pluginContext);
        final boolean isOptionalType = optionalTypeNameUtil.isOptionalType(typeName);

        if (isOptionalType) {
            return parameterName + ".get" + capitalize(fieldDefinition.getFieldName()) + "().orElse(null)";
        }

        return parameterName + ".get" + capitalize(fieldDefinition.getFieldName()) + "()";
    }
}
