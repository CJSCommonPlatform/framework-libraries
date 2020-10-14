package uk.gov.justice.services.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import uk.gov.justice.services.test.util.CantBeSerializedByJackson;

import java.io.ByteArrayOutputStream;

import com.google.common.collect.Lists;
import org.junit.Test;

public class DomainTestTest {

    @Test
    public void testEmptyInputOutput() {
        assertThat(DomainTest.generatedEventAsJsonNodeList(Lists.newArrayList()), hasSize(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenNoEventMatchesEventNameInJsonFile() throws Exception  {

        String filename = "unknown-event";
        DomainTest.eventsFromFileNames(filename);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenObjectWontSerializeToJson() throws Exception  {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.err.println(DomainTest.generatedEventAsJsonNode(new CantBeSerializedByJackson("one", "two", "three")));
    }

}
