package uk.gov.justice.generation.pojo.plugin.typemodifying;

import com.squareup.javapoet.ClassName;

public class ReferenceToClassNameConverter {

    public ClassName get(final String referenceValue) {

            final String name = referenceValue.substring(referenceValue.lastIndexOf('/') + 1);

            final int lastDot = name.lastIndexOf('.');

            final String packageName = name.substring(0, lastDot);
            final String simpleName = name.substring(lastDot + 1);

            return ClassName.get(packageName, simpleName);
    }
}
