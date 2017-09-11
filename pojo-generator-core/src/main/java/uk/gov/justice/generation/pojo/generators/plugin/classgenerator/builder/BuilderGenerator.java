package uk.gov.justice.generation.pojo.generators.plugin.classgenerator.builder;

import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.generators.ClassGeneratable;
import uk.gov.justice.generation.pojo.generators.ClassNameFactory;

import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class BuilderGenerator implements ClassGeneratable {

    private static final String BUILDER_SIMPLE_NAME = "Builder";

    private final ClassDefinition classDefinition;
    private final ClassNameFactory classNameFactory;
    private final BuilderFieldFactory builderFieldFactory;
    private final BuilderMethodFactory builderMethodFactory;

    public BuilderGenerator(
            final ClassDefinition classDefinition,
            final ClassNameFactory classNameFactory,
            final BuilderFieldFactory builderFieldFactory,
            final BuilderMethodFactory builderMethodFactory) {
        this.classDefinition = classDefinition;
        this.classNameFactory = classNameFactory;
        this.builderFieldFactory = builderFieldFactory;
        this.builderMethodFactory = builderMethodFactory;
    }

    @Override
    public TypeSpec generate() {

        final ClassName pojoClassName = classNameFactory.createClassNameFrom(classDefinition);

        final List<Definition> fieldDefinitions = classDefinition.getFieldDefinitions();
        final List<FieldSpec> fieldSpecs = builderFieldFactory.createFields(fieldDefinitions, classNameFactory);
        final List<MethodSpec> withMethods = builderMethodFactory.createTheWithMethods(
                fieldDefinitions,
                classNameFactory,
                getBuilderClassName());

        final MethodSpec buildMethod = builderMethodFactory.createTheBuildMethod(fieldDefinitions, pojoClassName);

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

    public MethodSpec generateStaticGetBuilderMethod() {
        return methodBuilder(classDefinition.getFieldName())
                .addModifiers(PUBLIC, STATIC)
                .returns(getBuilderClassName())
                .addCode(CodeBlock.builder().addStatement("return new $L()", getBuilderClassName()).build())
                .build();
    }

    private ClassName getBuilderClassName() {
        return classNameFactory.createClassNameFrom(classDefinition)
                .nestedClass(BUILDER_SIMPLE_NAME);
    }
}
