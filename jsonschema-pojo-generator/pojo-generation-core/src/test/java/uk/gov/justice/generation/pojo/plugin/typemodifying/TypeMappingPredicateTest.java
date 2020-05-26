package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static net.trajano.commons.testing.UtilityClassTestUtil.assertUtilityClassWellDefined;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.TypeMappingPredicate.FORMAT_TYPE;
import static uk.gov.justice.generation.pojo.plugin.typemodifying.TypeMappingPredicate.REFERENCE_TYPE;
import static uk.gov.justice.generation.utils.TypeMappingFactory.typeMappingOf;

import uk.gov.justice.generation.pojo.core.TypeMapping;

import java.util.List;

import org.junit.Test;

public class TypeMappingPredicateTest {

    @Test
    public void shouldBeWellDefinedUtilityClass() {
        assertUtilityClassWellDefined(TypeMappingPredicate.class);
    }

    @Test
    public void shouldFilterByReferenceType() throws Exception {
        final TypeMapping typeMapping_1 = typeMappingOf("reference", "name_1", "impl_1");
        final TypeMapping typeMapping_2 = typeMappingOf("reference", "name_2", "impl_2");

        final List<TypeMapping> typeMappings = asList(
                typeMapping_1,
                typeMappingOf("format", "name_3", "impl_3"),
                typeMapping_2
        );

        final List<TypeMapping> referenceMappings = typeMappings.stream()
                .filter(REFERENCE_TYPE)
                .collect(toList());

        assertThat(referenceMappings.size(), is(2));
        assertThat(referenceMappings, hasItems(typeMapping_1, typeMapping_2));
    }

    @Test
    public void shouldFilterByFormatType() throws Exception {
        final TypeMapping typeMapping_1 = typeMappingOf("format", "name_1", "impl_1");
        final TypeMapping typeMapping_2 = typeMappingOf("format", "name_2", "impl_2");

        final List<TypeMapping> typeMappings = asList(
                typeMapping_1,
                typeMappingOf("reference", "name_3", "impl_3"),
                typeMapping_2
        );

        final List<TypeMapping> referenceMappings = typeMappings.stream()
                .filter(FORMAT_TYPE)
                .collect(toList());

        assertThat(referenceMappings.size(), is(2));
        assertThat(referenceMappings, hasItems(typeMapping_1, typeMapping_2));
    }
}
