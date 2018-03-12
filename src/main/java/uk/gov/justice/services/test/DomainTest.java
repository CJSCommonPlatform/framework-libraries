package uk.gov.justice.services.test;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import uk.gov.justice.domain.annotation.Event;
import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;
import uk.gov.justice.services.test.domain.Metadata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.reflections.Reflections;

@RunWith(Cucumber.class)
@CucumberOptions(strict = false, features = "src/test/resources/domain-features", format = {"pretty",
        "json:target/cucumber.json"}, tags = {"~@ignore"})
public class DomainTest {

    public static final Reflections UK_GOV_REFLECTIONS = new Reflections("uk.gov");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapperProducer().objectMapper();
    private static final String METADATA = "_metadata";
    private static final Set<Class<?>> EVENT_ANNOTATED_CLASSES = UK_GOV_REFLECTIONS.getTypesAnnotatedWith(Event.class);
    private static final String NAME = "name";

    public static List<Object> eventsFromFileNames(final String fileNames) {
        return jsonNodesListFrom(fileNames).stream()
                .map(DomainTest::eventsFromFileNames)
                .collect(toList());
    }

    private static Object eventsFromFileNames(final JsonNode jsonEvent) {
        try {

            Class<?> clazz = EVENT_ANNOTATED_CLASSES.stream()
                    .filter(annotatedClass -> annotatedClass.getAnnotation(Event.class).value().equalsIgnoreCase(eventNameFrom(jsonEvent)))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Error applying initial event. Event class not found"));
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(jsonNodeWithoutMetadataFrom(jsonEvent)), clazz);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage(), e);
        }
    }

    public static String eventNameFrom(JsonNode node) {
        return node.get(METADATA).path(NAME).asText();
    }

    public static String eventNameFrom(Object obj) {
        return obj.getClass().getAnnotation(Event.class).value();
    }

    public static List<JsonNode> jsonNodesListFrom(final String fileNames) {
        List<JsonNode> jsonNodeList = new LinkedList<>();
        Arrays.asList(fileNames.split(","))
                .forEach(fileName -> jsonNodeList.addAll(jsonNodesFrom(fileName.trim())));
        return jsonNodeList;
    }

    public static List<JsonNode> jsonNodesFrom(final String fileName) {
        try {
            List<JsonNode> jsonNodes = new LinkedList<>();
            File eventFile = new File(format("src/test/resources/json/%s.json", fileName));
            eventFile = eventFile.exists() ? eventFile : new File(format("src/test/resources/json/%s.json", fileName.replaceAll("\\s", "-")));
            eventFile = eventFile.exists() ? eventFile : new File(format("src/test/resources/json/%s.json", fileName.replaceAll("\\s", "_")));
            JsonNode eventsJson = OBJECT_MAPPER.readTree(Files.readAllBytes(eventFile.toPath()));
            if (eventsJson.isArray()) {
                eventsJson.elements().forEachRemaining(jsonNodes::add);
            } else {
                jsonNodes.add(eventsJson);
            }
            return jsonNodes;
        } catch (IOException e) {
            throw new IllegalArgumentException(format("Error reading/parsing json file: %s", fileName), e);
        }
    }

    public static JsonNode jsonNodeWithoutMetadataFrom(final JsonNode jsonEvent) {
        final JsonNode jsonEventCopy = jsonEvent.deepCopy();
        ((ObjectNode) jsonEventCopy).remove(METADATA);
        return jsonEventCopy;
    }

    public static Object[] getMethodArguments(final JsonNode jsonNode, final Method target) {
        return stream(target.getParameters())
                .map(p -> OBJECT_MAPPER.convertValue(jsonNode.get(p.getName()), p.getType()))
                .toArray();
    }

    public static JsonNode generatedEventAsJsonNode(final Object generatedEvent) {
        return OBJECT_MAPPER.valueToTree(generatedEvent);
    }

    public static List<JsonNode> generatedEventAsJsonNodeList(final List<Object> generatedEvent) {
        return generatedEvent
                .stream()
                .map(event -> (JsonNode) OBJECT_MAPPER.valueToTree(event))
                .collect(toList());
    }

    public static JsonNode generateEventNodeWithMetadata(final Object event) {
        final Metadata metadataForActualEvent = new Metadata(eventNameFrom(event));
        final JsonNode actualEvent = generatedEventAsJsonNode(event);
        final JsonNode jsonNode = OBJECT_MAPPER.createObjectNode();
        ((ObjectNode)jsonNode).set("_metadata", OBJECT_MAPPER.valueToTree(metadataForActualEvent));
        ((ObjectNode)jsonNode).setAll((ObjectNode)actualEvent);
        return jsonNode;
    }

    public static List<JsonNode> toJsonNodes(final List<Object> jsonNodes) {
        return jsonNodes.stream().map(DomainTest::generateEventNodeWithMetadata).collect(toList());
    }
}
