package uk.gov.moj.cpp.jobstore.persistence;

/**
 * Gets the insert and update sql statements for the {@link JobSqlProvider}. This class allows
 * the sql to be overridden from the default postgres specific sql (used for inserting json as a
 * JsonB type) for job data to a simple string insert/update for the in memory H2 database used in tests
 */
public interface JobSqlProvider {

    /**
     * get the insert sql
     *
     * @return the insert sql for inserting job_data into the job table
     */
    String getInsertSql();

    /**
     * get the update sql
     *
     * @return the update job data sql for updating job_data into the job table
     */
    String getUpdateJobDataSql();
}
