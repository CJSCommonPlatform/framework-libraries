package uk.gov.justice.generation.pojo.generators.plugin.classmodifying;

import uk.gov.justice.generation.pojo.dom.ClassDefinition;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.builder.BuilderGenerator;
import uk.gov.justice.generation.pojo.generators.plugin.classmodifying.builder.BuilderGeneratorFactory;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Adds a builder for the class as an static inner class and a static
 * method for accessing the builder. For example, a class <i>MyClass</i> specified
 * with one property <i>myProperty</i> would be generated thusly:
 *
 * <pre>
 *     {@code public class MyClass {
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
 *
 *          public static Builder myClass() {
 *              return new MyClass.Builder();
 *          }
 *
 *          public static class Builder {
 *
 *              private String myProperty;
 *
 *              public Builder withMyProperty(final String myProperty) {
 *                  this.myProperty = myProperty;
 *                  return this;
 *              }
 *
 *              public MyClass build() {
 *                  return new MyClass(myProperty);
 *              }
 *          }
 *      }
 * }</pre>
 */
public class GenerateBuilderForClassPlugin implements ClassModifyingPlugin {

    private final BuilderGeneratorFactory builderGeneratorFactory;

    public GenerateBuilderForClassPlugin(final BuilderGeneratorFactory builderGeneratorFactory) {
        this.builderGeneratorFactory = builderGeneratorFactory;
    }

    @Override
    public TypeSpec.Builder generateWith(
            final TypeSpec.Builder outerClassBuilder,
            final ClassDefinition classDefinition,
            final PluginContext pluginContext) {

        final BuilderGenerator builderGenerator = builderGeneratorFactory.create(
                classDefinition,
                pluginContext.getClassNameFactory());

        final TypeSpec innerClassBuilder = builderGenerator.generate();
        final MethodSpec staticGetBuilderMethod = builderGenerator.generateStaticGetBuilderMethod();

        outerClassBuilder.addType(innerClassBuilder);
        outerClassBuilder.addMethod(staticGetBuilderMethod);

        return outerClassBuilder;
    }
}
