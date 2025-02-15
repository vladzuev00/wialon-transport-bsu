package by.vladzuev.locationreceiver.it;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.controller.tracker.view.SaveTrackerView;
import by.vladzuev.locationreceiver.util.HttpUtil;
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

import java.util.List;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
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
                  "imei": "11112222333344445555",
                  "phoneNumber": "447336934",
                  "id": 255
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
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldBeSaved()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("448535523")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "imei": "99887766554433221100",
                  "phoneNumber": "448535523",
                  "id": 1
                }""";
        JSONAssert.assertEquals(expectedBody, actualBody, true);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfImeiIsNull()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .phoneNumber("448535523")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Invalid imei",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfImeiIsNotValid()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("9988776655443322110")
                .phoneNumber("448535523")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Invalid imei",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfImeiShouldBeUnique()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("448535523")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Imei should be unique",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPhoneNumberIsNull()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Invalid phone number",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPhoneNumberIsNotValid()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("44853552")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Invalid phone number",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPhoneNumberShouldBeUnique()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("447336934")
                .password("password")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Phone number should be unique",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPasswordIsNull()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("448535523")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "httpStatus": "NOT_ACCEPTABLE",
                  "message": "Invalid password",
                  "dateTime": "2024-03-17 20-50-05"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPasswordIsNotValid()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("448535523")
                .password("pa")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "httpStatus": "NOT_ACCEPTABLE",
                  "message": "Invalid password",
                  "dateTime": "2024-03-17 20-50-05"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfUserIdIsNull()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("448535523")
                .password("password")
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "User with given id doesn't exist",
                   "dateTime": "2024-03-17 20-57-34"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfUserWithGivenIdNotExist()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .phoneNumber("448535523")
                .password("password")
                .userId(MIN_VALUE)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = HttpUtil.createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "User with given id doesn't exist",
                   "dateTime": "2024-03-17 20-57-34"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackersShouldBeSaved()
            throws Exception {
        final List<SaveTrackerView> givenViews = List.of(
                SaveTrackerView.builder()
                        .imei("99887766554433221100")
                        .phoneNumber("448535523")
                        .password("password")
                        .userId(255L)
                        .build(),
                SaveTrackerView.builder()
                        .imei("99887766554433221101")
                        .phoneNumber("448535524")
                        .password("password1")
                        .userId(255L)
                        .build()
        );
        final HttpEntity<List<SaveTrackerView>> givenHttpEntity = HttpUtil.createHttpEntity(givenViews);

        final String url = createUrlToSaveAll();
        final ResponseEntity<String> actual = restTemplate.postForEntity(url, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                [
                   {
                     "imei": "99887766554433221100",
                     "phoneNumber": "448535523",
                     "id": 1
                   },
                   {
                     "imei": "99887766554433221101",
                     "phoneNumber": "448535524",
                     "id": 2
                   }
                ]""";
        JSONAssert.assertEquals(expectedBody, actualBody, true);
    }

    //TODO: test failed cases save all and check unique property in inbound list

    private static String createUrlToFindTrackerById(final Long id) {
        return URL + "/" + id;
    }

    private static String createUrlToSaveAll() {
        return URL + "/batch";
    }
}
