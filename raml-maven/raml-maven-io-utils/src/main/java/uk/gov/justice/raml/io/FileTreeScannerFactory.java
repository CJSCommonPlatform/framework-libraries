package uk.gov.justice.raml.io;

/**
 * Factory for {@link FileTreeScanner}s.
 */
public class FileTreeScannerFactory {

    public FileTreeScanner create() {
        return new FileTreeScanner();
    }
}
