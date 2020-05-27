package uk.gov.justice.services.test.domain;

import static org.junit.Assert.assertThat;
import static uk.gov.justice.services.test.DomainTest.jsonNodesListFrom;
import static uk.gov.justice.services.test.DomainTest.toJsonNodes;
import static uk.gov.justice.services.test.domain.AggregateWrapper.aggregateWrapper;
import static uk.gov.justice.services.test.matchers.HasEventsMatcher.hasEvents;
import static uk.gov.justice.services.test.matchers.NoEventsMatcher.hasNoEvents;

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

        final List<JsonNode> actual = toJsonNodes(aggregateWrapper.generatedEvents());
        final List<JsonNode> expected = jsonNodesListFrom(fileNames);

        assertThat(actual, hasEvents(expected));

        expected.forEach(jsonNode -> aggregateWrapper.generatedEvents().remove(0));
    }

    @Then("^no events occurred$")
    public void assert_no_events_generated() {
        assertThat(toJsonNodes(aggregateWrapper.generatedEvents()), hasNoEvents());
    }
}
