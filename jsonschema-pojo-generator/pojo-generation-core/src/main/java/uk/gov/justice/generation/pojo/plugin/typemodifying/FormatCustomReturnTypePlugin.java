package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.STRING;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.TypeMappingPredicate.FORMAT_TYPE;

import uk.gov.justice.generation.pojo.core.TypeMapping;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.StringDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.FullyQualifiedNameToClassNameConverter;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Adds support for using format custom java types as return types and constructor parameters in the
 * generated class.
 *
 * To Use: Set the typeMappings tag in the generatorProperties tag.  The {@code <name>} of the type
 * mapping corresponds to the format value of a string field in the schema, the {@code
 * <implementation>} is the Java Class type to be used when generating the Java POJO, and the {@code
 * <type>} defines this as a format mapping type.  For example:
 *
 * <pre>
 *      {@code
 *
 *         <typeMappings>
 *              <typeMapping>
 *                  <name>date-time</name>
 *                  <type>format</type>
 *                  <implementation>java.time.ZonedDateTime</implementation>
 *              </typeMapping>
 *         </typeMappings>
 *      }
 * </pre>
 *
 * The name value of "date" will be the definition name and is referenced in your json schema file:
 *
 * <pre>
 *      {@code
 *
 *              "myProperty": {
 *                  "type": "string",
 *                  "format": "date-time"
 *              }
 *     }
 * </pre>
 *
 * <p>This will generate the following code:</p>
 *
 * <pre>
 *          {@code
 *
 *              public class MyClass {
 *
 *                  private final ZonedDateTime myProperty;
 *
 *                  public MyClass(final ZonedDateTime myProperty) {
 *                      this.myProperty = myProperty;
 *                  }
 *
 *                  public ZonedDateTime getMyProperty() {
 *                      return myProperty;
 *                  }
 *              }
 *          }
 * </pre>
 */
public class FormatCustomReturnTypePlugin implements TypeModifyingPlugin {

    private final CustomReturnTypeMapper customReturnTypeMapper;
    private final Predicate<TypeMapping> typeMappingPredicate;

    public FormatCustomReturnTypePlugin(final CustomReturnTypeMapper customReturnTypeMapper,
                                        final Predicate<TypeMapping> typeMappingPredicate) {
        this.customReturnTypeMapper = customReturnTypeMapper;
        this.typeMappingPredicate = typeMappingPredicate;
    }

    @FactoryMethod
    public static FormatCustomReturnTypePlugin formatCustomReturnTypePlugin() {

        final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter =
                new FullyQualifiedNameToClassNameConverter();
        final CustomReturnTypeMapper customReturnTypeMapper =
                new CustomReturnTypeMapper(fullyQualifiedNameToClassNameConverter);

        return new FormatCustomReturnTypePlugin(customReturnTypeMapper, FORMAT_TYPE);
    }

    /**
     * Modifies the class name to that specified by the schema string format type mapping in
     * generator properties.
     *
     * @param typeName      The type name to be modified
     * @param definition    The FieldDefinition of the type to be modified
     * @param pluginContext The {@link PluginContext}
     * @return the {@link TypeName}
     */
    @Override
    public TypeName modifyTypeName(final TypeName typeName,
                                   final Definition definition,
                                   final PluginContext pluginContext) {

        if (STRING.equals(definition.type())) {
            final StringDefinition stringDefinition = (StringDefinition) definition;

            final Function<String, Optional<ClassName>> classNameForFormat = value ->
                    customReturnTypeMapper.customTypeFor(typeMappingPredicate, value, pluginContext);

            final Optional<ClassName> className = stringDefinition.getFormat()
                    .flatMap(classNameForFormat);

            if (className.isPresent()) {
                return className.get();
            }
        }

        return typeName;
    }
}
