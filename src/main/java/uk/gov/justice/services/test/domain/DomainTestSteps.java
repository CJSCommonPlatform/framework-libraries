package uk.gov.justice.services.test.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.DomainTest.eventNameFrom;
import static uk.gov.justice.services.test.DomainTest.generatedEventAsJsonNode;
import static uk.gov.justice.services.test.DomainTest.jsonNodeWithoutMetadataFrom;
import static uk.gov.justice.services.test.DomainTest.jsonNodesListFrom;
import static uk.gov.justice.services.test.domain.AggregateWrapper.aggregateWrapper;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DomainTestSteps {

    private AggregateWrapper aggregateWrapper;

    @Given("^no previous events$")
    public void no_previous_events() {
        aggregateWrapper = aggregateWrapper();
    }

    @Given("^((?:(?!I\\s|you\\s|no\\s).*)(?:using\\s|with\\s)?.*)$")
    public void previous_events(final String fileNames) {
        if (aggregateWrapper != null && aggregateWrapper.initialised()) {
            assert_events_generated(fileNames);
        } else {
            aggregateWrapper = (aggregateWrapper == null ? aggregateWrapper() : aggregateWrapper).withInitialEventsFromFiles(fileNames);
        }
    }

    @When("^(?:I|you) (.*) (?:to|from|on|in) (?:a|an) (.*) (?:using|with) (?:a|an) (.*)$")
    public void call_method_with_params(final String methodName, final String aggregateName, final String fileName)
            throws Exception {
        aggregateWrapper.initialiseFromClass(aggregateName).invokeMethod(methodName.trim(), fileName.trim());
    }

    @When("^(?:I|you) (.*) (?:to|from|on|in) (?:a|an) ((?:(?!using|with).)*)$")
    public void call_method_with_no_params(final String methodName, final String aggregateName)
            throws Exception {
        aggregateWrapper.initialiseFromClass(aggregateName).invokeMethod(methodName.trim());
    }

    private void assert_events_generated(final String fileNames) {
        final List<JsonNode> jsonNodeList = jsonNodesListFrom(fileNames);
        int index = 0;
        for (JsonNode jsonNode : jsonNodeList) {
            Object generatedEvent = aggregateWrapper.generatedEvents().get(index);
            assertThat(eventNameFrom(generatedEvent), equalToIgnoringCase(eventNameFrom(jsonNode)));
            assertThat(generatedEventAsJsonNode(generatedEvent), equalTo(jsonNodeWithoutMetadataFrom(jsonNode)));
            index++;
        }
    }

    @Then("^no events occured$")
    public void assert_no_events_generated() {
        assertThat(aggregateWrapper.generatedEvents(), hasSize(0));
    }

}