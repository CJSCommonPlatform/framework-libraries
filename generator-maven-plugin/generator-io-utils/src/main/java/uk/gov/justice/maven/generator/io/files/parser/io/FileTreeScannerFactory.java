package uk.gov.justice.maven.generator.io.files.parser.io;

/**
 * Factory for {@link FileTreeScanner}s.
 */
public class FileTreeScannerFactory {

    public FileTreeScanner create() {
        return new FileTreeScanner();
    }
}
