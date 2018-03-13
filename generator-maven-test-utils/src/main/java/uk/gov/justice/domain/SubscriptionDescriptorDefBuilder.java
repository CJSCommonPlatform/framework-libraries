package uk.gov.justice.domain;

import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptor;
import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptorDef;

public final class SubscriptionDescriptorDefBuilder {
    
    private SubscriptionDescriptor subscriptionDescriptor;

    private SubscriptionDescriptorDefBuilder() {
    }

    public static SubscriptionDescriptorDefBuilder subscriptionDescriptorDef() {
        return new SubscriptionDescriptorDefBuilder();
    }

    public SubscriptionDescriptorDefBuilder withSubscriptionDescriptor(final SubscriptionDescriptor subscriptionDescriptor) {
        this.subscriptionDescriptor = subscriptionDescriptor;
        return this;
    }

    public SubscriptionDescriptorDef build() {
        return new SubscriptionDescriptorDef(subscriptionDescriptor);
    }
}
