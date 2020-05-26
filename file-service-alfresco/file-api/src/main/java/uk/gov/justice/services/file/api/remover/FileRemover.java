package uk.gov.justice.services.file.api.remover;

/**
 * Interface for removing a file from a file service.
 */
public interface FileRemover {

    void remove(final String fileId);
}
