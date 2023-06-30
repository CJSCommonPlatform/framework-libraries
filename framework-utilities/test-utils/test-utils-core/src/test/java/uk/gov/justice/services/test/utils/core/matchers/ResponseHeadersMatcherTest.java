package uk.gov.justice.services.test.utils.core.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.justice.services.test.utils.core.matchers.ResponseHeadersMatcher.headers;

import uk.gov.justice.services.test.utils.core.http.ResponseData;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.junit.jupiter.api.Test;

public class ResponseHeadersMatcherTest {

	@Test
	public void shouldMatchHeadersFromResponse() throws Exception {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.add("Content-Disposition", "attachment");
		headers.add("filename", "MaterialFullStackTestFile.docx");
		headers.add("Content-Type", "application/pdf");
		headers.add("X-Powered-By", "Undertow/1");		

		assertThat(new ResponseData(null, null, headers), headers().hasHeader(equalTo("Content-Type"),equalTo(List.of("application/pdf"))));

	}

	@Test
	public void shouldFailWhenResponseHeadersDoesNotMatch() {
		MultivaluedMap<String, Object> headers = new MultivaluedMapImpl<String, Object>();
		headers.add("Content-Disposition", "attachment");
		headers.add("filename", "MaterialFullStackTestFile.docx");
		headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		headers.add("X-Powered-By", "Undertow/1");

		try {
			assertThat(new ResponseData(null, null, headers), headers().hasHeader(equalTo("Content-Type"),equalTo(List.of("application/pdf"))));
			fail();
		} catch (final AssertionError expected) {
		}
	}
}
