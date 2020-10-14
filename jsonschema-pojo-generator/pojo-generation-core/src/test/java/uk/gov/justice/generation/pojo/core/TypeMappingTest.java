package uk.gov.justice.generation.pojo.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.generation.utils.TypeMappingFactory.typeMappingOf;

import org.junit.Test;

public class TypeMappingTest {

    @Test
    public void shouldReturnTypeMappingInformation() throws Exception {
        final String type = "type";
        final String name = "name";
        final String implementation = "ClassName";

        final TypeMapping typeMapping = typeMappingOf(type, name, implementation);

        assertThat(typeMapping.getType(), is(type));
        assertThat(typeMapping.getName(), is(name));
        assertThat(typeMapping.getImplementation(), is(implementation));
    }
}
