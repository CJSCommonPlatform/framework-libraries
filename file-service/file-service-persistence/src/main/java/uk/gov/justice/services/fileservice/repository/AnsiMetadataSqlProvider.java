package uk.gov.justice.services.fileservice.repository;

import javax.enterprise.inject.Alternative;

/**
 * Gets the sql for insert/updates when not using postgres JsonB
 */
@Alternative
public class AnsiMetadataSqlProvider implements MetadataSqlProvider {

    static final String INSERT_SQL = "INSERT INTO metadata(metadata, file_id) values (?, ?)";
    static final String UPDATE_SQL = "UPDATE metadata SET metadata = ? WHERE file_id = ?";

    @Override
    public String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    public String getUpdateSql() {
        return UPDATE_SQL;
    }
}