package by.vladzuev.locationreceiver.controller.trackeronline;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.model.online.TrackerOnline;
import by.vladzuev.locationreceiver.model.online.TrackerOnline.LastData;
import by.vladzuev.locationreceiver.service.onlinechecking.TrackerOnlineService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static by.vladzuev.locationreceiver.model.online.OnlineStatus.ONLINE;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class TrackerOnlineControllerTest extends AbstractSpringBootTest {
    private static final String CONTROLLER_URL = "/trackerOnline";

    @MockBean
    private TrackerOnlineService mockedOnlineService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void onlineShouldBeChecked()
            throws Exception {
        final Long givenTrackerId = 255L;

        final TrackerOnline givenOnline = TrackerOnline.builder()
                .trackerId(givenTrackerId)
                .lastData(
                        new LastData(
                                LocalDateTime.of(2024, 10, 3, 14, 2, 3),
                                new GpsCoordinate(5.5, 6.6)
                        )
                )
                .status(ONLINE)
                .build();
        when(mockedOnlineService.check(eq(givenTrackerId))).thenReturn(givenOnline);

        final String url = createUrlToCheckOnline(givenTrackerId);
        final ResponseEntity<String> actual = restTemplate.getForEntity(url, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "trackerId": 255,
                  "lastData": {
                    "dateTime": "2024-10-03T14:02:03",
                    "coordinate": {
                      "latitude": 5.5,
                      "longitude": 6.6
                    }
                  },
                  "status": "ONLINE"
                }""";
        assertEquals(expectedBody, actualBody, true);
    }

    private static String createUrlToCheckOnline(final Long trackerId) {
        return CONTROLLER_URL + "/" + trackerId;
    }
}
