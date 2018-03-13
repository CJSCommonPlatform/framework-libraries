package uk.gov.justice.domain;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.domain.SubscriptionBuilder.subscription;

import uk.gov.justice.domain.subscriptiondescriptor.Event;
import uk.gov.justice.domain.subscriptiondescriptor.Eventsource;
import uk.gov.justice.domain.subscriptiondescriptor.Subscription;

import org.junit.Test;

public class SubscriptionBuilderTest {


    @Test
    public void shouldBuildASubscription() throws Exception {

        final String name = "name";
        final Event event_1 = mock(Event.class);
        final Event event_2 = mock(Event.class);
        final Eventsource eventsource = mock(Eventsource.class);

        final Subscription subscription = subscription()
                .withName(name)
                .withEvents(asList(event_1, event_2))
                .withEventsource(eventsource)
                .build();

        assertThat(subscription.getName(), is(name));
        assertThat(subscription.getEventsource(), is(eventsource));
        assertThat(subscription.getEvents(), hasItem(event_1));
        assertThat(subscription.getEvents(), hasItem(event_2));
    }

    @Test
    public void shouldBeAbleToAddEventsOneAtATime() throws Exception {

        final String name = "name";
        final Event event_1 = mock(Event.class);
        final Event event_2 = mock(Event.class);
        final Eventsource eventsource = mock(Eventsource.class);

        final Subscription subscription = subscription()
                .withName(name)
                .withEvent(event_1)
                .withEvent(event_2)
                .withEventsource(eventsource)
                .build();

        assertThat(subscription.getName(), is(name));
        assertThat(subscription.getEventsource(), is(eventsource));
        assertThat(subscription.getEvents(), hasItem(event_1));
        assertThat(subscription.getEvents(), hasItem(event_2));
    }
}
