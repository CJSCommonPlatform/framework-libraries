package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static uk.gov.justice.generation.pojo.dom.DefinitionType.REFERENCE;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.TypeMappingPredicate.REFERENCE_TYPE;

import uk.gov.justice.generation.pojo.core.TypeMapping;
import uk.gov.justice.generation.pojo.dom.Definition;
import uk.gov.justice.generation.pojo.dom.ReferenceDefinition;
import uk.gov.justice.generation.pojo.plugin.FactoryMethod;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.CustomReturnTypeMapper;
import uk.gov.justice.generation.pojo.plugin.typemodifying.custom.FullyQualifiedNameToClassNameConverter;
import uk.gov.justice.generation.pojo.visitor.ReferenceValue;

import java.util.Optional;
import java.util.function.Predicate;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Adds support for using reference custom java types as return types and constructor parameters in
 * the generated class.
 *
 * To Use: Set the typeMappings tag in the generatorProperties tag.  The {@code <name>} of the type
 * mapping corresponds to the name of the definition being referenced in the schema, the {@code
 * <implementation>} is the Java Class type to be used when generating the Java POJO, and the {@code
 * <type>} defines this as a reference mapping type.  For example:
 *
 * <pre>
 *      {@code
 *
 *         <typeMappings>
 *              <typeMapping>
 *                  <name>date</name>
 *                  <type>reference</type>
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
 *                  "$ref": "#/definitions/date"
 *              },
 *              "definitions": {
 *                  "date": {
 *                      "type": "string",
 *                      "pattern": "^[1|2][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]$"
 *                  }
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
public class ReferenceCustomReturnTypePlugin implements TypeModifyingPlugin {

    private final CustomReturnTypeMapper customReturnTypeMapper;
    private final Predicate<TypeMapping> typeMappingPredicate;

    public ReferenceCustomReturnTypePlugin(final CustomReturnTypeMapper customReturnTypeMapper,
                                           final Predicate<TypeMapping> typeMappingPredicate) {
        this.customReturnTypeMapper = customReturnTypeMapper;
        this.typeMappingPredicate = typeMappingPredicate;
    }

    @FactoryMethod
    public static ReferenceCustomReturnTypePlugin customReturnTypePlugin() {

        final FullyQualifiedNameToClassNameConverter fullyQualifiedNameToClassNameConverter =
                new FullyQualifiedNameToClassNameConverter();
        final CustomReturnTypeMapper customReturnTypeMapper =
                new CustomReturnTypeMapper(fullyQualifiedNameToClassNameConverter);

        return new ReferenceCustomReturnTypePlugin(
                customReturnTypeMapper,
                REFERENCE_TYPE);
    }

    /**
     * Modifies the class name to that specified by the schema reference type mapping in generator
     * properties.
     *
     * @param typeName      The type name to be modified
     * @param definition    The FieldDefinition of the type to be modified
     * @param pluginContext The {@link PluginContext}
     * @return the {@link TypeName}
     */
    @Override
    public TypeName modifyTypeName(
            final TypeName typeName,
            final Definition definition,
            final PluginContext pluginContext) {

        if (REFERENCE.equals(definition.type())) {
            final ReferenceDefinition referenceDefinition = (ReferenceDefinition) definition;
            final ReferenceValue referenceValue = referenceDefinition.getReferenceValue();
            final Optional<ClassName> className = customReturnTypeMapper
                    .customTypeFor(typeMappingPredicate, referenceValue.getName(), pluginContext);

            if (className.isPresent()) {
                return className.get();
            }
        }

        return typeName;
    }
}
