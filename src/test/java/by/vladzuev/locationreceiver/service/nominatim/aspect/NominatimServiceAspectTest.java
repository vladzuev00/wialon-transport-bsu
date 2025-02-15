package by.vladzuev.locationreceiver.service.nominatim.aspect;

import by.vladzuev.locationreceiver.config.RestTemplateConfig;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.NominatimService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.IntStream.range;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@Import(
        {
                RestTemplateConfig.class,
                AnnotationAwareAspectJAutoProxyCreator.class,
                NominatimServiceAspect.class
        }
)
@RestClientTest(NominatimService.class)
public final class NominatimServiceAspectTest {

    @Value("${nominatim.millis-between-requests}")
    private long millisBetweenRequests;

    @Value("${nominatim.reverse.url.template}")
    private String urlTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NominatimService nominatimService;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void reverseRequestsShouldBeSentProtectingFrequentRequests() {
        final int givenRequestCount = 50;
        final GpsCoordinate givenCoordinate = new GpsCoordinate(5.5, 6.6);

        registerInterceptor();
        configureExpectation(givenRequestCount, givenCoordinate);

        doRequestsCheckingResponseNotNull(givenRequestCount, givenCoordinate);
    }

    private void registerInterceptor() {
        final TimeRequestControllingInterceptor interceptor = new TimeRequestControllingInterceptor();
        restTemplate.getInterceptors().add(interceptor);
    }

    @SuppressWarnings("SameParameterValue")
    private void doRequestsCheckingResponseNotNull(final int requestCount, final GpsCoordinate coordinate) {
        range(0, requestCount)
                .mapToObj(i -> nominatimService.reverse(coordinate))
                .forEach(Assert::assertNotNull);
    }

    @SuppressWarnings("SameParameterValue")
    private void configureExpectation(final int requestCount, final GpsCoordinate coordinate) {
        final String expectedUrl = createUrl(coordinate);
        server.expect(times(requestCount), requestTo(expectedUrl)).andRespond(withSuccess());
    }

    private String createUrl(final GpsCoordinate coordinate) {
        return format(urlTemplate, coordinate.getLatitude(), coordinate.getLongitude());
    }

    private final class TimeRequestControllingInterceptor implements ClientHttpRequestInterceptor {
        private long previousRequestTimeInMillis;

        @Override
        @SuppressWarnings("NullableProblems")
        public ClientHttpResponse intercept(final HttpRequest request,
                                            final byte[] body,
                                            final ClientHttpRequestExecution execution)
                throws IOException {
            final long requestExecutionTimeInMillis = currentTimeMillis();
            checkRequestExecutionTime(requestExecutionTimeInMillis);
            previousRequestTimeInMillis = requestExecutionTimeInMillis;
            return execution.execute(request, body);
        }

        private void checkRequestExecutionTime(final long executionTimeInMillis) {
            if (executionTimeInMillis - previousRequestTimeInMillis < millisBetweenRequests) {
                throw new IllegalStateException("Duration between requests isn't respected");
            }
        }
    }
}
