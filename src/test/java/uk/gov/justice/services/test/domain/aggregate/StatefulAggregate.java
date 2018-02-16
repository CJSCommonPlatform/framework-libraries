package uk.gov.justice.services.test.domain.aggregate;


import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.event.SomethingElseHappened;
import uk.gov.justice.services.test.domain.event.SomethingHappened;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.match;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.when;

public class StatefulAggregate implements Aggregate {

    private UUID id;

    private boolean eventApplied;

    public Stream<Object> doSomething(final UUID id) {
        if (eventApplied) {
            return apply(Stream.of(new SomethingElseHappened(id)));
        }
        return apply(Stream.of(new SomethingHappened(id)));
    }

    public Stream<Object> doSomethingTwice(final UUID id) {
        final List<SomethingHappened> values = new ArrayList<>();
        values.add(new SomethingHappened(id));
        values.add(new SomethingHappened(id));
        return apply(Stream.of(new SomethingHappened(id), new SomethingHappened(id)));
    }

    public Stream<Object> doNothing() {
        return Stream.empty();
    }

    @Override
    public Object apply(final Object event) {
        return match(event).with(
                when(SomethingHappened.class)
                        .apply(somethingHappenedEvent -> {
                    this.eventApplied = true;
                    this.id = somethingHappenedEvent.getId();
                }),
                when(SomethingElseHappened.class)
                        .apply(somethingElseHappenedEvent -> this.id = somethingElseHappenedEvent.getId())
        );
    }
}
