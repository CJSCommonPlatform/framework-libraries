package uk.gov.justice.services.fileservice.repository;

/**
 * Gets the insert and update sql statements for the {@link MetadataJdbcRepository}. This
 * class allows the sql to be overridden from the default postgres specific sql (used for inserting
 * json as a JsonB type) to a simple string insert/update for the in memory H2 database used in
 * tests
 */
public interface MetadataSqlProvider {

    /**
     * get the insert sql
     * @return the insert sql for inserting metadata into the metadata table
     */
    String getInsertSql();

    /**
     * get the update sql
     * @return the update sql for inserting metadata into the metadata table
     */
    String getUpdateSql();
}
