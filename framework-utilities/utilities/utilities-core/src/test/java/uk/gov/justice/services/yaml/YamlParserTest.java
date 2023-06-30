package uk.gov.justice.services.yaml;

import static java.nio.file.Paths.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import uk.gov.justice.services.yaml.subscriptiondescriptor.Event;
import uk.gov.justice.services.yaml.subscriptiondescriptor.Subscription;
import uk.gov.justice.services.yaml.subscriptiondescriptor.SubscriptionDescriptorDef;
import uk.gov.justice.services.yaml.subscriptiondescriptor.SubscriptionsDescriptor;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

public class YamlParserTest {

    @Test
    public void shouldParseSubscriptionPathAsSubscriptionDescriptorDefUsingClassType() throws Exception {

        final URL url = getFromClasspath("yaml/subscriptions-descriptor.yaml");

        final SubscriptionDescriptorDef subscriptionDescriptorDefinitionDef = yamlParser().parseYamlFrom(url, SubscriptionDescriptorDef.class);
        final SubscriptionsDescriptor subscriptionsDescriptor = subscriptionDescriptorDefinitionDef.getSubscriptionsDescriptor();
        assertThat(subscriptionsDescriptor.getService(), is("examplecontext"));
        assertThat(subscriptionsDescriptor.getServiceComponent(), is("EVENT_LISTENER"));
        assertThat(subscriptionsDescriptor.getSpecVersion(), is("1.0.0"));

        final List<Subscription> subscriptions = subscriptionsDescriptor.getSubscriptions();
        assertThat(subscriptions.size(), is(2));

        final Subscription subscription_1 = subscriptions.get(0);
        assertThat(subscription_1.getName(), is("subscription1"));
        assertThat(subscription_1.getEventSourceName(), is("example"));

        final List<Event> events_1 = subscription_1.getEvents();
        assertThat(events_1.size(), is(2));
        assertThat(events_1.get(0).getName(), is("example.recipe-added"));
        assertThat(events_1.get(0).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/example/example.recipe-added.json"));
        assertThat(events_1.get(1).getName(), is("example.recipe-deleted"));
        assertThat(events_1.get(1).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/example/example.recipe-deleted.json"));
    }


    @Test
    public void shouldParseSubscriptionPathAsSubscriptionDescriptorDef() throws Exception {

        final URL url = getFromClasspath("yaml/subscriptions-descriptor.yaml");

        final TypeReference<HashMap<String, SubscriptionsDescriptor>> typeReference
                = new TypeReference<>() {
        };

        final Map<String, SubscriptionsDescriptor> subscriptionDescriptorDefinitionMap = yamlParser().parseYamlFrom(url, typeReference);
        final SubscriptionsDescriptor subscriptionsDescriptor = subscriptionDescriptorDefinitionMap.get("subscriptions_descriptor");
        assertThat(subscriptionsDescriptor.getService(), is("examplecontext"));
        assertThat(subscriptionsDescriptor.getServiceComponent(), is("EVENT_LISTENER"));
        assertThat(subscriptionsDescriptor.getSpecVersion(), is("1.0.0"));

        final List<Subscription> subscriptions = subscriptionsDescriptor.getSubscriptions();
        assertThat(subscriptions.size(), is(2));

        final Subscription subscription_1 = subscriptions.get(0);
        assertThat(subscription_1.getName(), is("subscription1"));
        assertThat(subscription_1.getEventSourceName(), is("example"));

        final List<Event> events_1 = subscription_1.getEvents();
        assertThat(events_1.size(), is(2));
        assertThat(events_1.get(0).getName(), is("example.recipe-added"));
        assertThat(events_1.get(0).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/example/example.recipe-added.json"));
        assertThat(events_1.get(1).getName(), is("example.recipe-deleted"));
        assertThat(events_1.get(1).getSchemaUri(), is("http://justice.gov.uk/json/schemas/domains/example/example.recipe-deleted.json"));
    }


    @Test
    public void shouldThrowFileNotFoundExceptionForTypeReference() throws Exception {

        final URL url = get("this-subscription-does-not-exist.yaml").toUri().toURL();
        final TypeReference<SubscriptionsDescriptor> typeReference
                = new TypeReference<SubscriptionsDescriptor>() {
        };

        try {
            yamlParser().parseYamlFrom(url, typeReference);
            fail();
        } catch (final YamlParserException e) {
            assertThat(e.getCause(), is(instanceOf(FileNotFoundException.class)));
            assertThat(e.getMessage(), containsString("Failed to read YAML file"));
            assertThat(e.getMessage(), containsString("this-subscription-does-not-exist.yaml"));
        }
    }

    @Test
    public void shouldThrowFileNotFoundExceptionForClassType() throws Exception {

        final URL url = get("this-subscription-does-not-exist.yaml").toUri().toURL();
        try {
            yamlParser().parseYamlFrom(url, SubscriptionsDescriptor.class);
            fail();
        } catch (final YamlParserException e) {
            assertThat(e.getCause(), is(instanceOf(FileNotFoundException.class)));
            assertThat(e.getMessage(), containsString("Failed to read YAML file"));
            assertThat(e.getMessage(), containsString("this-subscription-does-not-exist.yaml"));
        }
    }

    private YamlParser yamlParser() {
        final YamlParser yamlParser = new YamlParser();
        return yamlParser;
    }

    @SuppressWarnings("ConstantConditions")
    private URL getFromClasspath(final String name) throws MalformedURLException {
        return get(getClass().getClassLoader().getResource(name).getPath()).toUri().toURL();
    }
}
