package by.bsu.wialontransport.protocol.it.wialon;

import by.bsu.wialontransport.configuration.property.protocolserver.WialonProtocolServerConfiguration;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.protocol.it.core.ProtocolIT;
import by.bsu.wialontransport.service.geocoding.service.NominatimGeocodingService;
import by.bsu.wialontransport.util.entity.DataEntityUtil;
import by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;

public final class WialonProtocolIT extends ProtocolIT {
    private static final TrackerEntity GIVEN_EXISTING_TRACKER = TrackerEntity.builder()
            .id(255L)
            .imei("11112222333344445555")
            .password("password")
            .build();

    private static final String GIVEN_REQUEST_TO_LOGIN = "#L#%s;%s\r\n".formatted(
            GIVEN_EXISTING_TRACKER.getImei(),
            GIVEN_EXISTING_TRACKER.getPassword()
    );
    private static final String SUCCESS_LOGIN_RESPONSE = "#AL#1\r\n";
    private static final String ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE = "#AL#01\r\n";
    private static final String CONNECTION_FAILURE_LOGIN_RESPONSE = "#AL#0\r\n";

    private static final String SUCCESS_PING_RESPONSE = "#AP#\r\n";

    private static final String GIVEN_VALID_REQUEST_DATA_PACKAGE = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
            + "177;545.4554;17;18;"
            + "5.5,4343.454544334,454.433,1;"
            + "keydrivercode;"
            + "122:1:5,123:2:6,124:2:7,"
            + "par1:3:str,116:2:0.5"
            + "\r\n";
    private static final String SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#1\r\n";
    private static final String FAILED_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#-1\r\n";

    @Autowired
    private WialonProtocolServerConfiguration serverConfiguration;

    @MockBean
    private NominatimGeocodingService mockedNominatimGeocodingService;

    private WialonClient client;

    @Before
    public void initializeClient()
            throws Exception {
        client = new WialonClient(serverConfiguration.getInetSocketAddress());
    }

    @After
    public void closeClient()
            throws IOException {
        client.close();
    }

    @Test
    public void loginPackageShouldBeHandledWithSuccessAuthorizationStatus()
            throws Exception {
        final String actual = client.request(GIVEN_REQUEST_TO_LOGIN);
        assertEquals(SUCCESS_LOGIN_RESPONSE, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithErrorCheckPasswordStatus()
            throws Exception {
        final String givenRequest = "#L#%s;wrong_password\r\n".formatted(GIVEN_EXISTING_TRACKER.getImei());

        final String actual = client.request(givenRequest);
        assertEquals(ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithConnectionFailureStatus()
            throws Exception {
        final String givenRequest = "#L#00000000000000000000;password\r\n";

        final String actual = client.request(givenRequest);
        assertEquals(CONNECTION_FAILURE_LOGIN_RESPONSE, actual);
    }

    @Test
    public void pingPackageShouldBeHandled()
            throws Exception {
        final String givenRequest = "#P#\r\n";

        final String actual = client.request(givenRequest);
        assertEquals(SUCCESS_PING_RESPONSE, actual);
    }

    @Test
    public void dataPackageWithDefinedPropertiesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        loginByExistingTracker();
        sendValidRequestDataPackage();

        assertTrue(waitDataDeliveringAndReturnDeliveredOrNot());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
                .speed(100)
                .course(15)
                .altitude(10)
                .amountOfSatellites(177)
                .hdop(545.4554)
                .inputs(17)
                .outputs(18)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        DataEntityUtil.checkEqualsExceptIdAndParameters(expectedData, actualData);

        final long actualParameterCount = countParameters();
        final long expectedParameterCount = 5;
        assertEquals(expectedParameterCount, actualParameterCount);

        assertTrue(isMatchingParametersExist(new ParameterView("122", INTEGER, "5", actualData)));
        assertTrue(isMatchingParametersExist(new ParameterView("123", DOUBLE, "6", actualData)));
        assertTrue(isMatchingParametersExist(new ParameterView("124", DOUBLE, "7", actualData)));
        assertTrue(isMatchingParametersExist(new ParameterView("par1", STRING, "str", actualData)));
        assertTrue(isMatchingParametersExist(new ParameterView("116", DOUBLE, "0.5", actualData)));

        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
                .country(0)
                .urban(0)
                .build();
        TrackerMileageEntityUtil.checkEqualsExceptId(expectedMileage, actualMileage);

        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
        final String expectedKafkaSavedDataConsumerPayload = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\"name\":\"122\",\"type\":\"INTEGER\",\"value\":\"5\",\"id\":\\d+},"
                + "\\{\"name\":\"123\",\"type\":\"DOUBLE\",\"value\":\"6\",\"id\":\\d+},"
                + "\\{\"name\":\"124\",\"type\":\"DOUBLE\",\"value\":\"7\",\"id\":\\d+},"
                + "\\{\"name\":\"par1\",\"type\":\"STRING\",\"value\":\"str\",\"id\":\\d+},"
                + "\\{\"name\":\"116\",\"type\":\"DOUBLE\",\"value\":\"0\\.5\",\"id\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayload));

        verifyNoInteractions(mockedNominatimGeocodingService);
    }

    private void loginByExistingTracker()
            throws IOException {
        client.requestExpectingResponse(
                GIVEN_REQUEST_TO_LOGIN,
                SUCCESS_LOGIN_RESPONSE,
                "Login by existing tracker is failed"
        );
    }

    private void sendValidRequestDataPackage()
            throws IOException {
        client.requestExpectingResponse(
                GIVEN_VALID_REQUEST_DATA_PACKAGE,
                SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE,
                "Sending data package is failed"
        );
    }
}
