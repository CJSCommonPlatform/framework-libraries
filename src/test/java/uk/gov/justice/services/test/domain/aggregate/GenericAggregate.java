package uk.gov.justice.services.test.domain.aggregate;

import org.apache.commons.lang3.tuple.Pair;
import uk.gov.justice.domain.aggregate.Aggregate;
import uk.gov.justice.services.test.domain.arg.ComplexArgument;
import uk.gov.justice.services.test.domain.event.SthDoneWithIntArgEvent;
import uk.gov.justice.services.test.domain.event.SthDoneWithNoArgsEvent;
import uk.gov.justice.services.test.domain.event.SthDoneWithStringArgEvent;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


public class GenericAggregate implements Aggregate {


    private List<Object> appliedEvents = new LinkedList<>();
    private List<Pair<String, Object[]>> methodInvocations = new LinkedList<>();

    public Stream<Object> doSthWithNoArgs() {
        methodInvocations.add(Pair.of("doSthWithNoArgs", new Object[]{}));
        return Stream.of(new SthDoneWithNoArgsEvent());
    }

    public Stream<Object> doSthWithStringArg(final String stringArg) {
        methodInvocations.add(Pair.of("doSthWithStringArg", new Object[]{stringArg}));
        return Stream.of(new SthDoneWithStringArgEvent(stringArg));
    }

    public Stream<Object> doSthWithIntArg(final Integer intArg) {
        methodInvocations.add(Pair.of("doSthWithIntArg", new Object[]{intArg}));
        return Stream.of(new SthDoneWithIntArgEvent(intArg));
    }

    public Stream<Object> doSthWithLongArg(final Long longArg) {
        methodInvocations.add(Pair.of("doSthWithLongArg", new Object[]{longArg}));
        return Stream.of(new SthDoneWithNoArgsEvent());
    }

    public Stream<Object> doSthWithStringAndUUIDArg(final String stringArg, final UUID id) {
        methodInvocations.add(Pair.of("doSthWithStringAndUUIDArg", new Object[]{stringArg, id}));
        return Stream.of(new SthDoneWithNoArgsEvent());
    }

    public Stream<Object> doSthWithBooleanArg(final Boolean booleanArg) {
        methodInvocations.add(Pair.of("doSthWithBooleanArg", new Object[]{booleanArg}));
        return Stream.of(new SthDoneWithNoArgsEvent());
    }

    public Stream<Object> doSthWithDateTimeArg(final ZonedDateTime dateTimeArg) {
        methodInvocations.add(Pair.of("doSthWithDateTimeArg", new Object[]{dateTimeArg}));
        return Stream.of(new SthDoneWithNoArgsEvent());
    }

    public Stream<Object> doSthWithComplexArg(final ComplexArgument complexArgument) {
        methodInvocations.add(Pair.of("doSthWithComplexArg", new Object[]{complexArgument}));
        return Stream.of(new SthDoneWithNoArgsEvent());
    }

    @Override
    public Object apply(final Object event) {
        return appliedEvents.add(event);
    }

    public List<Object> appliedEvents() {
        return appliedEvents;
    }

    public List<Pair<String, Object[]>> methodInvocations() {
        return methodInvocations;
    }

}