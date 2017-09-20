package uk.gov.justice.generation.pojo.plugin.typemodifying;

import static com.squareup.javapoet.ClassName.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceToClassNameConverterTest {

    @InjectMocks
    private ReferenceToClassNameConverter referenceToClassNameConverter;

    @Test
    public void shouldConvertAReferenceValueFromJsonSchemaIntoAClassName() throws Exception {

        assertThat(referenceToClassNameConverter.get("#/definitions/java.math.BigInteger"), is(equalTo(get(BigInteger.class))));
        assertThat(referenceToClassNameConverter.get("#/definitions/java.time.ZonedDateTime"), is(equalTo(get(ZonedDateTime.class))));
        assertThat(referenceToClassNameConverter.get("#/definitions/java.util.UUID"), is(equalTo(get(UUID.class))));
    }
}
