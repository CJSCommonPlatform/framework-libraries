package uk.gov.justice.services.test.utils.core.http;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.justice.services.test.utils.core.http.PollingRequestParams.DEFAULT_DELAY_MILLIS;
import static uk.gov.justice.services.test.utils.core.http.PollingRequestParams.DEFAULT_RETRY_COUNT;
import static uk.gov.justice.services.test.utils.core.http.RequestParamsBuilder.requestParams;
import static uk.gov.justice.services.test.utils.core.http.RequestParamsBuilder.requestParamswithHeaders;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.Test;

public class RequestParamsBuilderTest {

	
	 @Test
	    public void shouldCreateRequestParametersBuilder() throws Exception {

	        final String url = "a url";
	        final String mediaType = "the media type";
	     
	        final RequestParamsBuilder requestParamsBuilder = requestParams(url, mediaType);
	        final RequestParams requestParams = requestParamsBuilder.build();

	        assertThat(requestParams.getUrl(), is(url));
	        assertThat(requestParams.getMediaType(), is(mediaType));
	        assertThat(requestParams.getHeaders().isEmpty(), is(true));
	    }
	 
	 @Test
	    public void shouldCreateRequestParametersBuilderWithHeader() throws Exception {

	        final String url = "a url";
	        final String mediaType = "the media type";
	        MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
			headers.add("key", "value");
	        
	        final RequestParamsBuilder requestParamsBuilder = requestParamswithHeaders(url, mediaType, headers);
	        final RequestParams requestParams = requestParamsBuilder.build();

	        assertThat(requestParams.getUrl(), is(url));
	        assertThat(requestParams.getMediaType(), is(mediaType));
	        assertThat(requestParams.getHeaders().size(), is(1));
	        assertThat(requestParams.getHeaders().get(0), is(headers.get(0)));	    }
	 
}
