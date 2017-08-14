package uk.gov.justice.maven.generator.io.files.parser.io;

import com.google.common.base.Predicate;

import static java.io.File.separatorChar;
import static org.codehaus.plexus.util.SelectorUtils.matchPath;

/**
 * matches resource path against Ant url pattern
 */
public class AntPathMatcher implements Predicate<String> {
    private final String antPattern;

    /**
     * @param antPattern - ant pattern to be used in matching
     */
    public AntPathMatcher(final String antPattern) {
        this.antPattern = antPattern;
    }

    /**
     * Matches path against the antPattern
     *
     * @param path - resource path
     * @return - returns true if the path matches the pattern provided in the constructor, false otherwise
     */
    @Override
    public boolean apply(final String path) {
        return matchPath(antPattern.replace('/', separatorChar), path);
    }
}

