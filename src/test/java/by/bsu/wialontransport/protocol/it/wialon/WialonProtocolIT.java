package by.bsu.wialontransport.protocol.it.wialon;

import by.bsu.wialontransport.configuration.property.protocolserver.WialonProtocolServerConfiguration;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.protocol.it.core.ProtocolIT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static by.bsu.wialontransport.util.entity.DataEntityUtil.checkEqualsExceptIdAndParameters;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class WialonProtocolIT extends ProtocolIT {
    private static final String GIVEN_REQUEST_TO_LOGIN = "#L#%s;%s\r\n".formatted(
            GIVEN_EXISTING_TRACKER.getImei(),
            GIVEN_EXISTING_TRACKER.getPassword()
    );
    private static final String SUCCESS_LOGIN_RESPONSE = "#AL#1\r\n";
    private static final String ERROR_CHECKING_PASSWORD_LOGIN_RESPONSE = "#AL#01\r\n";
    private static final String CONNECTION_FAILURE_LOGIN_RESPONSE = "#AL#0\r\n";

    private static final String SUCCESS_PING_RESPONSE = "#AP#\r\n";

    private static final String GIVEN_VALID_REQUEST_DATA_PACKAGE = "#D#151122;145643;5344.588228;N;02720.15216;E;100;15;10;"
            + "177;545.4554;17;18;"
            + "5.5,4343.454544334,454.433,1;"
            + "keydrivercode;"
            + "122:2:5,123:2:6,124:2:7,"
            + "par1:3:str,116:2:0.5"
            + "\r\n";
    private static final String SUCCESS_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#1\r\n";
    private static final String FAILED_RECEIVING_DATA_PACKAGE_RESPONSE = "#AD#-1\r\n";

    @Autowired
    private WialonProtocolServerConfiguration serverConfiguration;

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

        final List<DataEntity> actualAllData = findAllDataOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2022, 11, 15, 14, 56, 43))
                .coordinate(new Coordinate(53.75277777777778, 27.396388888888886))
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

        //TODO: check parameters and mileage and consumer's payload
        //TODO: mock nominatim service and verify no interactions
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
        final String response = client.request(request);
        if (!Objects.equals(expectedResponse, response)) {
            throw new IllegalStateException(notExpectedResponseMessage);
        }
    }
}
