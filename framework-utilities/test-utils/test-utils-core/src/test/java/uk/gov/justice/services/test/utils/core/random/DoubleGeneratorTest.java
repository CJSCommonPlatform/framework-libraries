package uk.gov.justice.services.test.utils.core.random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.Times.times;
import static uk.gov.justice.services.test.utils.core.helper.TypeCheck.typeCheck;

import java.text.DecimalFormat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DoubleGeneratorTest {

    private static final int NUMBER_OF_TIMES = 100000;

    @Test
    public void shouldGenerateDoubleGreaterThanOrEqualToMinAndLessThanOrEqualToMax() {
        // given
        final Long min = -100L;
        final Long max = 100L;

        // when
        final Generator<Double> doubleGenerator = new DoubleGenerator(min, max, 0);

        // then
        typeCheck(doubleGenerator, d -> d >= min && d <= max).verify(times(NUMBER_OF_TIMES));
    }

    @Test
    public void shouldGenerateDoubleWithOptionalScale() {
        // given
        final Long min = -100L;
        final Long max = 100L;
        final Integer maxScale = 2;

        // when
        final Generator<Double> doubleGenerator = new DoubleGenerator(min, max, maxScale);

        // when
        typeCheck(doubleGenerator, d -> Double.toString(d).matches("(-)?(0|(?!0)\\d{1,3})(\\.\\d{1,2})?")).verify(times(NUMBER_OF_TIMES));
    }

    @Test
    public void shouldThrowExceptionIfScaleIsNegative() throws Exception {
        // given
        final Long min = -100L;
        final Long max = 100L;
        final Integer scale = -2;

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                new DoubleGenerator(min, max, scale).next()
        );

        assertThat(illegalArgumentException.getMessage(), is("Scale cannot be less than zero, got -2"));
    }

    @Test
    public void shouldThrowExceptionIfMinIsGreaterThanMax() throws Exception {

        final Long min = 101L;
        final Long max = 100L;
        final Integer scale = 2;

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                new DoubleGenerator(min, max, scale).next()
        );

        assertThat(illegalArgumentException.getMessage(), is("Min value cannot be greater than or equal to Max value, got Min: 101.0 and Max: 100.0"));
    }

    @Test
    public void shouldGenerateDoubleWhenRangeLeadsToInfinite() throws Exception {
        // given
        final Double min = -Double.MAX_VALUE;
        final Double max = Double.MAX_VALUE;
        final Integer scale = 2;
        final int noOfTimes = 1000;

        // when
        final Generator<Double> doubleGenerator = new DoubleGenerator(min, max, scale);
//        overrideUnderlyingRandomGenerator(doubleGenerator, random);

        // then
        typeCheck(doubleGenerator, s -> new DecimalFormat("#.###").format(s).matches("(-)?\\d+(\\.\\d{1,2})?")).verify(times(noOfTimes));
    }
}