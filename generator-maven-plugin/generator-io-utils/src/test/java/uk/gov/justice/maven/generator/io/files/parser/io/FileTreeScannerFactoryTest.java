package uk.gov.justice.maven.generator.io.files.parser.io;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link FileTreeScannerFactory} class.
 */
public class FileTreeScannerFactoryTest {

    private FileTreeScannerFactory factory;

    @BeforeEach
    public void setup() {
        factory = new FileTreeScannerFactory();
    }

    @Test
    public void shouldCreateFileTreeScanner() {
        FileTreeScanner scanner = factory.create();
        assertThat(scanner, isA(FileTreeScanner.class));
    }
}
