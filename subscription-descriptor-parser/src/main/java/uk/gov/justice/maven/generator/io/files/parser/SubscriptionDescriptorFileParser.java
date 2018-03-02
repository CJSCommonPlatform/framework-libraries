package uk.gov.justice.maven.generator.io.files.parser;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptor;
import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptorDef;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class SubscriptionDescriptorFileParser implements FileParser<SubscriptionDescriptor> {

    @Override
    public Collection<SubscriptionDescriptor> parse(final Path baseDir, final Collection<Path> paths) {
        return paths.stream()
                .map(path -> read(baseDir.resolve(path).toAbsolutePath().toString()))
                .collect(Collectors.toList());
    }

    private SubscriptionDescriptor read(final String filePath) {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule(PROPERTIES));

        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        try {
            return mapper.readValue(new File(filePath), SubscriptionDescriptorDef.class).getSubscriptionDescriptor();
        } catch (IOException e) {
            throw new SubscriptionDescriptorIOException("Failed to read subscriptions yaml file", e);
        }
    }
}
