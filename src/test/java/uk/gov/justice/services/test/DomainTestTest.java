package uk.gov.justice.services.test;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.google.common.collect.Lists;
import org.junit.Test;

public class DomainTestTest {

    @Test
    public void testEmptyInputOutput() {
        assertThat(DomainTest.generatedEventAsJsonNodeList(Lists.newArrayList()), hasSize(0));
    }

}
