package uk.gov.justice.services.messaging.spi;

import static java.util.stream.StreamSupport.stream;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BinaryOperator;

public class JsonEnvelopeProviderSelector {

    private final BinaryOperator<JsonEnvelopeProvider> highestPriorityAccumulator =
            (jsonEnvelopeProvider, otherJsonEnvelopeProvider) -> {
                if (jsonEnvelopeProvider.priority() <= otherJsonEnvelopeProvider.priority()) {
                    return jsonEnvelopeProvider;
                }

                return otherJsonEnvelopeProvider;
            };

    public JsonEnvelopeProvider selectFrom(final Spliterator<JsonEnvelopeProvider> spliterator) {

        final Optional<JsonEnvelopeProvider> jsonEnvelopeProvider =
                stream(spliterator, false)
                        .reduce(highestPriorityAccumulator);

        return jsonEnvelopeProvider
                .orElseThrow(() -> new JsonEnvelopeProviderNotFoundException("No JsonEnvelopeProvider implementation found"));
    }
}
