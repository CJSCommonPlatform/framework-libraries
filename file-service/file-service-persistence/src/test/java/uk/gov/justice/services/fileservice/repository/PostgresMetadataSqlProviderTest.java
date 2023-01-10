package uk.gov.justice.services.fileservice.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostgresMetadataSqlProviderTest {

    @InjectMocks
    private PostgresMetadataSqlProvider postgresMetadataSqlProvider;

    @Test
    public void shouldGetThePostgresSpecificInsertSqlForInsertingMetadataIntoTheMetadataTable() throws Exception {

        assertThat(postgresMetadataSqlProvider.getInsertSql(), is("INSERT INTO metadata(metadata, file_id) values (to_json(?::json), ?)"));
    }

    @Test
    public void shouldGetThePostgresSpecificUpdateSqlForUpdatingMetadataInTheMetadataTable() throws Exception {

        assertThat(postgresMetadataSqlProvider.getUpdateSql(), is("UPDATE metadata SET metadata = to_json(?::json) WHERE file_id = ?"));
    }
}