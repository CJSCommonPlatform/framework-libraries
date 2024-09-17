package uk.gov.justice.services.fileservice.client;

import static java.lang.Integer.parseInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.test.utils.core.reflection.ReflectionUtil.setField;

import org.junit.jupiter.api.Test;

public class FileStorePurgeConfigurationTest {

    private FileStorePurgeConfiguration fileStorePurgeConfiguration = new FileStorePurgeConfiguration();

    @Test
    public void shouldGetTheJndiValueForTheNumberOfDaysOlderThanPurge() throws Exception {

        final int purgeFilesOlderThanNumberOfDays = 23;

        setField(fileStorePurgeConfiguration, "purgeFilesOlderThanNumberOfDays" , "" + purgeFilesOlderThanNumberOfDays);

        assertThat(fileStorePurgeConfiguration.getPurgeFilesOlderThanNumberOfDays(), is(purgeFilesOlderThanNumberOfDays));
    }

    @Test
    public void shouldGetTheJndiValueForMaximumNumberOfFilesToPurge() throws Exception {

        final String maximumNumberOfFilesToPurge = "100000";

        setField(fileStorePurgeConfiguration, "maximumNumberOfFilesToPurge" , maximumNumberOfFilesToPurge);

        assertThat(fileStorePurgeConfiguration.getMaximumNumberOfFilesToPurge(), is(parseInt(maximumNumberOfFilesToPurge)));
    }
}