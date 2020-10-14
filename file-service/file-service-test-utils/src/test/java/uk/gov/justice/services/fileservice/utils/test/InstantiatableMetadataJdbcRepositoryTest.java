package uk.gov.justice.services.fileservice.utils.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.fileservice.utils.test.DatabaseDialect.ANSI_SQL;
import static uk.gov.justice.services.fileservice.utils.test.DatabaseDialect.POSTGRES;

import uk.gov.justice.services.fileservice.repository.AnsiMetadataSqlProvider;
import uk.gov.justice.services.fileservice.repository.MetadataSqlProvider;
import uk.gov.justice.services.fileservice.repository.PostgresMetadataSqlProvider;

import java.lang.reflect.Field;

import org.junit.Test;

public class InstantiatableMetadataJdbcRepositoryTest {

    @Test
    public void shouldCreateWithPostgresSqlIfTheDatabaseDialectIsPostgres() throws Exception {

        final InstantiatableMetadataJdbcRepository metadataRepository = new InstantiatableMetadataJdbcRepository(POSTGRES);

        assertThat(getSQlProviderFrom(metadataRepository), is(instanceOf(PostgresMetadataSqlProvider.class)));
    }

    @Test
    public void shouldCreateWithAnsiSqlIfTheDatabaseDialectIsAnsiSql() throws Exception {

        final InstantiatableMetadataJdbcRepository metadataRepository = new InstantiatableMetadataJdbcRepository(ANSI_SQL);

        assertThat(getSQlProviderFrom(metadataRepository), is(instanceOf(AnsiMetadataSqlProvider.class)));
    }

    private MetadataSqlProvider getSQlProviderFrom(final  InstantiatableMetadataJdbcRepository instantiatableMetadataJdbcRepository) throws Exception {

        final Field field = instantiatableMetadataJdbcRepository.getClass().getSuperclass().getDeclaredField("metadataSqlProvider");
        field.setAccessible(true);

        return (MetadataSqlProvider) field.get(instantiatableMetadataJdbcRepository);
    }
}
