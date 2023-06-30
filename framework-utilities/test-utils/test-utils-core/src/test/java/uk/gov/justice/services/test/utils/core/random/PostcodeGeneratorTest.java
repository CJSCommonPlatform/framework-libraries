package uk.gov.justice.services.test.utils.core.random;

import static com.btmatthews.hamcrest.regex.PatternMatcher.matches;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

public class PostcodeGeneratorTest {

    @Test
    public void shouldGenerateValidPostcodes() {
        final PostcodeGenerator postcodeGenerator = new PostcodeGenerator();

        for (int i = 0; i < 10000; i++) {
            final String postcode = postcodeGenerator.next();
            assertThat(postcode, containsString(" "));
            assertThat(postcode.length(), is(greaterThanOrEqualTo(4)));
            assertThat(postcode.length(), is(lessThanOrEqualTo(8)));

            final String errorMessage = format("generated postcode %s is invalid", postcode);

            final String tokens[] = postcode.split(" ");
            final String outwardCode = tokens[0];

            assertThat(errorMessage, outwardCode, matches("[A-Z0-9]+"));

            assertThat(errorMessage, outwardCode.substring(0, 1), matches("(?=[A-Z])([^QVX])"));
            if (outwardCode.length() >= 2 && isAlpha(outwardCode.substring(1, 2))) {
                assertThat(errorMessage, outwardCode.substring(1, 2), matches("(?=[A-Z])([^IJZ])"));
            }
            if (outwardCode.length() >= 3 && isAlpha(outwardCode.substring(2, 3))) {
                assertThat(errorMessage, outwardCode.substring(2, 3), matches("[ABCDEFGHJKPSTUW]"));
            }

            final String inwardCode = tokens[1];
            assertThat(errorMessage, inwardCode, matches("[0-9]((?=[A-Z])([^CIKMOV])){2}"));
        }
    }


}
