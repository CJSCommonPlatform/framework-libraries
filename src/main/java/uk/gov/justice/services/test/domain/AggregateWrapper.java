package uk.gov.justice.services.test.domain;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static uk.gov.justice.services.test.DomainTest.UK_GOV_REFLECTIONS;
import static uk.gov.justice.services.test.DomainTest.eventsFromFileNames;
import static uk.gov.justice.services.test.DomainTest.getMethodArguments;
import static uk.gov.justice.services.test.DomainTest.jsonNodesFrom;

import uk.gov.justice.domain.aggregate.Aggregate;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;

public class AggregateWrapper {

    private final List<Object> generatedEvents = new LinkedList<>();
    private final List<Object> initialEvents = new LinkedList<>();
    private boolean initialised = false;
    private Aggregate aggregate;


    public static AggregateWrapper aggregateWrapper() {
        return new AggregateWrapper();
    }

    public AggregateWrapper withInitialEventsFromFiles(final String fileNames) {
        this.initialEvents.addAll(eventsFromFileNames(fileNames));
        return this;
    }

    public AggregateWrapper initialiseFromClass(final String className) throws IllegalAccessException, InstantiationException {
        if (aggregate == null) {
            Optional<Class<? extends Aggregate>> clazz = UK_GOV_REFLECTIONS.getSubTypesOf(Aggregate.class).stream()
                    .filter((Class<? extends Aggregate> consumer) -> consumer.getSimpleName().equalsIgnoreCase(className))
                    .findFirst();
            this.aggregate = clazz
                    .orElseThrow(() -> new IllegalArgumentException(format("Error initialising Aggregate: %s. Aggregate class not found", className)))
                    .newInstance();
            this.aggregate.apply(initialEvents.stream());
        }
        this.initialised = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public void invokeMethod(final String methodName, final String fileContainingArguments) throws Exception {

        final JsonNode json = jsonNodesFrom(fileContainingArguments).get(0);
        final Set<String> suppliedParamNames = newHashSet(json.fieldNames());
        final Method method = getMethod(aggregate.getClass(), methodName, suppliedParamNames);

        final Object[] args = getMethodArguments(json, method);
        final List<Object> newEvents = ((Stream<Object>) method.invoke(aggregate, args)).collect(toList());
        generatedEvents.addAll(newEvents);
    }

    @SuppressWarnings("unchecked")
    public void invokeMethod(final String methodName) throws Exception {
        final Method method = getMethod(aggregate.getClass(), methodName, Collections.emptySet());
        final List<Object> newEvents = ((Stream<Object>) method.invoke(aggregate)).collect(toList());
        generatedEvents.addAll(newEvents);
    }

    private Method getMethod(final Class<?> clazz, final String methodName, final Set<String> suppliedParamNames) {
        final List<Method> methods = stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .filter(m -> suppliedParamNames.equals(
                        stream(m.getParameters())
                                .map(Parameter::getName)
                                .collect(toSet())))
                .collect(toList());

        if (methods.size() == 0) {
            throw new IllegalArgumentException("No method found");
        } else if (methods.size() > 1) {
            throw new IllegalArgumentException("Too many matching methods found");
        }

        return methods.get(0);
    }

    public Aggregate aggregate() {
        return aggregate;
    }

    public List<Object> generatedEvents() {
        return generatedEvents;
    }

    public boolean initialised() {
        return initialised;
    }
}
