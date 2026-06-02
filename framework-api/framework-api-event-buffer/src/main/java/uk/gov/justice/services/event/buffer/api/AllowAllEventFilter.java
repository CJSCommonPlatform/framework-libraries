package uk.gov.justice.services.event.buffer.api;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
@Priority(1)
public class AllowAllEventFilter implements EventFilter {
    @Override
    public boolean accepts(final String eventName) {
        return true;
    }
}
