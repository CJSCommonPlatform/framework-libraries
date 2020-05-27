package uk.gov.justice.services.test.domain.aggregate;

import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.doNothing;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.match;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.when;

import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.event.FirstEvent;
import uk.gov.justice.services.test.domain.event.SecondEvent;
import uk.gov.justice.services.test.domain.event.ThirdEvent;

import java.util.stream.Stream;

public class MultipleObjectAggregate implements Aggregate {

    private boolean firstEventApplied;
    private boolean secondEventApplied;

    public Stream<Object> performAction() {
        if (firstEventApplied && secondEventApplied) {
            return apply(Stream.of(new ThirdEvent()));
        }
        return Stream.empty();
    }

    @Override
    public Object apply(final Object event) {
        return match(event).with(
                when(FirstEvent.class)
                        .apply(firstEvent -> this.firstEventApplied = true),
                when(SecondEvent.class)
                        .apply(secondEvent -> this.secondEventApplied = firstEventApplied),
                when(ThirdEvent.class)
                        .apply(thirdEvent -> doNothing())
        );
    }
}
