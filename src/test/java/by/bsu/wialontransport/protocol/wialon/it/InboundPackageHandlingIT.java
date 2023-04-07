package by.bsu.wialontransport.protocol.wialon.it;

import by.bsu.wialontransport.base.AbstractKafkaContainerTest;
import by.bsu.wialontransport.configuration.WialonServerConfiguration;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Latitude;
import by.bsu.wialontransport.crud.entity.DataEntity.Longitude;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.protocol.wialon.server.WialonServer;
import by.bsu.wialontransport.protocol.wialon.server.factory.WialonServerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

public final class InboundPackageHandlingIT extends AbstractKafkaContainerTest {
    private static boolean serverWasRan = false;

    private static final Long GIVEN_EXISTING_TRACKER_ID = 255L;
    private static final String GIVEN_EXISTING_TRACKER_IMEI = "11112222333344445555";
    private static final String GIVEN_EXISTING_TRACKER_PASSWORD = "password";
    private static final String GIVEN_REQUEST_TO_LOGIN = "#L#" + GIVEN_EXISTING_TRACKER_IMEI + ";"
            + GIVEN_EXISTING_TRACKER_PASSWORD + "\r\n";

    private static final String SUCCESS_RESPONSE_LOGIN = "#AL#1\r\n";

    private static final String MESSAGE_EXCEPTION_FAILED_LOGIN = "Login is failed.";

    private static final int WAIT_MESSAGE_DELIVERING_IN_SECONDS = 3;

    private static final String HQL_QUERY_TO_FIND_ALL_DATA = "SELECT e FROM DataEntity e";

    private static final String HQL_QUERY_TO_FIND_PARAMETERS_AMOUNT = "SELECT COUNT(e) FROM ParameterEntity e";

    private static final String HQL_QUERY_TO_FIND_OUT_EXIST_PARAMETER_WITH_GIVEN_PROPERTIES
            = "SELECT 1 FROM ParameterEntity e "
            + "WHERE e.name = :name AND e.type = :type AND e.value = :value AND e.data = :data";
    private static final String NAME_NAMED_PARAMETER_PARAMETER_NAME = "name";
    private static final String NAME_NAMED_PARAMETER_PARAMETER_TYPE = "type";
    private static final String NAME_NAMED_PARAMETER_PARAMETER_VALUE = "value";
    private static final String NAME_NAMED_PARAMETER_PARAMETER_DATA = "data";

    @Autowired
    private WialonServerConfiguration serverConfiguration;

    @Autowired
    private WialonServerFactory serverFactory;

    private Client client;

    @Before
    public void startServerAndClient()
            throws Exception {
        this.runServerIfWasNotRun();
        this.client = new Client(this.serverConfiguration.getHost(), this.serverConfiguration.getPort());
    }

    @After
    public void closeClient()
            throws IOException {
        this.client.close();
    }

    @Test
    public void loginPackageShouldBeHandledWithSuccessAuthorizationStatus()
            throws Exception {
        final String actual = this.client.doRequest(GIVEN_REQUEST_TO_LOGIN)
                .get();
        assertEquals(SUCCESS_RESPONSE_LOGIN, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithErrorCheckPasswordStatus()
            throws Exception {
        final String givenRequest = "#L#" + GIVEN_EXISTING_TRACKER_IMEI + ";wrong_password\r\n";

        final String actual = this.client.doRequest(givenRequest)
                .get();
        final String expected = "#AL#01\r\n";
        assertEquals(expected, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithConnectionFailureStatus()
            throws Exception {
        final String givenRequest = "#L#00000000000000000000;wrong_password\r\n";

        final String actual = this.client.doRequest(givenRequest)
                .get();
        final String expected = "#AL#0\r\n";
        assertEquals(expected, actual);
    }

    @Test
    public void pingPackageShouldBeHandled()
            throws Exception {
        final String givenRequest = "#P#\r\n";

        final String actual = this.client.doRequest(givenRequest)
                .get();
        final String expected = "#AP#\r\n";
        assertEquals(expected, actual);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(statements = "UPDATE trackers_last_data SET data_id = NULL", executionPhase = AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM data", executionPhase = AFTER_TEST_METHOD)
    public void dataPackageShouldBeHandledAsValidWithoutFixing()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertEquals(1, dataFromDatabase.size());

        final DataEntity actualSavedData = dataFromDatabase.get(0);
        final DataEntity expectedSavedData = DataEntity.builder()
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(14, 56, 43))
                .latitude(createLatitude(55, 44, 6025, NORTH))
                .longitude(createLongitude(37, 39, 6834, EAST))
                .speed(100)
                .course(15)
                .altitude(10)
                .amountOfSatellites(177)
                .reductionPrecision(545.4554)
                .inputs(17)
                .outputs(18)
                .analogInputs(new double[]{5.5, 4343.454544334, 454.433, 1})
                .driverKeyCode("keydrivercode")
                .tracker(super.entityManager.getReference(TrackerEntity.class, GIVEN_EXISTING_TRACKER_ID))
                .build();
        checkEqualsExceptIdAndParameters(expectedSavedData, actualSavedData);

        assertEquals(5, this.findAmountOfParametersInDataBase());
        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("122", DOUBLE, "5", actualSavedData));
        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("123", DOUBLE, "6", actualSavedData));
        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("124", DOUBLE, "7", actualSavedData));
        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("par1", STRING, "str", actualSavedData));
        assertTrue(this.isParameterWithGivenPropertiesExistsInDataBase("116", DOUBLE, "0.5", actualSavedData));

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfAmountOfSatellitesIsLessThanMinimalAllowableAndThereIsNoPreviousValidDataToFix()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;2;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    //amountOfSatellitesIsMoreThanMaximalAllowable
    @Test
    public void dataPackageShouldBeSkippedBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowableAndThereIsNoPreviousValidDataToFix()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;1000;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfDateTimeIsLessThanMinimalAllowable()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151105;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfDateTimeIsMoreThanMaximalAllowable()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151199;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterDoesNotExistAndThereIsNoPreviousValidDataToFix()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //VDOP, PDOP
                + "123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterIsLessThanMinimalAllowableAndThereIsNoPreviousValidDataToFix()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:0.1,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterIsMoreThanMaximalAllowableAndThereIsNoPreviousValidDataToFix()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:2:8,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfHDOPParameterIsNotDoubleAndThereIsNoPreviousValidDataToFix()
            throws Exception {
        this.login();

        final String givenRequest = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                //HDOP, VDOP, PDOP
                + "122:1:5,123:2:6,124:2:7,"
                + "par1:3:str,116:2:0.5"
                + "\r\n";

        final String actual = this.client.doRequest(givenRequest).get();
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);

        waitMessageDelivering();

        final List<DataEntity> dataFromDatabase = this.findAllDataFromDataBase();
        assertTrue(dataFromDatabase.isEmpty());

        assertEquals(0, this.findAmountOfParametersInDataBase());

        //TODO: check in kafka
    }

    @Test
    public void dataPackageShouldBeSkippedBecauseOfVDOPParameterDoesNotExistAndThereIsNoPreviousValidDataToFix() {

    }

    private void runServerIfWasNotRun()
            throws InterruptedException {
        if (!serverWasRan) {
            new Thread(() -> {
                try (final WialonServer server = this.serverFactory.create()) {
                    server.run();
                }
            }).start();
            SECONDS.sleep(1);  //to give thread starting server time to run it
            serverWasRan = true;
        }
    }

    private void login()
            throws ExecutionException, InterruptedException {
        final String response = this.client.doRequest(GIVEN_REQUEST_TO_LOGIN).get();
        if (!SUCCESS_RESPONSE_LOGIN.equals(response)) {
            throw new IllegalStateException(MESSAGE_EXCEPTION_FAILED_LOGIN);
        }
    }

    private static void waitMessageDelivering()
            throws InterruptedException {
        SECONDS.sleep(WAIT_MESSAGE_DELIVERING_IN_SECONDS);
    }

    private static Latitude createLatitude(final int degrees, final int minutes, final int minuteShare,
                                           final Latitude.Type type) {
        return Latitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Longitude createLongitude(final int degrees, final int minutes, final int minuteShare,
                                             final Longitude.Type type) {
        return Longitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static void checkEqualsExceptIdAndParameters(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.getLatitude(), actual.getLatitude());
        assertEquals(expected.getLongitude(), actual.getLongitude());
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getTracker().getId(), actual.getTracker().getId());
    }

    private List<DataEntity> findAllDataFromDataBase() {
        return super.entityManager.createQuery(HQL_QUERY_TO_FIND_ALL_DATA, DataEntity.class)
                .getResultList();
    }

    private long findAmountOfParametersInDataBase() {
        return super.entityManager.createQuery(HQL_QUERY_TO_FIND_PARAMETERS_AMOUNT, Long.class)
                .getSingleResult();
    }

    private boolean isParameterWithGivenPropertiesExistsInDataBase(String name, ParameterEntity.Type type,
                                                                   String value, DataEntity data) {
        try {
            super.entityManager.createQuery(HQL_QUERY_TO_FIND_OUT_EXIST_PARAMETER_WITH_GIVEN_PROPERTIES)
                    .setParameter(NAME_NAMED_PARAMETER_PARAMETER_NAME, name)
                    .setParameter(NAME_NAMED_PARAMETER_PARAMETER_TYPE, type)
                    .setParameter(NAME_NAMED_PARAMETER_PARAMETER_VALUE, value)
                    .setParameter(NAME_NAMED_PARAMETER_PARAMETER_DATA, data)
                    .getSingleResult();
            return true;
        } catch (final NoResultException noResultException) {
            return false;
        }
    }

    private static final class Client implements AutoCloseable {
        private static final char PACKAGE_LAST_CHARACTER = '\n';

        private final Socket socket;
        private final ExecutorService executorService;

        public Client(final String host, final int port)
                throws IOException {
            this.socket = new Socket(host, port);
            this.executorService = newSingleThreadExecutor();
        }

        public Future<String> doRequest(final String request) {
            return this.executorService.submit(() -> {
                final OutputStream outputStream = this.socket.getOutputStream();
                outputStream.write(request.getBytes(UTF_8));

                final InputStream inputStream = this.socket.getInputStream();
                final StringBuilder responseBuilder = new StringBuilder();
                char currentReadChar;
                do {
                    currentReadChar = (char) inputStream.read();
                    responseBuilder.append(currentReadChar);
                } while (currentReadChar != PACKAGE_LAST_CHARACTER);

                return responseBuilder.toString();
            });
        }

        @Override
        public void close()
                throws IOException {
            this.executorService.shutdownNow();
            this.socket.close();
        }
    }
}
