//package by.bsu.wialontransport.protocol.wialon.it;
//
//import by.bsu.wialontransport.base.kafka.AbstractKafkaContainerTest;
//import by.bsu.wialontransport.base.kafka.TestKafkaSavedDataConsumer;
//import by.bsu.wialontransport.configuration.property.WialonServerConfiguration;
//import by.bsu.wialontransport.crud.entity.AddressEntity;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.crud.entity.DataEntity.Latitude;
//import by.bsu.wialontransport.crud.entity.DataEntity.Longitude;
//import by.bsu.wialontransport.crud.entity.ParameterEntity;
//import by.bsu.wialontransport.crud.entity.TrackerEntity;
//import by.bsu.wialontransport.protocol.wialon.server.WialonServer;
//import by.bsu.wialontransport.protocol.wialon.server.factory.WialonServerFactory;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Future;
//import java.util.stream.IntStream;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
//import static java.lang.String.format;
//import static java.nio.charset.StandardCharsets.UTF_8;
//import static java.util.concurrent.Executors.newSingleThreadExecutor;
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static java.util.stream.IntStream.range;
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.http.HttpEntity.EMPTY;
//import static org.springframework.http.HttpMethod.GET;
//import static org.springframework.http.ResponseEntity.ok;
//import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
//import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
//
//public class InboundPackageHandlingIT extends AbstractKafkaContainerTest {
//    private static boolean serverWasRan = false;
//
//    private static final Long GIVEN_EXISTING_TRACKER_ID = 255L;
//    private static final String GIVEN_EXISTING_TRACKER_IMEI = "11112222333344445555";
//    private static final String GIVEN_EXISTING_TRACKER_PASSWORD = "password";
//    private static final String GIVEN_REQUEST_TO_LOGIN = "#L#" + GIVEN_EXISTING_TRACKER_IMEI + ";"
//            + GIVEN_EXISTING_TRACKER_PASSWORD + "\r\n";
//
//    private static final String SUCCESS_RESPONSE_LOGIN = "#AL#1\r\n";
//
//    private static final String MESSAGE_EXCEPTION_FAILED_LOGIN = "Login is failed.";
//
//    private static final int WAIT_MESSAGE_DELIVERING_IN_SECONDS = 6;
//
//    private static final String HQL_QUERY_TO_FIND_ALL_DATA = "SELECT e FROM DataEntity e";
//
//    private static final String HQL_QUERY_TO_FIND_PARAMETERS_AMOUNT = "SELECT COUNT(e) FROM ParameterEntity e";
//
//    private static final String HQL_QUERY_TO_FIND_COUNT_PARAMETERS_WITH_GIVEN_PROPERTIES
//            = "SELECT COUNT(e) FROM ParameterEntity e "
//            + "WHERE e.name = :name AND e.type = :type AND e.value = :value AND e.data = :data";
//    private static final String NAME_NAMED_PARAMETER_PARAMETER_NAME = "name";
//    private static final String NAME_NAMED_PARAMETER_PARAMETER_TYPE = "type";
//    private static final String NAME_NAMED_PARAMETER_PARAMETER_VALUE = "value";
//    private static final String NAME_NAMED_PARAMETER_PARAMETER_DATA = "data";
//
//    private static final String GIVEN_VALID_REQUEST_DATA_PACKAGE =
//            "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                    + "5.5,4343.454544334,454.433,1;"
//                    + "keydrivercode;"
//                    //HDOP, VDOP, PDOP
//                    + "122:2:5,123:2:6,124:2:7,"
//                    + "par1:3:str,116:2:0.5"
//                    + "\r\n";
//    private static final String SUCCESS_RESPONSE_DATA_PACKAGE = "#AD#1\r\n";
//    private static final String FAILED_RESPONSE_DATA_PACKAGE = "#AD#-1\r\n";
//
//    private static final String TEMPLATE_RESPONSE_BLACK_BOX_PACKAGE = "#AB#%d\r\n";
//
//    @Autowired
//    private WialonServerConfiguration serverConfiguration;
//
//    @Autowired
//    private WialonServerFactory serverFactory;
//
//    @MockBean
//    private RestTemplate mockedRestTemplate;
//
//    @Autowired
//    private TestKafkaSavedDataConsumer savedDataConsumer;
//
//    private Client client;
//
//    @Before
//    public void startServerAndClient()
//            throws Exception {
//        this.runServerIfWasNotRun();
//        this.client = new Client(this.serverConfiguration.getHost(), this.serverConfiguration.getPort());
//    }
//
//    @After
//    public void closeClient()
//            throws IOException {
//        this.client.close();
//    }
//
//    @After
//    public void resetSavedDataConsumer() {
//        this.savedDataConsumer.reset();
//    }
//
//    @Test
//    public void loginPackageShouldBeHandledWithSuccessAuthorizationStatus()
//            throws Exception {
//        final String actual = this.client.doRequest(GIVEN_REQUEST_TO_LOGIN)
//                .get();
//        assertEquals(SUCCESS_RESPONSE_LOGIN, actual);
//    }
//
//    @Test
//    public void loginPackageShouldBeHandledWithErrorCheckPasswordStatus()
//            throws Exception {
//        final String givenRequest = "#L#" + GIVEN_EXISTING_TRACKER_IMEI + ";wrong_password\r\n";
//
//        final String actual = this.client.doRequest(givenRequest)
//                .get();
//        final String expected = "#AL#01\r\n";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void loginPackageShouldBeHandledWithConnectionFailureStatus()
//            throws Exception {
//        final String givenRequest = "#L#00000000000000000000;wrong_password\r\n";
//
//        final String actual = this.client.doRequest(givenRequest)
//                .get();
//        final String expected = "#AL#0\r\n";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void pingPackageShouldBeHandled()
//            throws Exception {
//        final String givenRequest = "#P#\r\n";
//
//        final String actual = this.client.doRequest(givenRequest)
//                .get();
//        final String expected = "#AP#\r\n";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeHandledAsValidWithoutFixingInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
//            throws Exception {
//        this.login();
//        this.sendValidRequestDataPackage();
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(1, dataFromDatabase.size());
//
//        final DataEntity actualSavedData = dataFromDatabase.get(0);
//        final DataEntity expectedSavedData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 43))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(createTracker(GIVEN_EXISTING_TRACKER_ID))
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", actualSavedData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524203, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, "
//                + "\"speed\": 100, \"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, "
//                + "\"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str,\\d+:116:2:0\\.5\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeHandledAsValidWithoutFixingInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromAPI()
//            throws Exception {
//        this.login();
//
//        when(this.mockedRestTemplate.exchange(
//                        anyString(),
//                        same(GET),
//                        same(EMPTY),
//                        any(ParameterizedTypeReference.class)
//                )
//        ).thenReturn(
//                ok(
//                        createNominatimResponse(
//                                57.406944,
//                                37.54833,
//                                "city",
//                                "country",
//                                new double[]{55.506944, 59.506944, 37.54833, 41.54833}
//                        )
//                )
//        );
//
//        this.sendValidRequestDataPackage();
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(1, dataFromDatabase.size());
//
//        final DataEntity actualSavedData = dataFromDatabase.get(0);
//        final DataEntity expectedSavedData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 43))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(createTracker(GIVEN_EXISTING_TRACKER_ID))
//                .address(createAddress(1L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", actualSavedData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", actualSavedData));
//
//        verify(this.mockedRestTemplate, times(1))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": \\d+, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, "
//                + "\"speed\": 100, \"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, "
//                + "\"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str,\\d+:116:2:0\\.5\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfAmountOfSatellitesIsLessThanMinimalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;2;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;1000;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfDateTimeIsLessThanMinimalAllowable()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151105;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfDateTimeIsMoreThanMaximalAllowable()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151199;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterDoesNotExistAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //VDOP, PDOP
//                + "123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterIsLessThanMinimalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:0.1,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterIsMoreThanMaximalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:8,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterIsNotDoubleAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:1:5,123:2:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfVDOPParameterDoesNotExistAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, PDOP
//                + "122:2:5,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfVDOPIsLessThanMinimalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:0,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfVDOPIsMoreThanMaximalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:8,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfVDOPIsNotDoubleAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:1:6,124:2:7,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfPDOPParameterDoesNotExistAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP
//                + "122:2:5,123:2:6,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfPDOPParameterIsLessThanMinimalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:2:0,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfPDOPParameterIsMoreThanMaximalAllowableAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:2:8,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    public void dataPackageShouldBeSkippedBecauseOfPDOPParameterIsNotDoubleAndThereIsNoPreviousValidDataToFix()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:5,123:2:6,124:1:8,"
//                + "par1:3:str,116:2:0.5"
//                + "\r\n";
//
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertTrue(dataFromDatabase.isEmpty());
//
//        assertEquals(0, this.findAmountOfParametersInDataBase());
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeHandledAsValidWithoutFixingInCaseExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, "
//                + "\"latitudeMinutes\": 44, \"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, "
//                + "\"longitudeDegrees\": 37, \"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, "
//                + "\"longitudeTypeValue\": 69, \"speed\": 100, \"course\": 15, \"altitude\": 10, "
//                + "\"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeHandledAsValidWithFixingBecauseOfAmountOfSatellitesIsLessThanMinimalAllowableInCaseExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;2;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeHandledAsValidWithFixingBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowableInCaseExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;1000;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, "
//                + "\"speed\": 100, \"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, "
//                + "\"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", \"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeSkippedBecauseOfDateTimeIsLessThanMinimalAllowableInCaseExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151100;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(1, dataFromDatabase.size());
//
//        final DataEntity previousData = dataFromDatabase.get(0);
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeSkippedBecauseOfDateTimeIsMoreThanMaximalAllowableInCaseExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151199;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(1, dataFromDatabase.size());
//
//        final DataEntity previousData = dataFromDatabase.get(0);
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfHDOPParameterDoesNotExistInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //VDOP, PDOP
//                + "123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfHDOPParameterIsLessThanMinimalAllowableInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:0,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, "
//                + "\"latitudeDegrees\": 55, \"latitudeMinutes\": 44, \"latitudeMinuteShare\": 6025, "
//                + "\"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, \"longitudeMinutes\": 39, "
//                + "\"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, "
//                + "\"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfHDOPParameterIsMoreThanMaximalAllowableInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:8,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, "
//                + "\"latitudeMinutes\": 44, \"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, "
//                + "\"longitudeDegrees\": 37, \"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, "
//                + "\"longitudeTypeValue\": 69, \"speed\": 100, \"course\": 15, \"altitude\": 10, "
//                + "\"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfHDOPParameterIsNotDoubleInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:1:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        waitDataDeliveringAndReturnDeliveredOrNot();
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfPDOPParameterDoesNotExistInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP
//                + "122:2:4,123:2:5,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfPDOPParameterIsLessThanMinimalAllowableInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:0,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfPDOPParameterIsMoreThanMaximalAllowableInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:8,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void dataPackageShouldBeFixedAndSavedBecauseOfPDOPParameterIsNotDoubleInCaseExistingPreviousValidData()
//            throws Exception {
//        this.login();
//
//        this.sendValidRequestDataPackage();
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//        this.savedDataConsumer.reset();
//
//        final String givenRequest = "#D#151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:1:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(SUCCESS_RESPONSE_DATA_PACKAGE, response);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(2, dataFromDatabase.size());
//
//        final DataEntity previousData = findOldestData(dataFromDatabase);
//        final DataEntity actualSavedNewestData = findNewestData(dataFromDatabase);
//        final DataEntity expectedSavedNewestData = DataEntity.builder()
//                .date(LocalDate.of(2022, 11, 15))
//                .time(LocalTime.of(14, 56, 44))
//                .latitude(createLatitude(55, 44, 6025, NORTH))
//                .longitude(createLongitude(37, 39, 6834, EAST))
//                .speed(100)
//                .course(15)
//                .altitude(10)
//                .amountOfSatellites(177)
//                .reductionPrecision(545.4554)
//                .inputs(17)
//                .outputs(18)
//                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                .driverKeyCode("keydrivercode")
//                .tracker(this.findGivenExistingTracker())
//                .address(createAddress(258L))
//                .build();
//        checkEqualsExceptIdAndParameters(expectedSavedNewestData, actualSavedNewestData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedNewestData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", previousData));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedNewestData));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, "
//                + "value = \\{\"id\": \\d+, \"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6025, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6834, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:5,\\d+:123:2:6,\\d+:124:2:7,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    public void dataPackageShouldNotBeHandledBecauseOfRequestDoesNotMatchRegex()
//            throws Exception {
//        this.login();
//
//        //there is no date
//        final String givenRequest = "#D#;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:1:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String response = this.client.doRequest(givenRequest).get();
//        assertEquals(FAILED_RESPONSE_DATA_PACKAGE, response);
//
//        assertFalse(waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
//        assertEquals(0, dataFromDatabase.size());
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void blackBoxPackageShouldBeHandledSuccessfullyInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 44))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build(),
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(1)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,"
//                + "\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", \"trackerId\": 255}\\) \\|\\| "
//                + "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, offset = \\d+, "
//                + "CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", \"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,"
//                + "\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", \"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfDateTimeIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151109;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfDateTimeIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151199;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfAmountOfSatellitesIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;2;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;1000;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfHDOPParameterDoesNotExistInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //VDOP, PDOP
//                + "123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfHDOPParameterIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:0,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfHDOPParameterIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:8,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfTypeOfHDOPParameterIsNotDoubleInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:1:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfVDOPParameterDoesNotExistInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, PDOP
//                + "122:1:4,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfVDOPParameterIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:0,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfVDOPParameterIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:8,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfTypeOfVDOPParameterIsNotDoubleInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:1:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfPDOPParameterDoesNotExistInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP
//                + "122:2:4,123:2:5,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfPDOPParameterIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:0,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfPDOPParameterIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:8,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void firstDataShouldBeSkippedInBlackBoxPackageBecauseOfPDOPParameterIsNotDoubleInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:1:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6027, NORTH))
//                        .longitude(createLongitude(37, 39, 6836, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6027, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6836, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void secondDataShouldBeSkippedInBlackBoxPackageBecauseOfDateTimeIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151100;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 44))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void secondDataShouldBeSkippedInBlackBoxPackageBecauseOfDateTimeIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151199;145645;5544.6027;N;03739.6836;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 44))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(5, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, \"longitudeTypeValue\": 69, \"speed\": 100, "
//                + "\"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, "
//                + "\"inputs\": 17, \"outputs\": 18, \"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void secondDataShouldBeFixedInBlackBoxPackageBecauseOfAmountOfSatellitesIsLessThanMinimalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;2;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 44))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build(),
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(1)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, \"longitudeTypeValue\": 69, "
//                + "\"speed\": 100, \"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, "
//                + "\"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\) \\|\\| ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": \\d+, \"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, "
//                + "\"latitudeMinutes\": 44, \"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, "
//                + "\"longitudeDegrees\": 37, \"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, "
//                + "\"longitudeTypeValue\": 69, \"speed\": 100, \"course\": 15, \"altitude\": 10, "
//                + "\"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    @Transactional(propagation = NOT_SUPPORTED)
//    @Sql(statements = "INSERT INTO addresses"
//            + "(id, bounding_box, center, city_name, country_name) "
//            + "VALUES(258, "
//            + "ST_GeomFromText('POLYGON((37.54833 55.406944, 41.54833 55.406944, 41.54833 59.406944, 37.54833 59.406944, 37.54833 55.406944))', 4326), "
//            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
//    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "DELETE FROM addresses", executionPhase = AFTER_TEST_METHOD)
//    @Sql(statements = "ALTER SEQUENCE addresses_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
//    public void secondDataShouldBeFixedInBlackBoxPackageBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowableInCaseNotExistingPreviousData()
//            throws Exception {
//        this.login();
//
//        final String givenRequest = "#B#"
//                + "151122;145644;5544.6026;N;03739.6835;E;100;15;10;177;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "|"
//                + "151122;145645;5544.6027;N;03739.6836;E;100;15;10;1000;545.4554;17;18;"
//                + "5.5,4343.454544334,454.433,1;"
//                + "keydrivercode;"
//                //HDOP, VDOP, PDOP
//                + "122:2:4,123:2:5,124:2:6,"
//                + "par1:3:str2,116:2:0.4"
//                + "\r\n";
//        final String actualResponse = this.client.doRequest(givenRequest).get();
//        final String expectedResponse = createResponseBlackBoxPackage(2);
//        assertEquals(expectedResponse, actualResponse);
//
//        assertTrue(this.waitDataDeliveringAndReturnDeliveredOrNot());
//
//        final List<DataEntity> actualSavedData = this.findAllDataFromDataBase();
//        final List<DataEntity> expectedSavedData = List.of(
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 44))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build(),
//                DataEntity.builder()
//                        .date(LocalDate.of(2022, 11, 15))
//                        .time(LocalTime.of(14, 56, 45))
//                        .latitude(createLatitude(55, 44, 6026, NORTH))
//                        .longitude(createLongitude(37, 39, 6835, EAST))
//                        .speed(100)
//                        .course(15)
//                        .altitude(10)
//                        .amountOfSatellites(177)
//                        .reductionPrecision(545.4554)
//                        .inputs(17)
//                        .outputs(18)
//                        .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
//                        .driverKeyCode("keydrivercode")
//                        .tracker(this.findGivenExistingTracker())
//                        .address(createAddress(258L))
//                        .build()
//        );
//        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);
//
//        assertEquals(10, this.findAmountOfParametersInDataBase());
//
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "4", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "5", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "6", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str2", actualSavedData.get(1)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(0)));
//        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.4", actualSavedData.get(1)));
//
//        verify(this.mockedRestTemplate, times(0))
//                .exchange(anyString(), same(GET), same(EMPTY), any(ParameterizedTypeReference.class));
//
//        final String actualPayload = this.savedDataConsumer.getPayload();
//        final String expectedPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, leaderEpoch = \\d+, "
//                + "offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
//                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 255, value = \\{\"id\": \\d+, "
//                + "\"epochSeconds\": 1668524204, \"latitudeDegrees\": 55, \"latitudeMinutes\": 44, "
//                + "\"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, \"longitudeDegrees\": 37, "
//                + "\"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, \"longitudeTypeValue\": 69, "
//                + "\"speed\": 100, \"course\": 15, \"altitude\": 10, \"amountOfSatellites\": 177, "
//                + "\"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\) \\|\\| ConsumerRecord\\(topic = saved-data, partition = \\d+, "
//                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, "
//                + "serialized value size = \\d+, headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), "
//                + "key = 255, value = \\{\"id\": \\d+, \"epochSeconds\": 1668524205, \"latitudeDegrees\": 55, "
//                + "\"latitudeMinutes\": 44, \"latitudeMinuteShare\": 6026, \"latitudeTypeValue\": 78, "
//                + "\"longitudeDegrees\": 37, \"longitudeMinutes\": 39, \"longitudeMinuteShare\": 6835, "
//                + "\"longitudeTypeValue\": 69, \"speed\": 100, \"course\": 15, \"altitude\": 10, "
//                + "\"amountOfSatellites\": 177, \"reductionPrecision\": 545\\.4554, \"inputs\": 17, \"outputs\": 18, "
//                + "\"serializedAnalogInputs\": \"5\\.5,4343\\.454544334,454\\.433,1\\.0\", "
//                + "\"driverKeyCode\": \"keydrivercode\", "
//                + "\"serializedParameters\": \"\\d+:122:2:4,\\d+:123:2:5,\\d+:124:2:6,\\d+:par1:3:str2,\\d+:116:2:0\\.4\", "
//                + "\"trackerId\": 255}\\)";
//        assertTrue(actualPayload.matches(expectedPayloadRegex));
//    }
//
//    private void runServerIfWasNotRun()
//            throws InterruptedException {
//        if (!serverWasRan) {
//            new Thread(() -> {
//                try (final WialonServer server = this.serverFactory.create()) {
//                    server.run();
//                }
//            }).start();
//            SECONDS.sleep(1);  //to give thread starting server time to run it
//            serverWasRan = true;
//        }
//    }
//
//    private void login()
//            throws ExecutionException, InterruptedException {
//        final String response = this.client.doRequest(GIVEN_REQUEST_TO_LOGIN).get();
//        if (!SUCCESS_RESPONSE_LOGIN.equals(response)) {
//            throw new IllegalStateException(MESSAGE_EXCEPTION_FAILED_LOGIN);
//        }
//    }
//
//    private boolean waitDataDeliveringAndReturnDeliveredOrNot()
//            throws InterruptedException {
//        return this.savedDataConsumer
//                .getCountDownLatch()
//                .await(WAIT_MESSAGE_DELIVERING_IN_SECONDS, SECONDS);
//    }
//
//    private static Latitude createLatitude(final int degrees, final int minutes, final int minuteShare,
//                                           final Latitude.Type type) {
//        return Latitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static Longitude createLongitude(final int degrees, final int minutes, final int minuteShare,
//                                             final Longitude.Type type) {
//        return Longitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static void checkEqualsExceptIdAndParameters(final List<DataEntity> expected,
//                                                         final List<DataEntity> actual) {
//        assertEquals(expected.size(), actual.size());
//        range(0, expected.size()).forEach(i -> checkEqualsExceptIdAndParameters(expected.get(i), actual.get(i)));
//    }
//
//    private static void checkEqualsExceptIdAndParameters(final DataEntity expected, final DataEntity actual) {
//        assertEquals(expected.getDate(), actual.getDate());
//        assertEquals(expected.getTime(), actual.getTime());
//        assertEquals(expected.getLatitude(), actual.getLatitude());
//        assertEquals(expected.getLongitude(), actual.getLongitude());
//        assertEquals(expected.getSpeed(), actual.getSpeed());
//        assertEquals(expected.getCourse(), actual.getCourse());
//        assertEquals(expected.getAltitude(), actual.getAltitude());
//        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
//        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
//        assertEquals(expected.getInputs(), actual.getInputs());
//        assertEquals(expected.getOutputs(), actual.getOutputs());
//        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
//        assertEquals(expected.getTracker().getId(), actual.getTracker().getId());
//        assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
//    }
//
//    private List<DataEntity> findAllDataFromDataBase() {
//        return super.entityManager.createQuery(HQL_QUERY_TO_FIND_ALL_DATA, DataEntity.class)
//                .getResultList();
//    }
//
//    private long findAmountOfParametersInDataBase() {
//        return super.entityManager.createQuery(HQL_QUERY_TO_FIND_PARAMETERS_AMOUNT, Long.class)
//                .getSingleResult();
//    }
//
//    private boolean isParameterWithGivenPropertiesExistsInDataBase(String name, ParameterEntity.Type type,
//                                                                   String value, DataEntity data) {
//        return findCountOfParametersWithGivenProperties(name, type, value, data) >= 1;
//    }
//
//    private long findCountOfParametersWithGivenProperties(String name, ParameterEntity.Type type,
//                                                          String value, DataEntity data) {
//        return super.entityManager.createQuery(HQL_QUERY_TO_FIND_COUNT_PARAMETERS_WITH_GIVEN_PROPERTIES, Long.class)
//                .setParameter(NAME_NAMED_PARAMETER_PARAMETER_NAME, name)
//                .setParameter(NAME_NAMED_PARAMETER_PARAMETER_TYPE, type)
//                .setParameter(NAME_NAMED_PARAMETER_PARAMETER_VALUE, value)
//                .setParameter(NAME_NAMED_PARAMETER_PARAMETER_DATA, data)
//                .getSingleResult();
//    }
//
//    private void sendValidRequestDataPackage()
//            throws ExecutionException, InterruptedException {
//        final String response = this.client.doRequest(GIVEN_VALID_REQUEST_DATA_PACKAGE).get();
//        if (!response.equals(SUCCESS_RESPONSE_DATA_PACKAGE)) {
//            throw new IllegalStateException("Response of sending data package isn't success.");
//        }
//    }
//
//    private static DataEntity findOldestData(final List<DataEntity> data) {
//        return data.stream()
//                .min(InboundPackageHandlingIT::compareById)
//                .orElseThrow(() -> new IllegalArgumentException("Impossible to find oldest data in empty list."));
//    }
//
//    private static DataEntity findNewestData(final List<DataEntity> data) {
//        return data.stream()
//                .max(InboundPackageHandlingIT::compareById)
//                .orElseThrow(() -> new IllegalArgumentException("Impossible to find newest data in empty list."));
//    }
//
//    private static int compareById(final DataEntity first, final DataEntity second) {
//        return (int) (first.getId() - second.getId());
//    }
//
//    private TrackerEntity findGivenExistingTracker() {
//        return super.entityManager.getReference(TrackerEntity.class, GIVEN_EXISTING_TRACKER_ID);
//    }
//
//    private static TrackerEntity createTracker(final Long id) {
//        return TrackerEntity.builder()
//                .id(id)
//                .build();
//    }
//
//    private static AddressEntity createAddress(final Long id) {
//        return AddressEntity.builder()
//                .id(id)
//                .build();
//    }
//
//    private static NominatimResponse createNominatimResponse(final double centerLatitude, final double centerLongitude,
//                                                             final String cityName, final String countryName,
//                                                             final double[] boundingBoxCoordinates) {
//        final NominatimResponseAddress address = new NominatimResponseAddress(cityName, countryName);
//        return new NominatimResponse(
//                centerLatitude,
//                centerLongitude,
//                address,
//                boundingBoxCoordinates,
//                //TODO: do some value
//                null
//        );
//    }
//
//    private static String createResponseBlackBoxPackage(final int amountOfFixedPackages) {
//        return format(TEMPLATE_RESPONSE_BLACK_BOX_PACKAGE, amountOfFixedPackages);
//    }
//
//    private static final class Client implements AutoCloseable {
//        private static final char PACKAGE_LAST_CHARACTER = '\n';
//
//        private final Socket socket;
//        private final ExecutorService executorService;
//
//        public Client(final String host, final int port)
//                throws IOException {
//            this.socket = new Socket(host, port);
//            this.executorService = newSingleThreadExecutor();
//        }
//
//        public Future<String> doRequest(final String request) {
//            return this.executorService.submit(() -> {
//                final OutputStream outputStream = this.socket.getOutputStream();
//                outputStream.write(request.getBytes(UTF_8));
//
//                final InputStream inputStream = this.socket.getInputStream();
//                final StringBuilder responseBuilder = new StringBuilder();
//                char currentReadChar;
//                do {
//                    currentReadChar = (char) inputStream.read();
//                    responseBuilder.append(currentReadChar);
//                } while (currentReadChar != PACKAGE_LAST_CHARACTER);
//
//                return responseBuilder.toString();
//            });
//        }
//
//        @Override
//        public void close()
//                throws IOException {
//            this.executorService.shutdownNow();
//            this.socket.close();
//        }
//    }
//}
