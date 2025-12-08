package uk.gov.justice.services.test.utils.core.http;

import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ws.rs.core.Response.Status.fromStatusCode;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.awaitility.pollinterval.PollInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.justice.services.test.utils.core.rest.RestClient;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import com.google.common.annotations.VisibleForTesting;
import org.awaitility.core.ConditionEvaluationLogger;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Client for polling a rest endpoint and matching the response against the specified Matcher.
 * <p>
 * To Use:
 * <p>
 * Poll until the response has specified number of events with required json payload:
 * <pre><blockquote>
 *
 *      import static uk.gov.justice.services.test.utils.core.http.RequestParamsBuilder.requestParams;
 *      import static uk.gov.justice.services.test.utils.core.http.RestPoller.poll;
 *
 *      final String url = "http://localhost:8080/my-context/my/rest/endpoint";
 *      final String mediaType = "application/vnd.notification.query.events+json";
 *
 *      final RequestParams requestParams = requestParams(url, mediaType)
 *              .withHeader("header-name", "header-value")
 *              .build();
 *
 *      poll(requestParams)
 *          .until(
 *              status().is(OK),
 *              payload()
 *                  .isJson(allOf(
 *                      withJsonPath("$.events", hasSize(2)),
 *                      withJsonPath("$.events[0].userId", is(userId1)),
 *                      withJsonPath("$.events[1].userId", is(userId2))
 *                      )
 *                  )
 *          )
 *      ;
 *
 * </blockquote></pre>
 * <p>
 * The call is configured using <code>RequestParams</code>. This object is most easily created using
 * a <code>RequestParamsBuilder</code> which takes a url and media type and will supply all other
 * required parameters with defaults. Overriding these defaults is done in the usual builder pattern
 * way.
 * <p>
 * Poll until the response has specified substring in the payload:
 * <pre><blockquote>
 *
 *      import static uk.gov.justice.services.test.utils.core.http.RequestParamsBuilder.requestParams;
 *      import static uk.gov.justice.services.test.utils.core.http.RestPoller.poll;
 *
 *      final String url = "http://localhost:8080/my-context/my/rest/endpoint";
 *      final String mediaType = "application/vnd.notification.query.events+json";
 *
 *      final RequestParams requestParams = requestParams(url, mediaType)
 *              .withHeader("header-name", "header-value")
 *              .build();
 *
 *      poll(requestParams)
 *          .until(
 *              status().is(OK),
 *              payload()
 *                  .that(
 *                      containsString("notification-event.xlsx")
 *                  )
 *          )
 *      ;
 *
 * </blockquote></pre>
 * <p>
 * Poll ignoring certain response status, until the response has specified number of events with
 * required data:
 * <pre><blockquote>
 *
 *      import static uk.gov.justice.services.test.utils.core.http.RequestParamsBuilder.requestParams;
 *      import static uk.gov.justice.services.test.utils.core.http.RestPoller.poll;
 *
 *      final String url = "http://localhost:8080/my-context/my/rest/endpoint";
 *      final String mediaType = "application/vnd.notification.query.events+json";
 *
 *      final RequestParams requestParams = requestParams(url, mediaType)
 *              .withHeader("header-name", "header-value")
 *              .build();
 *
 *      poll(requestParams)
 *          .ignoring(
 *              status().is(NOT_FOUND)
 *          )
 *          .and()
 *          .ignoring(
 *              status().is(FORBIDDEN)
 *          )
 *          .until(
 *              status().is(OK),
 *              payload()
 *                  .isJson(allOf(
 *                      withJsonPath("$.events", hasSize(2)),
 *                      withJsonPath("$.events[0].userId", is(userId1)),
 *                      withJsonPath("$.events[1].userId", is(userId2))
 *                      )
 *                  )
 *          )
 *      ;
 *
 * </blockquote></pre>
 */
public class RestPoller {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestPoller.class);

    private final RestClient restClient;
    private final RequestParams requestParams;

    private ConditionFactory await;
    private Matcher<ResponseData> expectedResponseMatcher;
    private Optional<Matcher<ResponseData>> ignoredResponseMatcher = empty();


    /**
     * Instantiates a new rest poller
     *
     * @deprecated use RestPoller(RestClient, requestParams, pollInterval, timeout),
     * default polling values defined here are not viable for most of usages
     */
    @VisibleForTesting
    @Deprecated
    RestPoller(final RestClient restClient, final RequestParams requestParams) {
        this.requestParams = requestParams;
        this.restClient = restClient;
        this.await = await().with()
                .pollInterval(1, SECONDS)
                .with().timeout(10, SECONDS);
    }

    @VisibleForTesting
    RestPoller(final RestClient restClient, final RequestParams requestParams, PollInterval pollInterval, Duration timeout) {
        this.requestParams = requestParams;
        this.restClient = restClient;
        this.await = await()
                .with()
                .pollInterval(pollInterval)
                .timeout(timeout);
    }

    /**
     * Instantiates a new rest poller
     *
     * @param requestParams request parameters
     * @return this
     * @deprecated use poll(RestClient, requestParams, pollInterval, timeout),
     * default polling values defined here are not viable for most of usages
     */
    @Deprecated
    public static RestPoller poll(final RequestParams requestParams) {
        return new RestPoller(new RestClient(), requestParams);
    }

    /**
     * Instantiates a new rest poller
     *
     * @param requestParamsBuilder request parameters builder
     * @return this
     * @deprecated use poll(RestClient, requestParams, pollInterval, timeout),
     * default polling values defined here are not viable for most of usages
     */
    @Deprecated
    public static RestPoller poll(final RequestParamsBuilder requestParamsBuilder) {
        return new RestPoller(new RestClient(), requestParamsBuilder.build());
    }


    /**
     * Instantiates a new rest poller
     *
     * @param requestParams request parameters
     * @return this
     */
    public static RestPoller poll(final RequestParams requestParams, PollInterval pollInterval, Duration timeout) {
        return new RestPoller(new RestClient(), requestParams, pollInterval, timeout);
    }

    /**
     * Specify matchers to ignore the intermediate responses received during the poll.
     *
     * @param matchers response data matchers
     * @return this
     */
    public RestPoller ignoring(final Matcher<ResponseData>... matchers) {
        if (ignoredResponseMatcher.isPresent()) {
            this.ignoredResponseMatcher = Optional.of(anyOf(ignoredResponseMatcher.get(), allOf(matchers)));
        } else {
            this.ignoredResponseMatcher = Optional.of(allOf(matchers));
        }
        return this;
    }

    /**
     * Poll the rest endpoint <code>until</code> the response matches the specified matchers or
     * throw a timeout exception.
     *
     * @param matchers response data matchers
     * @return final response data
     * @throws ConditionTimeoutException If condition was not fulfilled within the given time
     *                                   period.
     */
    public ResponseData until(final Matcher<ResponseData>... matchers) {
        expectedResponseMatcher = allOf(matchers);

        final ResponseData responseData = this.await.until(new CallableRestClient(requestParams), combinedMatcher());

        assertThat(responseData, is(expectedResponseMatcher));

        return responseData;
    }

    /**
     * print the matcher evaluation results, on every poll, to the console using System.out.printf.
     * Also print the final value if applicable.
     *
     * @return this
     */
    public RestPoller logging() {
        this.await = this.await.with().conditionEvaluationListener(new ConditionEvaluationLogger());
        return this;
    }

    /**
     * Overrides the delay between polls. If not specified a default of 1 second is used.
     *
     * @param pollInterval the poll interval
     * @param unit         the unit
     * @return this
     */
    public RestPoller pollInterval(final long pollInterval, final TimeUnit unit) {
        this.await = this.await.with().pollInterval(pollInterval, unit);
        return this;
    }

    /**
     * Overrides the delay between polls. If not specified a default of 1 second is used.
     *
     * @param pollInterval PollInterval Strategy, please look {@link FibonacciPollWithStartAndMax}
     * @return this
     */
    public RestPoller pollInterval(PollInterval pollInterval) {
        this.await = this.await.with().pollInterval(pollInterval);
        return this;
    }

    /**
     * Poll at most <code>timeout</code> before throwing a timeout exception.
     * <p>
     * Overrides the default timeout period. If not specified a default of 10 seconds is used.
     *
     * @param timeout the timeout
     * @param unit    the unit
     */
    public RestPoller timeout(final long timeout, final TimeUnit unit) {
        this.await = this.await.with().timeout(timeout, unit);
        return this;
    }

    /**
     * Specify the delay that will be used before RestPoller starts polling for the
     * result the first time. If you don't specify a poll delay explicitly it'll be the same as the
     * poll interval.
     *
     * @param delay the delay
     * @param unit  the unit
     */
    public RestPoller pollDelay(final long delay, final TimeUnit unit) {
        this.await = this.await.with().pollDelay(delay, unit);
        return this;
    }

    /**
     * A method to increase the readability
     *
     * @return this
     */
    public RestPoller with() {
        return this;
    }

    /**
     * A method to increase the readability
     *
     * @return this
     */
    public RestPoller and() {
        return this;
    }


    private Matcher<ResponseData> combinedMatcher() {
        if (ignoredResponseMatcher.isPresent()) {
            return new StopPollingMatcher(expectedResponseMatcher, ignoredResponseMatcher.get());
        }
        return expectedResponseMatcher;
    }


    private class CallableRestClient implements Callable<ResponseData> {
        private final RequestParams requestParams;

        private CallableRestClient(final RequestParams requestParams) {
            this.requestParams = requestParams;
        }

        @Override
        public ResponseData call() {
            try (Response response = restClient.query(
                    requestParams.getUrl(),
                    requestParams.getMediaType(),
                    requestParams.getHeaders())) {

                String payload = null;
                try {
                    payload = response.readEntity(String.class);
                } catch (Exception e) {
                    LOGGER.trace("Failed to read response body", e);
                }
                return new ResponseData(fromStatusCode(response.getStatus()), payload, response.getHeaders());
            }
        }
    }

    private class StopPollingMatcher extends TypeSafeDiagnosingMatcher<ResponseData> {

        private final Matcher<ResponseData> expectedResponseDataMatcher;
        private final Matcher<ResponseData> ignoredResponseDataMatcher;

        StopPollingMatcher(final Matcher<ResponseData> expectedResponseDataMatcher, final Matcher<ResponseData> ignoredResponseDataMatcher) {
            this.expectedResponseDataMatcher = expectedResponseDataMatcher;
            this.ignoredResponseDataMatcher = ignoredResponseDataMatcher;
        }

        @Override
        protected boolean matchesSafely(final ResponseData responseData, final Description description) {
            if (expectedResponseDataMatcher.matches(responseData)) {
                return true;
            } else if (ignoredResponseDataMatcher.matches(responseData)) {
                ignoredResponseDataMatcher.describeMismatch(responseData, description);
                return false;
            }

            return true;
        }

        @Override
        public void describeTo(final Description description) {
            description.appendDescriptionOf(ignoredResponseDataMatcher);
        }
    }


}
