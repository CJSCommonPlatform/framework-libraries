package uk.gov.justice.generation.pojo.write;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

public class JavaPoetJavaFileCreatorTest {

    private final JavaPoetJavaFileCreator javaPoetJavaFileCreator = new JavaPoetJavaFileCreator();

    @Test
    public void shouldCreateJavaFile() {

        final String packageName = "org.fred.something.or.other";
        final TypeSpec typeSpec = simpleClassTypeSpec();
        final JavaFile javaFile = javaPoetJavaFileCreator.createJavaFile(typeSpec, packageName);

        assertThat(javaFile, is(notNullValue()));

        assertThat(javaFile.typeSpec, is(typeSpec));
        assertThat(javaFile.packageName, is(packageName));
    }

    private TypeSpec simpleClassTypeSpec() {
        final MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        return TypeSpec.classBuilder("Address")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
    }
}