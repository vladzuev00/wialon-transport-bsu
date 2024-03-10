package by.bsu.wialontransport.it;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.skyscreamer.jsonassert.Customization.customization;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql("classpath:sql/tracker-online-it/before-test.sql")
@Sql(value = "classpath:sql/tracker-online-it/after-test.sql", executionPhase = AFTER_TEST_METHOD)
public class TrackerOnlineIT extends AbstractSpringBootTest {
    private static final String CONTROLLER_URL = "/trackerOnline";

    private static final String JSON_PROPERTY_PATH_LAST_DATA_DATE_TIME = "lastData.dateTime";
    public static final CustomComparator JSON_COMPARATOR_IGNORING_LAST_DATA_DATE_TIME = new CustomComparator(
            STRICT,
            customization(JSON_PROPERTY_PATH_LAST_DATA_DATE_TIME, (first, second) -> true)
    );

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void trackerShouldBeOnline()
            throws Exception {
        final Long givenTrackerId = 255L;

        final String url = createUrlToCheckOnline(givenTrackerId);
        final ResponseEntity<String> actual = restTemplate.getForEntity(url, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "trackerId": 255,
                  "lastData": {
                    "dateTime": "2024-03-10T14:14:46",
                    "coordinate": {
                      "latitude": 53.234,
                      "longitude": 27.3435
                    }
                  },
                  "status": "ONLINE"
                }""";
        assertNotNull(actualBody);
        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_LAST_DATA_DATE_TIME);
    }

    @Test
    public void trackerShouldBeOfflineBecauseOfExceedingThreshold()
            throws Exception {
        final Long givenTrackerId = 256L;

        final String url = createUrlToCheckOnline(givenTrackerId);
        final ResponseEntity<String> actual = restTemplate.getForEntity(url, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "trackerId": 256,
                  "lastData": {
                    "dateTime": "2024-03-10T14:14:46",
                    "coordinate": {
                      "latitude": 53.236,
                      "longitude": 27.3437
                    }
                  },
                  "status": "OFFLINE"
                }""";
        assertNotNull(actualBody);
        assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_LAST_DATA_DATE_TIME);
    }

    @Test
    public void trackerShouldBeOfflineBecauseOfNoDataFromThisTracker()
            throws Exception {
        final Long givenTrackerId = 257L;

        final String url = createUrlToCheckOnline(givenTrackerId);
        final ResponseEntity<String> actual = restTemplate.getForEntity(url, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "trackerId": 257,
                   "lastData": null,
                   "status": "OFFLINE"
                 }""";
        assertNotNull(actualBody);
        assertEquals(expectedBody, actualBody, true);
    }

    private static String createUrlToCheckOnline(final Long trackerId) {
        return CONTROLLER_URL + "/" + trackerId;
    }
}
