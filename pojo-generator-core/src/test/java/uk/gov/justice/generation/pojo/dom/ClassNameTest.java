package uk.gov.justice.generation.pojo.dom;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ClassNameTest {

    @Test
    public void shouldCreateAClassNameFromPackageNameAndSimpleName() throws Exception {

        final String packageName = "org.bananas.cool";
        final String simpleName = "TimeMachine";

        final ClassName className = new ClassName(packageName, simpleName);

        assertThat(className.getPackageName(), is(packageName));
        assertThat(className.getSimpleName(), is(simpleName));
        assertThat(className.getFullyQualifiedName(), is("org.bananas.cool.TimeMachine"));
    }

    @Test
    public void shouldCreateAClassNameFromAClass() throws Exception {

        final ClassName stringClassName = new ClassName(String.class);
        final ClassName integerClassName = new ClassName(Integer.class);

        assertThat(stringClassName.getPackageName(), is("java.lang"));
        assertThat(stringClassName.getSimpleName(), is("String"));
        assertThat(stringClassName.getFullyQualifiedName(), is("java.lang.String"));

        assertThat(integerClassName.getPackageName(), is("java.lang"));
        assertThat(integerClassName.getSimpleName(), is("Integer"));
        assertThat(integerClassName.getFullyQualifiedName(), is("java.lang.Integer"));
    }
}
