package uk.gov.justice.domain.subscriptiondescriptor;

public class SubscriptionDescriptorDef {

    private final SubscriptionDescriptor subscriptionDescriptor;

    public SubscriptionDescriptorDef(final SubscriptionDescriptor subscriptionDescriptor) {
        this.subscriptionDescriptor = subscriptionDescriptor;
    }

    public SubscriptionDescriptor getSubscriptionDescriptor() {
        return subscriptionDescriptor;
    }
}

