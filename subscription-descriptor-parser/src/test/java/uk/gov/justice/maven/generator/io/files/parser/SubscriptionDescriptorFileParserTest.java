package uk.gov.justice.maven.generator.io.files.parser;

import static java.nio.file.Paths.get;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import uk.gov.justice.domain.subscriptiondescriptor.Event;
import uk.gov.justice.domain.subscriptiondescriptor.Eventsource;
import uk.gov.justice.domain.subscriptiondescriptor.Location;
import uk.gov.justice.domain.subscriptiondescriptor.Subscription;
import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptor;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.everit.json.schema.ValidationException;
import org.junit.Test;

public class SubscriptionDescriptorFileParserTest {

    final Path baseDir = get("src/test/resources/");

    @Test
    public void shouldThrowFileNotFoundException() {
        final SubscriptionDescriptorFileParser subscriptionDescriptorFileParser = new SubscriptionDescriptorFileParserFactory().create();
        final List<Path> paths = singletonList(get("no-subscription.yaml"));
        try {
            subscriptionDescriptorFileParser.parse(baseDir, paths);
            fail();
        } catch (SubscriptionDescriptorException re) {
            assertThat(re, is(instanceOf(SubscriptionDescriptorException.class)));
            assertThat(re.getCause(), is(instanceOf(NoSuchFileException.class)));
        }
    }

    @Test
    public void shouldFailOnIncorrectSubscriptionYaml() {
        final SubscriptionDescriptorFileParser subscriptionDescriptorFileParser = new SubscriptionDescriptorFileParserFactory().create();

        final List<Path> paths = singletonList(get("incorrect-subscription.yaml"));
        try {
            subscriptionDescriptorFileParser.parse(baseDir, paths);
            fail();
        } catch (SubscriptionDescriptorException re) {
            assertThat(re, is(instanceOf(SubscriptionDescriptorException.class)));
            assertThat(re.getCause(), is(instanceOf(ValidationException.class)));
            assertThat(re.getCause().getMessage(), is("#/subscription_descriptor: required key [spec_version] not found"));
        }
    }

    @Test
    public void shouldParsePathsToYaml() {
        final SubscriptionDescriptorFileParser subscriptionDescriptorFileParser = new SubscriptionDescriptorFileParserFactory().create();

        final List<Path> paths = singletonList(get("subscription.yaml"));
        final Collection<SubscriptionDescriptor> subscriptionDescriptors = subscriptionDescriptorFileParser.parse(baseDir, paths);

        assertThat(subscriptionDescriptors, hasSize(1));
        for (final SubscriptionDescriptor subscriptionDescriptor : subscriptionDescriptors) {
            assertSubscriptionDescriptor(subscriptionDescriptor);
            final List<Subscription> subscriptions = subscriptionDescriptor.getSubscriptions();
            for (final Subscription subscription : subscriptions) {
                if (subscription.getName().equalsIgnoreCase("subscription1")) {
                    assertExampleEvents(subscription);
                    assertExampleEventSource(subscription.getEventsource());
                }
                if (subscription.getName().equalsIgnoreCase("subscription2")) {
                    assertPeopleEvents(subscription);
                    assertPeopleEventSource(subscription.getEventsource());
                }
            }
        }
    }

    private void assertExampleEventSource(Eventsource eventsource) {
        final String name = eventsource.getName();
        assertThat(name, is("examplecontext"));
        final Location location = eventsource.getLocation();
        assertThat(location.getJmsUri(), is("jms:topic:example.event"));
        assertThat(location.getRestUri(), is("http://localhost:8080/example/event-source-api/rest"));
    }

    private void assertPeopleEventSource(Eventsource eventsource) {
        final String name = eventsource.getName();
        assertThat(name, is("people"));
        final Location location = eventsource.getLocation();
        assertThat(location.getJmsUri(), is("jms:topic:people.event"));
        assertThat(location.getRestUri(), is("http://localhost:8080/people/event-source-api/rest"));
    }

    private SubscriptionDescriptor assertSubscriptionDescriptor(SubscriptionDescriptor subscriptionDescriptor) {
        assertThat(subscriptionDescriptor.getService(), is("examplecontext"));
        assertThat(subscriptionDescriptor.getServiceComponent(), is("EVENT_LISTENER"));
        assertThat(subscriptionDescriptor.getSpecVersion(), is("1.0.0"));
        return subscriptionDescriptor;
    }

    private void assertExampleEvents(Subscription subscription) {
        final List<Event> events = subscription.getEvents();
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getName(), is("example.recipe-added"));
        assertThat(events.get(0).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/example/example.recipe-added.json"));
        assertThat(events.get(1).getName(), is("example.recipe-deleted"));
        assertThat(events.get(1).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/example/example.recipe-deleted.json"));
    }

    private void assertPeopleEvents(Subscription subscription) {
        final List<Event> events = subscription.getEvents();
        assertThat(events.size(), is(2));
        assertThat(events.get(0).getName(), is("people.person-added"));
        assertThat(events.get(0).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/people/people.person-added.json"));
        assertThat(events.get(1).getName(), is("people.person-removed"));
        assertThat(events.get(1).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/people/people.person-removed.json"));
    }
}
