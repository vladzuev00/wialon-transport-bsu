package by.bsu.wialontransport.protocol.it.newwing;

import by.bsu.wialontransport.configuration.property.protocolserver.NewWingProtocolServerConfiguration;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.protocol.it.core.ProtocolIT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public final class NewWingProtocolIT extends ProtocolIT {
    private static final TrackerEntity GIVEN_EXISTING_TRACKER = TrackerEntity.builder()
            .id(257L)
            .imei("00000000000000003009")
            .password("password")
            .build();

    private static final byte[] GIVEN_LOGIN_REQUEST_BYTES = {
            71, 80, 82, 83, 71, 67,  //GPRSGC
            -63, 11,                 //3009
            -80, 13, -25, 14         //250023344
    };

    private static final String SUCCESS_RESPONSE = "TTTTT";
    private static final String FAIL_RESPONSE = "FFFFF";

    @Autowired
    private NewWingProtocolServerConfiguration serverConfiguration;

    private NewWingClient client;

    @Before
    public void initializeClient()
            throws Exception {
        client = new NewWingClient(serverConfiguration.getInetSocketAddress());
    }

    @After
    public void closeClient()
            throws IOException {
        client.close();
    }

    @Test
    public void loginPackageShouldBeHandledSuccessfully()
            throws Exception {
        final String actual = client.request(GIVEN_LOGIN_REQUEST_BYTES);
        assertEquals(SUCCESS_RESPONSE, actual);
    }

    @Test
    public void loginPackageShouldBeHandledAsFailed()
            throws Exception {
        final byte[] givenRequestBytes = {
                71, 80, 82, 83, 71, 67,  //GPRSGC
                -63, 12,                 //3265
                -80, 13, -25, 14         //250023344
        };

        final String actual = client.request(givenRequestBytes);
        assertEquals(FAIL_RESPONSE, actual);
    }

    @Test
    public void eventCountPackageShouldBeHandledSuccessfully()
            throws Exception {
        final byte[] givenRequestBytes = {
                71, 80, 82, 83, 71, 73, //GPRSGI
                -63, 12,                //3265
                -63, 12,                //3265
                -80, 13, -25, 14        //250023344
        };

        final String actual = client.request(givenRequestBytes);
        assertEquals(SUCCESS_RESPONSE, actual);
    }

    @Test
    public void dataPackageWithDefinedPropertiesShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase() {
        throw new RuntimeException();
    }
}
