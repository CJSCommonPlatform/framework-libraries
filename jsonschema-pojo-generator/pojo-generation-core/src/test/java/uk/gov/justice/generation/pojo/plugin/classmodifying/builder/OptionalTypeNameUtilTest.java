package uk.gov.justice.generation.pojo.plugin.classmodifying.builder;

import static com.squareup.javapoet.TypeName.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OptionalTypeNameUtilTest {

    @InjectMocks
    private OptionalTypeNameUtil optionalTypeNameUtil;

    @Test
    public void shouldReturnTrueForOptionalParameterizedType() {

        final ParameterizedTypeName optionalWithStringTypeName = ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.get(String.class));

        final boolean result = optionalTypeNameUtil.isOptionalType(optionalWithStringTypeName);

        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseForNonParameterizedType() {

        final TypeName typeName = get(String.class);

        final boolean result = optionalTypeNameUtil.isOptionalType(typeName);

        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnFalseForNonOptionalParameterizedType() {

        final ParameterizedTypeName optionalWithStringTypeName = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(String.class));

        final boolean result = optionalTypeNameUtil.isOptionalType(optionalWithStringTypeName);

        assertThat(result, is(false));
    }

    @Test
    public void shouldReturnParameterTypeOfOptionalType() {

        final ParameterizedTypeName optionalWithStringTypeName = ParameterizedTypeName.get(ClassName.get(Optional.class), ClassName.get(String.class));

        final TypeName typeName = optionalTypeNameUtil.getOptionalTypeFrom(optionalWithStringTypeName);

        assertThat(typeName, is(ClassName.get(String.class)));
    }
}