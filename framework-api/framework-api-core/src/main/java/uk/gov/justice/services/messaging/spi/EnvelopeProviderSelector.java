package uk.gov.justice.services.messaging.spi;

import static java.util.stream.StreamSupport.stream;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BinaryOperator;

public class EnvelopeProviderSelector {

    private final BinaryOperator<EnvelopeProvider> highestPriorityAccumulator =
            (envelopeProvider, otherEnvelopeProvider) -> {
                if (envelopeProvider.priority() <= otherEnvelopeProvider.priority()) {
                    return envelopeProvider;
                }

                return otherEnvelopeProvider;
            };

    public EnvelopeProvider selectFrom(final Spliterator<EnvelopeProvider> spliterator) {

        final Optional<EnvelopeProvider> envelopeProvider =
                stream(spliterator, false)
                        .reduce(highestPriorityAccumulator);

        return envelopeProvider
                .orElseThrow(() -> new EnvelopeProviderNotFoundException("No EnvelopeProvider implementation found"));
    }
}
