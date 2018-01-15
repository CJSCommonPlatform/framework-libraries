package uk.gov.justice.services.test.domain.aggregate;


import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.match;
import static uk.gov.justice.domain.aggregate.matcher.EventSwitcher.when;

import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.event.NoteCreated;
import uk.gov.justice.services.test.domain.event.SomethingHappened;

import java.util.UUID;
import java.util.stream.Stream;

public class AggregateWithParameters implements Aggregate {


    private UUID id;

    public Stream<Object> checkOrderParameters(final UUID id,
                                               final int intValue,
                                               final String stringValue,
                                               final boolean booleanValue) {
        return apply(Stream.of(new SomethingHappened(id)));
    }

    public Stream<Object> createNote(final UUID noteId) {
        return apply(Stream.of(new NoteCreated(id)));
    }

    @Override
    public Object apply(final Object event) {
        return match(event).with(
                when(SomethingHappened.class).apply(x -> this.id = x.getId()),
                when(NoteCreated.class).apply(x -> this.id = x.getNoteId()));
    }
}

