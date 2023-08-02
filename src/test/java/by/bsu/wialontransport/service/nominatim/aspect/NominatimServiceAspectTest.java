//package by.bsu.wialontransport.service.nominatim.aspect;
//
//import by.bsu.wialontransport.configuration.GeoJsonConfiguration;
//import by.bsu.wialontransport.configuration.GeometryFactoryConfiguration;
//import by.bsu.wialontransport.configuration.RestTemplateConfiguration;
//import by.bsu.wialontransport.model.Coordinate;
//import by.bsu.wialontransport.service.nominatim.NominatimService;
//import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
//import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.Address;
//import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.client.RestTemplate;
//import org.wololo.jts2geojson.GeoJSONWriter;
//
//import java.io.IOException;
//import java.util.List;
//
//import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
//import static java.lang.String.format;
//import static java.lang.System.currentTimeMillis;
//import static java.util.stream.IntStream.range;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.client.ExpectedCount.times;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//
//@RunWith(SpringRunner.class)
//@Import({
//        RestTemplateConfiguration.class,
//        GeometryFactoryConfiguration.class,
//        GeoJsonConfiguration.class,
//        AnnotationAwareAspectJAutoProxyCreator.class,
//        NominatimServiceAspect.class
//})
//@RestClientTest(NominatimService.class)
//public final class NominatimServiceAspectTest {
//
//    @Value("${nominatim.millis-between-requests}")
//    private long millisBetweenRequests;
//
//    @Value("${nominatim.reverse.url.template}")
//    private String urlTemplate;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private NominatimService nominatimService;
//
//    @Autowired
//    private MockRestServiceServer server;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Autowired
//    private GeoJSONWriter geoJSONWriter;
//
//    @Test
//    public void requestsOfReversingShouldBeSentWithWaitsBetweenRequests()
//            throws Exception {
//        this.registerInterceptor();
//
//        final Geometry givenGeometry = createPolygon(
//                this.geometryFactory,
//                3, 4, 5, 6, 7, 8
//        );
//        final NominatimReverseResponse givenResponse = NominatimReverseResponse.builder()
//                .centerLatitude(4.4)
//                .centerLongitude(5.5)
//                .address(new Address("city", "country"))
//                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
//                .geometry(this.geoJSONWriter.write(givenGeometry))
//                .extraTags(new ExtraTags("place", "yes"))
//                .build();
//        final String givenResponseJson = this.objectMapper.writeValueAsString(givenResponse);
//
//        final int givenAmountOfRequests = 50;
//        final double givenLatitude = 5.5;
//        final double givenLongitude = 6.6;
//        final String expectedUrl = this.createUrl(givenLatitude, givenLongitude);
//        this.server.expect(
//                times(givenAmountOfRequests),
//                requestTo(expectedUrl)
//        ).andRespond(withSuccess(givenResponseJson, APPLICATION_JSON));
//
//        range(0, givenAmountOfRequests).mapToObj(
//                i -> isEven(i)
//                        ? this.nominatimService.reverse(givenLatitude, givenLongitude)
//                        : this.nominatimService.reverse(new Coordinate(givenLatitude, givenLongitude))
//        ).forEach(Assert::assertNotNull);
//    }
//
//    private void registerInterceptor() {
//        final TimeSendingRequestControllingInterceptor interceptor
//                = new TimeSendingRequestControllingInterceptor();
//        final List<ClientHttpRequestInterceptor> interceptors = this.restTemplate.getInterceptors();
//        interceptors.add(interceptor);
//    }
//
//    private String createUrl(final double latitude, final double longitude) {
//        return format(this.urlTemplate, latitude, longitude);
//    }
//
//    private static boolean isEven(final int research) {
//        return research % 2 == 0;
//    }
//
//    private final class TimeSendingRequestControllingInterceptor implements ClientHttpRequestInterceptor {
//        private long timeInMillisPreviousRequest;
//
//        public TimeSendingRequestControllingInterceptor() {
//            this.timeInMillisPreviousRequest = 0;
//        }
//
//        @Override
//        @SuppressWarnings("all")
//        public ClientHttpResponse intercept(final HttpRequest request,
//                                            final byte[] body,
//                                            final ClientHttpRequestExecution execution)
//                throws IOException {
//            final long currentTimeMillis = currentTimeMillis();
//            if (currentTimeMillis - this.timeInMillisPreviousRequest < millisBetweenRequests) {
//                throw new IllegalStateException("Duration between requests isn't respected.");
//            } else {
//                this.timeInMillisPreviousRequest = currentTimeMillis;
//            }
//            return execution.execute(request, body);
//        }
//    }
//}
