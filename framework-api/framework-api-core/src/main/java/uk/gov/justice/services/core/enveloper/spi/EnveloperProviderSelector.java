package uk.gov.justice.services.core.enveloper.spi;

import static java.util.stream.StreamSupport.stream;

import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BinaryOperator;

public class EnveloperProviderSelector {

    private final BinaryOperator<EnveloperProvider> highestPriorityAccumulator =
            (enveloperProvider, otherEnveloperProvider) -> {
                if (enveloperProvider.priority() <= otherEnveloperProvider.priority()) {
                    return enveloperProvider;
                }

                return otherEnveloperProvider;
            };

    public EnveloperProvider selectFrom(final Spliterator<EnveloperProvider> spliterator) {

        final Optional<EnveloperProvider> enveloperProvider =
                stream(spliterator, false)
                        .reduce(highestPriorityAccumulator);

        return enveloperProvider
                .orElseThrow(() -> new EnveloperProviderNotFoundException("No EnveloperProvider implementation found"));
    }
}
