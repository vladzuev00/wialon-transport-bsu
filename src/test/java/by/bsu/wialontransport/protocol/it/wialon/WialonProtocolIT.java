package by.bsu.wialontransport.protocol.it.wialon;

import by.bsu.wialontransport.configuration.property.protocolserver.WialonProtocolServerConfiguration;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.protocol.it.core.ProtocolIT;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.entity.DataEntityUtil.checkEqualsExceptIdAndParameters;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

//TODO: checks in all tests data id
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

    private static final String SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#1\r\n";
    private static final String FAILED_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#-1\r\n";

    @Autowired
    private WialonProtocolServerConfiguration serverConfiguration;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

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
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithDefinedPropertiesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromNominatim()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;6054.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final NominatimReverseResponse givenReverseResponse = createNominatimReverseResponse();
        final var expectedCoordinate = new by.bsu.wialontransport.model.Coordinate(
                60.948055555555555,
                27.60972222222222
        );
        when(mockedNominatimService.reverse(eq(expectedCoordinate))).thenReturn(Optional.of(givenReverseResponse));

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
                .coordinate(new Coordinate(60.9480555555556, 27.6097222222222))
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
                .address(createAddress(1L))
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 1, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 60\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
    }

    @Test
    public void dataPackageWithNotDefinedDateShouldBeHandledButSkippedAsNotValid()
            throws Exception {
        final String givenRequestDataPackage = "#D#NA;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();

        final String actual = client.request(givenRequestDataPackage);
        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertFalse(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 0;
        assertEquals(expectedAllDataSize, actualAllDataSize);
    }

    @Test
    public void dataPackageWithNotDefinedTimeShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;NA;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 0, 0, 0))
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
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668470400, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedLatitudeShouldBeHandledButSkippedAsNotValid()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;NA;NA;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();

        final String actual = client.request(givenRequestDataPackage);
        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertFalse(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 0;
        assertEquals(expectedAllDataSize, actualAllDataSize);
    }

    @Test
    public void dataPackageWithNotDefinedLongitudeShouldBeHandledButSkippedAsNotValid()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;NA;NA;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();

        final String actual = client.request(givenRequestDataPackage);
        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertFalse(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 0;
        assertEquals(expectedAllDataSize, actualAllDataSize);
    }

    @Test
    public void dataPackageWithNotDefinedSpeedShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;NA;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
                .speed(0)
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
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 0\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedCourseShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;NA;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
                .speed(100)
                .course(0)
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
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 0, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedAltitudeShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;NA;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .altitude(0)
                .amountOfSatellites(177)
                .hdop(545.4554)
                .inputs(17)
                .outputs(18)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 0, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedAmountOfSatellitesShouldBeHandledButSkippedAsNotValid()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();

        final String actual = client.request(givenRequestDataPackage);
        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertFalse(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 0;
        assertEquals(expectedAllDataSize, actualAllDataSize);
    }

    @Test
    public void dataPackageWithNotDefinedHdopShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;NA;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .hdop(0)
                .inputs(17)
                .outputs(18)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 0\\.0, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedInputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;NA;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .inputs(0)
                .outputs(18)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 0, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedOutputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;NA;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .outputs(0)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 0, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithEmptyAnalogInputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + ";"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .analogInputs(new double[]{})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedAnalogInputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "NA;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .analogInputs(new double[]{})
                .driverKeyCode("keydrivercode")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithNotDefinedDriverKeyCodeShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "NA;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
                .driverKeyCode("not defined")
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"not defined\", "
                + "\"serializedParameters\": "
                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void dataPackageWithEmptyParametersShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
        checkEqualsExceptIdAndParameters(expectedData, actualData);

        final long actualParameterCount = countParameters();
        final long expectedParameterCount = 0;
        assertEquals(expectedParameterCount, actualParameterCount);

        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
                .country(0)
                .urban(0)
                .build();
        TrackerMileageEntityUtil.checkEqualsExceptId(expectedMileage, actualMileage);

        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", "
                + "\"serializedParameters\": "
                + "\"\\[]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void twoDataPackageWithDefinedPropertiesAndCoordinateInCityShouldBeHandledAndAddressesShouldBeReceivedFromDatabase()
            throws Exception {
        final String givenFirstRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";
        final String givenSecondRequestDataPackage = "#D#151122;145644;5354.177;N;02731.339;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenFirstRequestDataPackage);
        sendDataPackageExpectingSuccess(givenSecondRequestDataPackage);

        resetSavedDataConsumer(2);
        assertTrue(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 2;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualFirstData = actualAllData.get(0);
        final DataEntity expectedFirstData = DataEntity.builder()
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
        checkEqualsExceptIdAndParameters(expectedFirstData, actualFirstData);

        final DataEntity actualSecondData = actualAllData.get(1);
        final DataEntity expectedSecondData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 44))
                .coordinate(new Coordinate(53.9491666666667, 27.6108333333333))
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
        checkEqualsExceptIdAndParameters(expectedSecondData, actualSecondData);

        final long actualParameterCount = countParameters();
        final long expectedParameterCount = 10;
        assertEquals(expectedParameterCount, actualParameterCount);

        assertTrue(isMatchingParametersExist(new ParameterView("122", INTEGER, "5", actualFirstData)));
        assertTrue(isMatchingParametersExist(new ParameterView("123", DOUBLE, "6", actualFirstData)));
        assertTrue(isMatchingParametersExist(new ParameterView("124", DOUBLE, "7", actualFirstData)));
        assertTrue(isMatchingParametersExist(new ParameterView("par1", STRING, "str", actualFirstData)));
        assertTrue(isMatchingParametersExist(new ParameterView("116", DOUBLE, "0.5", actualFirstData)));
        assertTrue(isMatchingParametersExist(new ParameterView("122", INTEGER, "5", actualSecondData)));
        assertTrue(isMatchingParametersExist(new ParameterView("123", DOUBLE, "6", actualSecondData)));
        assertTrue(isMatchingParametersExist(new ParameterView("124", DOUBLE, "7", actualSecondData)));
        assertTrue(isMatchingParametersExist(new ParameterView("par1", STRING, "str", actualSecondData)));
        assertTrue(isMatchingParametersExist(new ParameterView("116", DOUBLE, "0.5", actualSecondData)));

        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
                .country(0)
                .urban(0.1435181328100863)
                .build();
        TrackerMileageEntityUtil.checkEqualsExceptId(expectedMileage, actualMileage);

        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\[\\"
                + "{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\) "
                + "\\|\\| "
                + "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, offset = \\d+, "
                + "CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
                + "\"addressId\": 102, \"epochSeconds\": 1668524204, \"latitude\": 53\\.94916666666666, "
                + "\"longitude\": 27\\.610833333333332, \"course\": 15, \"speed\": 100\\.0, \"altitude\": 10, "
                + "\"amountOfSatellites\": 177, \"hdop\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
                + "\"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\["
                + "\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    //TODO: test case when first coordinate is not city, second is city
    //TODO: test case when first coordinate is city, second is not city
    //TODO: test case when first coordinate is not city, second is not city(same address)
    //TODO: test case when first coordinate is not city, second is not city(different addresses)

    @Test
    public void firstDataPackageShouldBeHandledAndSecondDataPackageShouldNotBeHandledBecauseOfWrongDateTimeOrder()
            throws Exception {
        final String givenFirstRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";
        final String givenSecondRequestDataPackage = "#D#151122;145642;5354.177;N;02731.339;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenFirstRequestDataPackage);
        sendDataPackageExpectingSuccess(givenSecondRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

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
        checkEqualsExceptIdAndParameters(expectedData, actualData);

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
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
                + "key = 255, value = \\{\"id\": \\d+, \"addressId\": 102, \"epochSeconds\": 1668524203, "
                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\[\\"
                + "{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":\\d+},"
                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":\\d+}]\", "
                + "\"trackerId\": 255}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    @Test
    public void notValidDataPackageShouldNotBeHandled()
            throws Exception {
        //not valid date
        final String givenDataPackage = "#D#15112;145642;5354.177;N;02731.339;E;100;15;10;"
                + "177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        loginByExistingTracker();
        sendDataPackageExpectingFail(givenDataPackage);

        assertFalse(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingTrackerAndAddressOrderedById();
        assertTrue(actualAllData.isEmpty());
    }

    //TODO: test black box package
    //TODO: test case with a lot of package

    private void loginByExistingTracker()
            throws IOException {
        client.requestExpectingResponse(
                GIVEN_REQUEST_TO_LOGIN,
                SUCCESS_LOGIN_RESPONSE,
                "Login by existing tracker isn't success"
        );
    }

    private void sendDataPackageExpectingSuccess(final String request)
            throws IOException {
        client.requestExpectingResponse(
                request,
                SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE,
                "Sending data package isn't success"
        );
    }

    private void sendDataPackageExpectingFail(final String request)
            throws IOException {
        client.requestExpectingResponse(
                request,
                FAILED_RECEIVING_DATA_PACKAGE_RESPONSE,
                "Sending data package isn't failed"
        );
    }

    private NominatimReverseResponse createNominatimReverseResponse() {
        return NominatimReverseResponse.builder()
                .centerLatitude(5.5)
                .centerLongitude(6.6)
                .address(new NominatimReverseResponse.Address("city", "town", "country"))
                .boundingBoxCoordinates(new double[]{4.4, 5.5, 6.6, 7.7})
                .geometry(
                        geoJSONWriter.write(
                                createPolygon(
                                        geometryFactory,
                                        4.4, 5.5, 8.8, 5.5, 8.8, 9.9, 4.4, 9.9
                                )
                        )
                )
                .extraTags(new ExtraTags("city", "yes"))
                .build();
    }

    private static AddressEntity createAddress(final Long id) {
        return AddressEntity.builder()
                .id(id)
                .build();
    }
}
