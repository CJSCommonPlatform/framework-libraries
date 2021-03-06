package uk.gov.justice.raml.io;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

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
