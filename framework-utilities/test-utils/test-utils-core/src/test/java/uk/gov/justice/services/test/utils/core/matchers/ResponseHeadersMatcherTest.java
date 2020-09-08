package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static uk.gov.justice.services.test.utils.core.matchers.ResponseHeadersMatcher.headers;

import uk.gov.justice.services.test.utils.core.http.ResponseData;

import java.util.Arrays;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ResponseHeadersMatcherTest {

	@Rule
	public ExpectedException expectedException = none();

	@Test
	public void shouldMatchHeadersFromResponse() throws Exception {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.add("Content-Disposition", "attachment");
		headers.add("filename", "MaterialFullStackTestFile.docx");
		headers.add("Content-Type", "application/pdf");
		headers.add("X-Powered-By", "Undertow/1");		

		assertThat(new ResponseData(null, null, headers), headers().hasHeader(equalTo("Content-Type"),equalTo( Arrays.asList("application/pdf"))));

	}

	@Test(expected = AssertionError.class)
	public void shouldFailWhenResponseHeadersDoesNotMatch() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.add("Content-Disposition", "attachment");
		headers.add("filename", "MaterialFullStackTestFile.docx");
		headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		headers.add("X-Powered-By", "Undertow/1");

		assertThat(new ResponseData(null, null, headers), headers().hasHeader(equalTo("Content-Type"),equalTo( Arrays.asList("application/pdf"))));
	}
}
