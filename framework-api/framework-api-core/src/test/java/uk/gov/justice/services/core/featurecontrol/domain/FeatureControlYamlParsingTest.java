package uk.gov.justice.services.core.featurecontrol.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.common.converter.jackson.ObjectMapperProducer;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeatureControlYamlParsingTest {

    @Test
    public void shouldSuccessfullyParseYamlFileToObjects() throws Exception {

        final URL localFeatureFileLocation = getClass().getClassLoader().getResource("feature-control.yaml");

        assertThat(localFeatureFileLocation, is(notNullValue()));

        final ObjectMapper yamlObjectMapper = new ObjectMapperProducer().yamlObjectMapper();

        final FeatureControl featureControl = yamlObjectMapper.readValue(
                localFeatureFileLocation,
                FeatureControl.class);

        final List<Feature> features = featureControl.getFeatures();

        assertThat(features.size(), is(4));
        assertThat(features.get(0).getFeatureName(), is("enabled-feature-1"));
        assertThat(features.get(0).isEnabled(), is(true));
        assertThat(features.get(1).getFeatureName(), is("enabled-feature-2"));
        assertThat(features.get(1).isEnabled(), is(true));
        assertThat(features.get(2).getFeatureName(), is("disabled-feature-1"));
        assertThat(features.get(2).isEnabled(), is(false));
        assertThat(features.get(3).getFeatureName(), is("disabled-feature-2"));
        assertThat(features.get(3).isEnabled(), is(false));
    }
}