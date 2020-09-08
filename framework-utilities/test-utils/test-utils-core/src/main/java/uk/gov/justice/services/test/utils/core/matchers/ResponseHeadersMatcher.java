package uk.gov.justice.services.test.utils.core.matchers;

import static java.util.Optional.empty;

import uk.gov.justice.services.test.utils.core.http.ResponseData;

import java.util.Optional;

import javax.ws.rs.core.MultivaluedMap;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsMapContaining;

public class ResponseHeadersMatcher extends ResponseMatcher<ResponseData> {
	private Optional<IsMapContaining<String, Object>> headersMatcher = empty();

	public static ResponseHeadersMatcher headers() {
		return new ResponseHeadersMatcher();
	}

	public ResponseHeadersMatcher hasHeader(final Matcher<String> keyMatcher, final Matcher<Object> valueMatcher) {
		this.headersMatcher = Optional.of(new IsMapContaining<>(keyMatcher, valueMatcher));
		return this;
	}

	@Override
	protected boolean matchesSafely(final ResponseData item, final Description mismatchDescription) {
		final MultivaluedMap<String, Object> actualHeaders = item.getHeaders();
		
		if (headersMatcher.isPresent() && !headersMatcher.get().matches(actualHeaders)) {
			describeMismatch(headersMatcher.get(), actualHeaders, mismatchDescription);
			return false;
		} 
		return true; 

	}

	private void describeMismatch(final IsMapContaining<String, Object> isMapContaining,
			final MultivaluedMap<String, Object> actualHeaders, final Description mismatchDescription) {
		mismatchDescription.appendText("Headers ");
		isMapContaining.describeMismatch(actualHeaders, mismatchDescription);

	}

	@Override
	public void describeTo(final Description description) {
		headersMatcher.ifPresent(matcher -> description.appendText("Headers ").appendDescriptionOf(matcher));

	}

}
