package uk.gov.justice.services.test.domain.aggregate;


import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.event.SomethingHappened;

import java.util.UUID;
import java.util.stream.Stream;

import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.match;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.when;

public class AggregateWithParameters implements Aggregate {


    private UUID id;

    public Stream<Object> checkOrderParameters(final UUID id,
                                               final int intValue,
                                               final String stringValue,
                                               final boolean booleanValue) {
        return apply(Stream.of(new SomethingHappened(id)));
    }

    @Override
    public Object apply(final Object event) {
        return match(event).with(
                when(SomethingHappened.class).apply(x -> this.id = x.getId()));
    }
}

