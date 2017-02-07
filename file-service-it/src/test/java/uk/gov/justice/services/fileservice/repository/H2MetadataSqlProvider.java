package uk.gov.justice.services.fileservice.repository;

public class H2MetadataSqlProvider implements MetadataSqlProvider {

    private static final String INSERT_SQL = "INSERT INTO metadata(metadata, file_id) values (?, ?)";
    private static final String UPDATE_SQL = "UPDATE metadata SET metadata = ? WHERE file_id = ?";

    @Override
    public String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    public String getUpdateSql() {
        return UPDATE_SQL;
    }
}
