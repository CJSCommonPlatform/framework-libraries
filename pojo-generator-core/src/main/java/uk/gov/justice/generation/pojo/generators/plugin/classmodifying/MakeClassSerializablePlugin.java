package uk.gov.justice.generation.pojo.generators.plugin.classmodifying;

import static com.squareup.javapoet.TypeName.LONG;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;

import java.io.Serializable;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * A class plugin that adds {@see Serializable} interface and
 * {@code serialVersionUID} to the generated class.
 *
 * Note: the serialVersionUID is always set to 1.
 *
 * For example:
 *
 * <pre>
 *     {@code public class MyClass implements Serializable {
 *
 *          private static final long serialVersionUID = 1L;
 *
 *          private final String myProperty;
 *
 *          public MyClass(final String myProperty) {
 *              this.myProperty = myProperty;
 *          }
 *
 *          public String getMyProperty() {
 *              return myProperty;
 *          }
 *      }
 *     }
 *
 * </pre>
 */
public class MakeClassSerializablePlugin implements ClassModifyingPlugin {

    private static final String SERIAL_VERSION_FIELD_NAME = "serialVersionUID";
    private static final String SERIAL_VERSION_VALUE = "1L";

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder typeSpecBuilder,
                                         final ClassDefinition classDefinition,
                                         final PluginContext pluginContext) {

        typeSpecBuilder.addSuperinterface(Serializable.class)
                .addField(FieldSpec
                        .builder(LONG, SERIAL_VERSION_FIELD_NAME, PRIVATE, STATIC, FINAL)
                        .initializer(SERIAL_VERSION_VALUE)
                        .build());

        return typeSpecBuilder;
    }
}
