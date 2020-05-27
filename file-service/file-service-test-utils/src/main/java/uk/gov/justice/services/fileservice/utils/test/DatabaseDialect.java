package uk.gov.justice.services.fileservice.utils.test;

/**
 * The database dialect that will be used by the file service.
 *
 * If you are pointing at a postgres instance in your tests then use POSTGRES, otherwise
 * use ANSI_SQL (for H2 etc.)
 */
public enum DatabaseDialect {
    POSTGRES,
    ANSI_SQL
}
