package uk.gov.justice.generation.pojo.plugin.classmodifying;

import static com.squareup.javapoet.TypeName.LONG;
import static java.lang.String.format;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.plugin.IncompatiblePluginException;
import uk.gov.justice.generation.pojo.plugin.PluginContext;
import uk.gov.justice.generation.pojo.plugin.typemodifying.SupportJavaOptionalsPlugin;

import java.io.Serializable;
import java.util.List;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * A class plugin that adds {@link Serializable} interface and {@code serialVersionUID} to the
 * generated class.
 *
 * Note: the serialVersionUID is always set to 1.
 *
 * <p>For example:</p>
 * <pre>
 *     {@code
 *
 *     public class MyClass implements Serializable {
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
 * </pre>
 */
public class MakeClassSerializablePlugin implements ClassModifyingPlugin {

    private static final String SERIAL_VERSION_FIELD_NAME = "serialVersionUID";
    private static final String SERIAL_VERSION_VALUE = "1L";

    @Override
    public TypeSpec.Builder generateWith(final TypeSpec.Builder classBuilder,
                                         final ClassDefinition classDefinition,
                                         final PluginContext pluginContext) {

        classBuilder.addSuperinterface(Serializable.class)
                .addField(FieldSpec
                        .builder(LONG, SERIAL_VERSION_FIELD_NAME, PRIVATE, STATIC, FINAL)
                        .initializer(SERIAL_VERSION_VALUE)
                        .build());

        return classBuilder;
    }

    @Override
    public void checkCompatibilityWith(final List<String> pluginNames) {

        final String incompatiblePluginClassName = SupportJavaOptionalsPlugin.class.getName();

        if (pluginNames.contains(incompatiblePluginClassName)) {
            throw new IncompatiblePluginException(format(
                    "Plugin '%s' is incompatible with plugin '%s' and should not be run together",
                    getClass().getName(),
                    incompatiblePluginClassName));
        }
    }
}
