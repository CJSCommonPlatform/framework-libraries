package uk.gov.justice.raml.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.base.Predicate;
import org.junit.Test;

public class AntPathMatcherTest {
    private Predicate<String> antPathMatcher;

    @Test
    public void shouldMatchFilesWithExtension() throws Exception {
        antPathMatcher = new AntPathMatcher("**/*.raml");
        assertThat(antPathMatcher.apply("abc.raml"), is(true));
        assertThat(antPathMatcher.apply("aaa-bcd.raml"), is(true));
        assertThat(antPathMatcher.apply("aaa-bcd.aml"), is(false));
    }

    @Test
    public void shouldMatchFilesEndingWithSpecificEnding() throws Exception {
        antPathMatcher = new AntPathMatcher("**/*abc.raml");
        assertThat(antPathMatcher.apply("abc.raml"), is(true));
        assertThat(antPathMatcher.apply("aaa-aabc.raml"), is(true));
        assertThat(antPathMatcher.apply("aaa-bcd.aml"), is(false));
    }
}
