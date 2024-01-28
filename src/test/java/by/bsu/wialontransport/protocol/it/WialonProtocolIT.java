package by.bsu.wialontransport.protocol.it;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.base.kafka.TestKafkaSavedDataConsumer;
import by.bsu.wialontransport.configuration.property.protocolserver.WialonProtocolServerConfiguration;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.util.entity.DataEntityUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static by.bsu.wialontransport.util.entity.DataEntityUtil.checkEqualsExceptIdAndParameters;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@Sql("classpath:sql/protocol-it/before-test.sql")
@Sql(value = "classpath:sql/protocol-it/after-test.sql", executionPhase = AFTER_TEST_METHOD)
public final class WialonProtocolIT extends AbstractContextTest {
    private static final Charset CHARSET = UTF_8;

    private static final AddressEntity GIVEN_EXISTING_ADDRESS = AddressEntity.builder()
            .id(103L)
            .build();
    private static final TrackerEntity GIVEN_EXISTING_TRACKER = TrackerEntity.builder()
            .id(255L)
            .imei("11112222333344445555")
            .password("password")
            .build();
    private static final String GIVEN_REQUEST_TO_LOGIN = "#L#%s;%s\r\n"
            .formatted(
                    GIVEN_EXISTING_TRACKER.getImei(),
                    GIVEN_EXISTING_TRACKER.getPassword()
            );
    private static final String SUCCESS_LOGIN_RESPONSE = "#AL#1\r\n";
    private static final String ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE = "#AL#01\r\n";
    private static final String CONNECTION_FAILURE_LOGIN_RESPONSE = "#AL#0\r\n";

    private static final String SUCCESS_PING_RESPONSE = "#AP#\r\n";

    private static final String GIVEN_VALID_REQUEST_DATA_PACKAGE = "#D#151122;145643;5345.1065;N;02720.2270;E;100;15;10;"
            + "177;545.4554;17;18;"
            + "5.5,4343.454544334,454.433,1;"
            + "keydrivercode;"
            + "122:2:5,123:2:6,124:2:7,"
            + "par1:3:str,116:2:0.5"
            + "\r\n";
    private static final String SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#1\r\n";
    private static final String FAILED_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#-1\r\n";

    private static final int WAIT_DATA_DELIVERING_IN_SECONDS = 6;

    @Autowired
    private WialonProtocolServerConfiguration serverConfiguration;

    @Autowired
    private TestKafkaSavedDataConsumer savedDataConsumer;

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
        final String actual = request(GIVEN_REQUEST_TO_LOGIN);
        assertEquals(SUCCESS_LOGIN_RESPONSE, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithErrorCheckPasswordStatus()
            throws Exception {
        final String givenRequest = "#L#%s;wrong_password\r\n".formatted(GIVEN_EXISTING_TRACKER.getImei());

        final String actual = request(givenRequest);
        assertEquals(ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE, actual);
    }

    @Test
    public void loginPackageShouldBeHandledWithConnectionFailureStatus()
            throws Exception {
        final String givenRequest = "#L#00000000000000000000;password\r\n";

        final String actual = request(givenRequest);
        assertEquals(CONNECTION_FAILURE_LOGIN_RESPONSE, actual);
    }

    @Test
    public void pingPackageShouldBeHandled()
            throws Exception {
        final String givenRequest = "#P#\r\n";

        final String actual = request(givenRequest);
        assertEquals(SUCCESS_PING_RESPONSE, actual);
    }

    @Test
    public void dataPackageWithDefinedPropertiesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        loginByExistingTracker();
        sendValidRequestDataPackage();

        assertTrue(waitDataDeliveringAndReturnDeliveredOrNot());

        final List<DataEntity> actualAllData = findAllDataOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
                .coordinate(new Coordinate(53.75295883333333, 27.333963999999998))
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
    }

    private String request(final String request)
            throws IOException {
        final byte[] requestBytes = request.getBytes(CHARSET);
        final byte[] responseBytes = client.request(requestBytes);
        return new String(responseBytes, CHARSET);
    }

    private void loginByExistingTracker()
            throws IOException {
        sendRequestExpectingResponse(
                GIVEN_REQUEST_TO_LOGIN,
                SUCCESS_LOGIN_RESPONSE,
                "Login by existing tracker is failed"
        );
    }

    private void sendValidRequestDataPackage()
            throws IOException {
        sendRequestExpectingResponse(
                GIVEN_VALID_REQUEST_DATA_PACKAGE,
                SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE,
                "Sending data package is failed"
        );
    }

    private void sendRequestExpectingResponse(final String request,
                                              final String expectedResponse,
                                              final String notExpectedResponseMessage)
            throws IOException {
        final String response = request(request);
        if (!Objects.equals(expectedResponse, response)) {
            throw new IllegalStateException(notExpectedResponseMessage);
        }
    }

    private boolean waitDataDeliveringAndReturnDeliveredOrNot()
            throws InterruptedException {
        return savedDataConsumer
                .getCountDownLatch()
                .await(WAIT_DATA_DELIVERING_IN_SECONDS, SECONDS);
    }

    private List<DataEntity> findAllDataOrderedById() {
        return entityManager.createQuery("SELECT e FROM DataEntity e ORDER BY e.id", DataEntity.class)
                .getResultList();
    }

    private static final class WialonClient implements AutoCloseable {
        private final Socket socket;

        public WialonClient(final InetSocketAddress address)
                throws IOException {
            socket = new Socket(address.getAddress(), address.getPort());
        }

        public byte[] request(final byte[] bytes)
                throws IOException {
            send(bytes);
            return receive();
        }

        private void send(final byte[] bytes)
                throws IOException {
            socket.getOutputStream().write(bytes);
        }

        private byte[] receive()
                throws IOException {
            return socket.getInputStream().readAllBytes();
        }

        @Override
        public void close()
                throws IOException {
            socket.close();
        }
    }
}
