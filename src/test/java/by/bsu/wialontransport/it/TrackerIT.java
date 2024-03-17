package by.bsu.wialontransport.it;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.controller.tracker.view.SaveTrackerView;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static by.bsu.wialontransport.util.HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME;
import static by.bsu.wialontransport.util.HttpUtil.createHttpEntity;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class TrackerIT extends AbstractSpringBootTest {
    private static final String URL = "/tracker";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void trackerShouldBeFoundById()
            throws Exception {
        final Long givenId = 255L;

        final String url = createUrlToFindTrackerById(givenId);
        final ResponseEntity<String> actual = restTemplate.getForEntity(url, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "id": 255,
                  "imei": "11112222333344445555",
                  "phoneNumber": "447336934"
                }""";
        assertEquals(expectedBody, actualBody, true);
    }

    @Test
    public void trackerShouldNotBeFoundById()
            throws Exception {
        final Long givenId = MAX_VALUE;

        final String url = createUrlToFindTrackerById(givenId);
        final ResponseEntity<String> actual = restTemplate.getForEntity(url, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_FOUND, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_FOUND",
                   "message": "Entity wasn't found",
                   "dateTime": "2024-03-17 11-47-11"
                }""";
        assertNotNull(actualBody);
        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldBeSaved()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .password("password")
                .phoneNumber("447336934")
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                """;
        JSONAssert.assertEquals(expectedBody, actualBody, true);
    }

    private static String createUrlToFindTrackerById(final Long id) {
        return URL + "/" + id;
    }
}
