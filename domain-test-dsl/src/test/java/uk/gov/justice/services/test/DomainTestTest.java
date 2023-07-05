package uk.gov.justice.services.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import uk.gov.justice.services.test.util.CantBeSerializedByJackson;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

public class DomainTestTest {

    @Test
    public void testEmptyInputOutput() {
        assertThat(DomainTest.generatedEventAsJsonNodeList(Lists.newArrayList()), hasSize(0));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenNoEventMatchesEventNameInJsonFile() throws Exception  {
        assertThrows(IllegalArgumentException.class, () -> DomainTest.eventsFromFileNames("unknown-event"));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenObjectWontSerializeToJson() throws Exception  {
        assertThrows(IllegalArgumentException.class, () -> DomainTest.generatedEventAsJsonNode(new CantBeSerializedByJackson("one", "two", "three")));
    }

}
