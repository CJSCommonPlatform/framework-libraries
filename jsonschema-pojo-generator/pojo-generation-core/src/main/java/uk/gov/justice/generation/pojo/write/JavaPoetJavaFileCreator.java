package uk.gov.justice.generation.pojo.write;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class JavaPoetJavaFileCreator {

    public JavaFile createJavaFile(final TypeSpec typeSpec, final String packageName) {
        return JavaFile.builder(packageName, typeSpec).build();
    }
}
