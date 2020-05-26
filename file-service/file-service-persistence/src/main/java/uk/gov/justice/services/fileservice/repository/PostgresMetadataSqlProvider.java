package uk.gov.justice.services.fileservice.repository;

import javax.enterprise.inject.Alternative;

@Alternative
public class PostgresMetadataSqlProvider implements MetadataSqlProvider {

    private static final String INSERT_SQL = "INSERT INTO metadata(metadata, file_id) values (to_json(?::json), ?)";
    private static final String UPDATE_SQL = "UPDATE metadata SET metadata = to_json(?::json) WHERE file_id = ?";

    @Override
    public String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    public String getUpdateSql() {
        return UPDATE_SQL;
    }
}
