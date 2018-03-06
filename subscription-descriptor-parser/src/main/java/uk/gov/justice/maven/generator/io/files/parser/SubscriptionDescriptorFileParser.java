package uk.gov.justice.maven.generator.io.files.parser;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.lang.String.format;

import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptor;
import uk.gov.justice.domain.subscriptiondescriptor.SubscriptionDescriptorDef;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.everit.json.schema.ValidationException;

public class SubscriptionDescriptorFileParser implements FileParser<SubscriptionDescriptor> {


    private SubscriptionDescriptorFileValidator subscriptionDescriptorFileValidator;

    public SubscriptionDescriptorFileParser(final SubscriptionDescriptorFileValidator subscriptionDescriptorFileValidator) {
        this.subscriptionDescriptorFileValidator = subscriptionDescriptorFileValidator;
    }
    @Override
    public Collection<SubscriptionDescriptor> parse(final Path baseDir, final Collection<Path> paths) {
        return paths.stream()
                .map(path -> read(baseDir.resolve(path).toAbsolutePath()))
                .collect(Collectors.toList());
    }

    private SubscriptionDescriptor read(final Path filePath) {
        try {
            subscriptionDescriptorFileValidator.validate(filePath);

            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                    .registerModule(new Jdk8Module())
                    .registerModule(new ParameterNamesModule(PROPERTIES));

            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

            return mapper.readValue(filePath.toFile(), SubscriptionDescriptorDef.class).getSubscriptionDescriptor();
        } catch (final NoSuchFileException e) {
            throw new SubscriptionDescriptorException(format("No such subscriptions YAML file %s ", filePath), e);
        } catch (final ValidationException e) {
            throw new SubscriptionDescriptorException(format("Failed to validate subscriptions yaml file %s ", filePath), e);
        } catch (final IOException e) {
            throw new SubscriptionDescriptorException(format("Failed to read subscriptions yaml file %s ", filePath), e);
        }
    }
}
