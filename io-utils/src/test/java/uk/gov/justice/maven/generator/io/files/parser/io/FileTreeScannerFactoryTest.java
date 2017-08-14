package uk.gov.justice.maven.generator.io.files.parser.io;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for the {@link FileTreeScannerFactory} class.
 */
public class FileTreeScannerFactoryTest {

    private FileTreeScannerFactory factory;

    @Before
    public void setup() {
        factory = new FileTreeScannerFactory();
    }

    @Test
    public void shouldCreateFileTreeScanner() {
        FileTreeScanner scanner = factory.create();
        assertThat(scanner, isA(FileTreeScanner.class));
    }
}
