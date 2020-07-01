package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static uk.gov.justice.generation.pojo.plugin.classmodifying.AddAdditionalPropertiesToClassPlugin.ADDITIONAL_PROPERTIES_FIELD_NAME;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.classmodifying.properties.AdditionalPropertiesDeterminer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * A {@link ClassGeneratable} for creating a Builder as a static class inside your POJO
 */
public class BuilderGenerator implements ClassGeneratable {

    private static final String BUILDER_SIMPLE_NAME = "Builder";

    private final ClassDefinition classDefinition;
    private final ClassNameFactory classNameFactory;
    private final BuilderFieldFactory builderFieldFactory;
    private final BuilderMethodFactory builderMethodFactory;
    private final AdditionalPropertiesDeterminer additionalPropertiesDeterminer;
    private final PluginContext pluginContext;

    public BuilderGenerator(
            final ClassDefinition classDefinition,
            final ClassNameFactory classNameFactory,
            final BuilderFieldFactory builderFieldFactory,
            final BuilderMethodFactory builderMethodFactory,
            final AdditionalPropertiesDeterminer additionalPropertiesDeterminer,
            final PluginContext pluginContext) {
        this.classDefinition = classDefinition;
        this.classNameFactory = classNameFactory;
        this.builderFieldFactory = builderFieldFactory;
        this.builderMethodFactory = builderMethodFactory;
        this.additionalPropertiesDeterminer = additionalPropertiesDeterminer;
        this.pluginContext = pluginContext;
    }

    @Override
    public TypeSpec generate() {

        final ClassName pojoClassName = classNameFactory.createClassNameFrom(classDefinition);

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();
        final List<FieldSpec> fieldSpecs = createFields(fieldDefinitions);
        final List<MethodSpec> withMethods = createWithMethods(fieldDefinitions);
        final MethodSpec buildMethod = buildMethod(pojoClassName, fieldDefinitions);


        return classBuilder(BUILDER_SIMPLE_NAME)
                .addModifiers(PUBLIC, STATIC)
                .addFields(fieldSpecs)
                .addMethods(withMethods)
                .addMethod(buildMethod)
                .build();
    }

    @Override
    public String getSimpleClassName() {
        return BUILDER_SIMPLE_NAME;
    }

    @Override
    public String getPackageName() {
        throw new UnsupportedOperationException();
    }

    public MethodSpec generateStaticGetBuilderMethod() {
        return methodBuilder(uncapitalize(classNameFactory.createClassNameFrom(classDefinition).simpleName()))
                .addModifiers(PUBLIC, STATIC)
                .returns(getBuilderClassName())
                .addCode(CodeBlock.builder().addStatement("return new $L()", getBuilderClassName()).build())
                .build();
    }

    private ClassName getBuilderClassName() {
        return classNameFactory.createClassNameFrom(classDefinition)
                .nestedClass(BUILDER_SIMPLE_NAME);
    }

    private List<FieldSpec> createFields(final List<Definition> fieldDefinitions) {
        final List<FieldSpec> fields = new ArrayList<>(builderFieldFactory.createFields(
                fieldDefinitions,
                classNameFactory,
                pluginContext));

        if (additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)) {
            final ParameterizedTypeName map = ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    TypeName.get(String.class),
                    TypeName.get(Object.class));

            final ParameterizedTypeName hashMap = ParameterizedTypeName.get(
                    ClassName.get(HashMap.class),
                    TypeName.get(String.class),
                    TypeName.get(Object.class));

            final FieldSpec additionalProperties = FieldSpec.builder(map, ADDITIONAL_PROPERTIES_FIELD_NAME)
                    .addModifiers(PRIVATE, FINAL)
                    .initializer("new $T()", hashMap)
                    .build();

            fields.add(additionalProperties);
        }

        return fields;
    }

    private List<MethodSpec> createWithMethods(final List<Definition> fieldDefinitions) {

        final ClassName builderClassName = getBuilderClassName();
        if (additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)) {
            return builderMethodFactory.createTheWithMethodsWithAdditionalProperties(
                    fieldDefinitions,
                    classNameFactory,
                    builderClassName,
                    pluginContext
            );
        }

        return builderMethodFactory.createTheWithMethods(
                fieldDefinitions,
                classNameFactory,
                builderClassName,
                pluginContext);
    }

    private MethodSpec buildMethod(final ClassName pojoClassName, final List<Definition> fieldDefinitions) {

        if (additionalPropertiesDeterminer.shouldAddAdditionalProperties(classDefinition, pluginContext)) {
            return builderMethodFactory.createTheBuildMethodWithAdditionalProperties(fieldDefinitions, pojoClassName, pluginContext);
        }

        return builderMethodFactory.createTheBuildMethod(fieldDefinitions, pojoClassName, pluginContext);
    }
}
