package uk.gov.justice.services.test.utils.core.random;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.Times.times;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.typeCheck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LocalPartGeneratorTest {

    @Mock
    private LocalPartValidator validator;

//    @BeforeEach
//    public void setUp() {
//        when(validator.validate(anyString())).thenReturn(true);
//    }

    @Test
    public void shouldGenerateLocalParts() {
        final LocalPartGenerator generator = new LocalPartGenerator(new LocalPartValidator());

        typeCheck(generator, s -> {
            final String withoutComments = s.replace("(comment)", "");
            return withoutComments.matches("[0-9a-zA-Z!#$%&'*+-/=?^_`.{|}~ \"(),:;<>@\\[\\]\\\\]{1,64}");
        }).verify(times(10000));
    }

    @Test
    public void shouldRetryToGenerateTheDomainPartIfValidationFails() {
        when(validator.validate(anyString())).thenReturn(false).thenReturn(true);
        final LocalPartGenerator generator = new LocalPartGenerator(validator);

        generator.next();

        verify(validator, Mockito.times(2)).validate(anyString());
    }

}


