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

import java.util.List;

import static by.bsu.wialontransport.util.HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME;
import static by.bsu.wialontransport.util.HttpUtil.createHttpEntity;
import static java.lang.Long.MAX_VALUE;
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
                .phoneNumber("448535523")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "id": 1,
                  "imei": "99887766554433221100",
                  "phoneNumber": "448535523"
                }""";
        JSONAssert.assertEquals(expectedBody, actualBody, true);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfImeiIsNotValid()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("9988776655443322110")
                .password("password")
                .phoneNumber("448535523")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "imei : Invalid imei",
                   "dateTime": "2024-03-17 20-32-11"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPasswordIsNotValid()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .password("pa")
                .phoneNumber("448535523")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "httpStatus": "NOT_ACCEPTABLE",
                  "message": "password : Invalid password",
                  "dateTime": "2024-03-17 20-50-05"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPhoneNumberIsNotValid()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .password("password")
                .phoneNumber("44853552")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "phoneNumber : Invalid phone number",
                   "dateTime": "2024-03-17 20-52-58"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfUserIdIsNull()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .password("password")
                .phoneNumber("448535523")
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "userId : must not be null",
                   "dateTime": "2024-03-17 20-57-34"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfImeiAlreadyExist()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("448535523")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "imei : Imei should be unique",
                   "dateTime": "2024-03-21 08-18-25"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackerShouldNotBeSavedBecauseOfPhoneNumberAlreadyExist()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("99887766554433221100")
                .password("password")
                .phoneNumber("447336934")
                .userId(255L)
                .build();
        final HttpEntity<SaveTrackerView> givenHttpEntity = createHttpEntity(givenView);

        final ResponseEntity<String> actual = restTemplate.postForEntity(URL, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NOT_ACCEPTABLE, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "phoneNumber : Phone number should be unique",
                   "dateTime": "2024-03-21 08-22-26"
                }""";
        assertNotNull(actualBody);
        JSONAssert.assertEquals(expectedBody, actualBody, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void trackersShouldBeSaved()
            throws Exception {
        final List<SaveTrackerView> givenViews = List.of(
                SaveTrackerView.builder()
                        .imei("99887766554433221100")
                        .password("password")
                        .phoneNumber("448535523")
                        .userId(255L)
                        .build(),
                SaveTrackerView.builder()
                        .imei("99887766554433221101")
                        .password("password1")
                        .phoneNumber("448535524")
                        .userId(255L)
                        .build()
        );
        final HttpEntity<List<SaveTrackerView>> givenHttpEntity = createHttpEntity(givenViews);

        final String url = createUrlToSaveAll();
        final ResponseEntity<String> actual = restTemplate.postForEntity(url, givenHttpEntity, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                [
                   {
                     "id": 1,
                     "imei": "99887766554433221100",
                     "phoneNumber": "448535523"
                   },
                   {
                     "id": 2,
                     "imei": "99887766554433221101",
                     "phoneNumber": "448535524"
                   }
                ]""";
        JSONAssert.assertEquals(expectedBody, actualBody, true);
    }

    //TODO: continue test

    private static String createUrlToFindTrackerById(final Long id) {
        return URL + "/" + id;
    }

    private static String createUrlToSaveAll() {
        return URL + "/batch";
    }
}
