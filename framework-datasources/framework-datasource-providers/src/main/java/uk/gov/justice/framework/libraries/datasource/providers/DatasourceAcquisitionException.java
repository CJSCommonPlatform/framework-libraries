package uk.gov.justice.framework.libraries.datasource.providers;

public class DatasourceAcquisitionException extends RuntimeException {

    public DatasourceAcquisitionException(final String message) {
        super(message);
    }

    public DatasourceAcquisitionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
