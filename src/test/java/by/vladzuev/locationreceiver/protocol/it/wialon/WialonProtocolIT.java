//package by.bsu.wialontransport.protocol.it.wialon;
//
//import by.bsu.wialontransport.config.property.protocolserver.WialonProtocolServerConfig;
//import by.bsu.wialontransport.crud.entity.*;
//import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
//import by.bsu.wialontransport.protocol.it.core.ProtocolIT;
//import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
//import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
//import by.bsu.wialontransport.util.entity.DataEntityUtil;
//import by.bsu.wialontransport.util.entity.ParameterEntityUtil;
//import by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.wololo.jts2geojson.GeoJSONWriter;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static by.bsu.wialontransport.util.CollectionUtil.concat;
//import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
//import static java.util.Collections.emptyList;
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//public final class WialonProtocolIT extends ProtocolIT {
//    private static final TrackerEntity GIVEN_EXISTING_TRACKER = TrackerEntity.builder()
//            .id(255L)
//            .imei("11112222333344445555")
//            .password("password")
//            .build();
//
//    private static final String GIVEN_REQUEST_TO_LOGIN = "#L#%s;%s\r\n".formatted(
//            GIVEN_EXISTING_TRACKER.getImei(),
//            GIVEN_EXISTING_TRACKER.getPassword()
//    );
//    private static final String SUCCESS_LOGIN_RESPONSE = "#AL#1\r\n";
//    private static final String ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE = "#AL#01\r\n";
//    private static final String CONNECTION_FAILURE_LOGIN_RESPONSE = "#AL#0\r\n";
//
//    private static final String SUCCESS_PING_RESPONSE = "#AP#\r\n";
//
//    private static final String SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#1\r\n";
//    private static final String FAILED_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#-1\r\n";
//
//    @Autowired
//    private WialonProtocolServerConfig serverConfiguration;
//
//    private WialonClient client;
//
//    @Before
//    public void initializeClient()
//            throws Exception {
//        //TODO: refactor
//        client = new WialonClient(new InetSocketAddress(serverConfiguration.getHost(), serverConfiguration.getPort()));
//    }
//
//    @After
//    public void closeClient()
//            throws IOException {
//        client.close();
//    }
//
//    @Test
//    public void loginPackageShouldBeHandledWithSuccessAuthorizationStatus()
//            throws Exception {
//        final String actual = client.request(GIVEN_REQUEST_TO_LOGIN);
//        assertEquals(SUCCESS_LOGIN_RESPONSE, actual);
//    }
//
//    @Test
//    public void loginPackageShouldBeHandledWithErrorCheckPasswordStatus()
//            throws Exception {
//        final String givenRequest = "#L#%s;wrong_password\r\n".formatted(GIVEN_EXISTING_TRACKER.getImei());
//
//        final String actual = client.request(givenRequest);
//        assertEquals(ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE, actual);
//    }
//
//    @Test
//    public void loginPackageShouldBeHandledWithConnectionFailureStatus()
//            throws Exception {
//        final String givenRequest = "#L#00000000000000000000;password\r\n";
//
//        final String actual = client.request(givenRequest);
//        assertEquals(CONNECTION_FAILURE_LOGIN_RESPONSE, actual);
//    }
//
//    @Test
//    public void pingPackageShouldBeHandled()
//            throws Exception {
//        final String givenRequest = "#P#\r\n";
//
//        final String actual = client.request(givenRequest);
//        assertEquals(SUCCESS_PING_RESPONSE, actual);
//    }
//
//    @Test
//    public void dataPackageWithDefinedPropertiesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithDefinedPropertiesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromNominatim()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;6054.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final NominatimReverseResponse givenReverseResponse = createNominatimReverseResponse();
//        final var expectedCoordinate = new by.bsu.wialontransport.model.Coordinate(
//                60.948055555555555,
//                27.60972222222222
//        );
//        when(getMockedNominatimService().reverse(eq(expectedCoordinate))).thenReturn(Optional.of(givenReverseResponse));
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(60.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(createAddress(1L))
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
//        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 1, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 60\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedDateShouldBeHandledButSkippedAsNotValid()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#NA;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//
//        final String actual = client.request(givenRequestDataPackage);
//        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertFalse(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 0;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedTimeShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;NA;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 0, 0, 0))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668470400, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedLatitudeShouldBeHandledButSkippedAsNotValid()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;NA;NA;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//
//        final String actual = client.request(givenRequestDataPackage);
//        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertFalse(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 0;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedLongitudeShouldBeHandledButSkippedAsNotValid()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;NA;NA;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//
//        final String actual = client.request(givenRequestDataPackage);
//        assertEquals(SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE, actual);
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertFalse(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 0;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedSpeedShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;NA;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(0)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 0\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedCourseShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;NA;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(0)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 0, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedAltitudeShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;NA;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(0)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 0, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedAmountOfSatellitesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "NA;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(3)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 3, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedHdopShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;NA;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(0)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 0\\.0, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedInputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;NA;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(0)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 0, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedOutputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;NA;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(0)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 0, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithEmptyAnalogInputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + ";"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedAnalogInputsShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "NA;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithNotDefinedDriverKeyCodeShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "NA;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("not defined")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"not defined\", "
//                + "\"serializedParameters\": "
//                + "\"\\[\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void dataPackageWithEmptyParametersShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(emptyList())
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": "
//                + "\"\\[]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void twoDataPackageWithDefinedPropertiesAndCoordinateInCityShouldBeHandledAndAddressesShouldBeReceivedFromDatabase()
//            throws Exception {
//        final String givenFirstRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//        final String givenSecondRequestDataPackage = "#D#151122;145644;5354.177;N;02731.339;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenFirstRequestDataPackage);
//        sendDataPackageExpectingSuccess(givenSecondRequestDataPackage);
//
//        resetSavedDataConsumer(2);
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 2;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualFirstData = actualAllData.get(0);
//        final List<ParameterEntity> expectedFirstDataParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualFirstData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualFirstData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualFirstData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualFirstData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualFirstData)
//        );
//        final DataEntity expectedFirstData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedFirstDataParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedFirstData, actualFirstData);
//
//        final DataEntity actualSecondData = actualAllData.get(1);
//        final List<ParameterEntity> expectedSecondDataParameters = List.of(
//                new ParameterEntity(6L, "122", INTEGER, "5", actualSecondData),
//                new ParameterEntity(7L, "123", DOUBLE, "6", actualSecondData),
//                new ParameterEntity(8L, "124", DOUBLE, "7", actualSecondData),
//                new ParameterEntity(9L, "par1", STRING, "str", actualSecondData),
//                new ParameterEntity(10L, "116", DOUBLE, "0.5", actualSecondData)
//        );
//        final DataEntity expectedSecondData = DataEntity.builder()
//                .id(2L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 44))
//                .coordinate(new Coordinate(53.9491666666667, 27.6108333333333))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedSecondDataParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedSecondData, actualSecondData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
//        final List<ParameterEntity> expectedParameters = concat(expectedFirstDataParameters, expectedSecondDataParameters);
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0.1435181328100863)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\[\\"
//                + "{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\) "
//                + "\\|\\| "
//                + "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, offset = \\d+, "
//                + "CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": 2, "
//                + "\"addressId\": 102, \"epochSeconds\": 1668524204, \"latitude\": 53\\.94916666666666, "
//                + "\"longitude\": 27\\.610833333333332, \"course\": 15, \"speed\": 100\\.0, \"altitude\": 10, "
//                + "\"amountOfSatellites\": 177, \"hdop\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\["
//                + "\\{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":6},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":7},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":8},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":9},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":10}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    //TODO: test case when first coordinate is not city, second is city
//    //TODO: test case when first coordinate is city, second is not city
//    //TODO: test case when first coordinate is not city, second is not city(same address)
//    //TODO: test case when first coordinate is not city, second is not city(different addresses)
//
//    @Test
//    public void firstDataPackageShouldBeHandledAndSecondDataPackageShouldNotBeHandledBecauseOfWrongDateTimeOrder()
//            throws Exception {
//        final String givenFirstRequestDataPackage = "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//        final String givenSecondRequestDataPackage = "#D#151122;145642;5354.177;N;02731.339;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingSuccess(givenFirstRequestDataPackage);
//        sendDataPackageExpectingSuccess(givenSecondRequestDataPackage);
//
//        assertTrue(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        final int actualAllDataSize = actualAllData.size();
//        final int expectedAllDataSize = 1;
//        assertEquals(expectedAllDataSize, actualAllDataSize);
//
//        final DataEntity actualData = actualAllData.get(0);
//        final List<ParameterEntity> expectedParameters = List.of(
//                new ParameterEntity(1L, "122", INTEGER, "5", actualData),
//                new ParameterEntity(2L, "123", DOUBLE, "6", actualData),
//                new ParameterEntity(3L, "124", DOUBLE, "7", actualData),
//                new ParameterEntity(4L, "par1", STRING, "str", actualData),
//                new ParameterEntity(5L, "116", DOUBLE, "0.5", actualData)
//        );
//        final DataEntity expectedData = DataEntity.builder()
//                .id(1L)
//                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
//                .coordinate(new Coordinate(53.9480555555556, 27.6097222222222))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .hdop(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .parameters(expectedParameters)
//                .tracker(GIVEN_EXISTING_TRACKER)
//                .address(GIVEN_EXISTING_ADDRESS)
//                .build();
////        DataEntityUtil.checkEquals(expectedData, actualData);
//
//        final List<ParameterEntity> actualParameters = findParametersFetchingDataOrderedById();
////        ParameterEntityUtil.checkEquals(expectedParameters, actualParameters);
//
//        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
//        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
//                .id(1L)
//                .country(0)
//                .urban(0)
//                .build();
////        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);
//
//        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
//        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": 1, \"addressId\": 102, \"epochSeconds\": 1668524203, "
//                + "\"latitude\": 53\\.948055555555555, \"longitude\": 27\\.60972222222222, \"course\": 15, "
//                + "\"speed\": 100\\.0, \"altitude\": 10, \"amountOfSatellites\": 177, \"hdop\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"\\[5\\.5,4343\\.454544334,454\\.433,1\\.0]\", "
//                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\[\\"
//                + "{\\\\\"name\\\\\":\\\\\"122\\\\\",\\\\\"type\\\\\":\\\\\"INTEGER\\\\\",\\\\\"value\\\\\":\\\\\"5\\\\\",\\\\\"id\\\\\":1},"
//                + "\\{\\\\\"name\\\\\":\\\\\"123\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"6\\\\\",\\\\\"id\\\\\":2},"
//                + "\\{\\\\\"name\\\\\":\\\\\"124\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"7\\\\\",\\\\\"id\\\\\":3},"
//                + "\\{\\\\\"name\\\\\":\\\\\"par1\\\\\",\\\\\"type\\\\\":\\\\\"STRING\\\\\",\\\\\"value\\\\\":\\\\\"str\\\\\",\\\\\"id\\\\\":4},"
//                + "\\{\\\\\"name\\\\\":\\\\\"116\\\\\",\\\\\"type\\\\\":\\\\\"DOUBLE\\\\\",\\\\\"value\\\\\":\\\\\"0\\.5\\\\\",\\\\\"id\\\\\":5}]\", "
//                + "\"trackerId\": 255}\\)";
////        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));
//
//        verifyNoRequestsToNominatim();
//    }
//
//    @Test
//    public void notValidDataPackageShouldNotBeHandled()
//            throws Exception {
//        //not valid date
//        final String givenDataPackage = "#D#15112;145642;5354.177;N;02731.339;E;100;15;10;"
//                + "177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        loginByExistingTracker();
//        sendDataPackageExpectingFail(givenDataPackage);
//
//        assertFalse(isSuccessDataDelivering());
//
//        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
//        assertTrue(actualAllData.isEmpty());
//    }
//
//    //TODO: test black box package
//    //TODO: test case with a lot of package
//
//    private void loginByExistingTracker()
//            throws IOException {
//        client.requestExpectingResponse(
//                GIVEN_REQUEST_TO_LOGIN,
//                SUCCESS_LOGIN_RESPONSE,
//                "Login by existing tracker isn't success"
//        );
//    }
//
//    private void sendDataPackageExpectingSuccess(final String request)
//            throws IOException {
//        client.requestExpectingResponse(
//                request,
//                SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE,
//                "Sending data package isn't success"
//        );
//    }
//
//    private void sendDataPackageExpectingFail(final String request)
//            throws IOException {
//        client.requestExpectingResponse(
//                request,
//                FAILED_RECEIVING_DATA_PACKAGE_RESPONSE,
//                "Sending data package isn't failed"
//        );
//    }
//}
