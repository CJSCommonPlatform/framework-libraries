package uk.gov.justice.services.fileservice.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.fileservice.repository.AnsiMetadataSqlProvider.INSERT_SQL;
import static uk.gov.justice.services.fileservice.repository.AnsiMetadataSqlProvider.UPDATE_SQL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnsiMetadataSqlProviderTest {

    @InjectMocks
    private AnsiMetadataSqlProvider ansiMetadataSqlProvider;

    @Test
    public void shouldReturnTheCorrectAnsiSqlForInsert() throws Exception {
        assertThat(ansiMetadataSqlProvider.getInsertSql(), is(INSERT_SQL));
    }

    @Test
    public void shouldReturnTheCorrectAnsiSqlForUpdate() throws Exception {
        assertThat(ansiMetadataSqlProvider.getUpdateSql(), is(UPDATE_SQL));
    }
}
