package uk.gov.justice.generation.pojo.generators;

import static com.squareup.javapoet.ClassName.get;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;

import uk.gov.justice.generation.pojo.core.ClassNameParser;
import uk.gov.justice.generation.pojo.core.GenerationContext;
import uk.gov.justice.generation.pojo.core.PackageNameParser;
import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.PluginContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * Gets the correct class name for a java type. Used for return types, fields and parameters. Can
 * handle generics and lists
 */
public class TypeNameProvider {

    private static final int FIRST_CHILD = 0;

    private final GenerationContext generationContext;
    private final PackageNameParser packageNameParser;
    private final ClassNameParser classNameParser;

    public TypeNameProvider(final GenerationContext generationContext,
                            final PackageNameParser packageNameParser,
                            final ClassNameParser classNameParser) {
        this.generationContext = generationContext;
        this.packageNameParser = packageNameParser;
        this.classNameParser = classNameParser;
    }

    /**
     * Generates the correct class name for an array. As an array is always implemented as a java
     * {@link List} the class name will always be a list of the type specified by the {@link
     * Definition}
     *
     * @param definition       The {@link Definition} from which to create the class name
     * @param classNameFactory The {@link ClassNameFactory} to create the underlying class name (not
     *                         including any generics)
     * @return The correct type name
     */
    public TypeName typeNameForArray(
            final Definition definition,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext) {

        final ClassDefinition classDefinition = (ClassDefinition) definition;

        if (classDefinition.getFieldDefinitions().isEmpty()) {
            throw new GenerationException(format("No definition present for array types. For field: %s", classDefinition.getFieldName()));
        }

        final Definition childDefinition = classDefinition.getFieldDefinitions().get(FIRST_CHILD);
        final TypeName typeName = classNameFactory.createTypeNameFrom(childDefinition, pluginContext);

        return ParameterizedTypeName.get(get(List.class), typeName);
    }

    /**
     * Creates the correct java type name for a {@link ReferenceDefinition} by using the child
     * reference as the java type. Can handle arrays and generics
     *
     * @param definition       The {@link ReferenceDefinition}
     * @param classNameFactory The {@link ClassNameFactory} to create the underlying class name (not
     *                         including any generics)
     * @return The correct type name
     */
    public TypeName typeNameForReference(
            final Definition definition,
            final ClassNameFactory classNameFactory,
            final PluginContext pluginContext) {
        final ReferenceDefinition referenceDefinition = (ReferenceDefinition) definition;

        if (referenceDefinition.getFieldDefinitions().isEmpty()) {
            throw new GenerationException(format("No definition present for reference type. For field: %s", referenceDefinition.getFieldName()));
        }

        final Definition childDefinition = referenceDefinition.getFieldDefinitions().get(FIRST_CHILD);
        return classNameFactory.createTypeNameFrom(childDefinition, pluginContext);
    }

    /**
     * Generates the correct type name for a {@link String}
     *
     * @return The class name for {@link String}
     */
    public ClassName typeNameForString() {
        return get(String.class);
    }

    /**
     * Generates the correct type name for a java class
     *
     * @param definition The definition from which to generated the type name
     * @return The class name for a java class
     */
    public ClassName typeNameForClass(final Definition definition) {
        final ClassDefinition classDefinition = (ClassDefinition) definition;
        final Optional<String> id = classDefinition.getId();

        if (id.isPresent()) {
            final String uri = id.get();
            final String packageName = packageNameParser.appendToBasePackage(uri, generationContext.getPackageName());

            return get(packageName, classNameParser.simpleClassNameFrom(uri));
        }

        return get(generationContext.getPackageName(), capitalize(definition.getFieldName()));
    }

    /**
     * Generates the correct type name for a {@link BigDecimal}
     *
     * @return The class name for {@link BigDecimal}
     */
    public ClassName typeNameForNumber() {
        return get(BigDecimal.class);
    }

    /**
     * Generates the correct type name for a {@link Integer}
     *
     * @return The class name for {@link Integer}
     */
    public ClassName typeNameForInteger() {
        return get(Integer.class);
    }

    /**
     * Generates the correct type name for a {@link Boolean}
     *
     * @return The class name for {@link Boolean}
     */
    public ClassName typeNameForBoolean() {
        return get(Boolean.class);
    }
}
