package uk.gov.moj.cpp.task.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RandomPercentageProviderTest {

    @InjectMocks
    private RandomPercentageProvider randomPercentageProvider;

    @Test
    public void shouldGetPseudoRandomIntegerFromZeroInclusiveToOneHundredExclusive() throws Exception {

        for (int i = 0; i < 10_000; i++) {
            final int randomPercentage = randomPercentageProvider.getRandomPercentage();
            assertThat(randomPercentage, is(lessThan(100)));
            assertThat(randomPercentage, is(greaterThanOrEqualTo(0)));
        }
    }
}