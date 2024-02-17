package by.bsu.wialontransport.protocol.it.newwing;

import by.bsu.wialontransport.configuration.property.protocolserver.NewWingProtocolServerConfiguration;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.protocol.it.core.ProtocolIT;
import by.bsu.wialontransport.util.entity.DataEntityUtil;
import by.bsu.wialontransport.util.entity.TrackerMileageEntityUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;

public final class NewWingProtocolIT extends ProtocolIT {
    private static final TrackerEntity GIVEN_EXISTING_TRACKER = TrackerEntity.builder()
            .id(257L)
            .imei("00000000000000003009")
            .password("password")
            .build();

    private static final byte[] GIVEN_REQUEST_TO_LOGIN = {
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
        final String actual = client.request(GIVEN_REQUEST_TO_LOGIN);
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
    public void dataPackageShouldBeHandledInCaseNotExistingPreviousDataAndAddressShouldBeReceivedFromDatabase()
            throws Exception {
        final byte[] givenRequestDataPackage = {
                71, 80, 82, 83, 83, 73,  //GPRSSI
                18,                      //HOURS
                51,                      //MINUTES
                43,                      //SECONDS
                -22, 20,                 //LATITUDE_INTEGER_PART
                -70, 7,                  //LATITUDE_FRACTIONAL_PART
                -83, 10,                 //LONGITUDE_INTEGER_PART
                -53, 6,                  //LONGITUDE_INTEGER_PART
                2,                       //HDOP_INTEGER_PART
                123,                     //HDOP_FRACTIONAL_PART
                -74, 0,                  //COURSE
                0, 0,                    //SPEED_INTEGER_PART
                10,                      //SPEED_FRACTIONAL_PART
                31,                      //DAY
                10,                      //MONTH
                23,                      //YEAR
                0, 0,                    //FIRST_ANALOG_INPUT_LEVEL
                0, 0,                    //SECOND_ANALOG_INPUT_LEVEL
                0, 0,                    //THIRD_ANALOG_INPUT_LEVEL
                -73, 1,                  //FOURTH_ANALOG_INPUT_LEVEL
                -22,                     //FLAGS_BYTE
                127,                     //DISCRETE_INPUT_STATE
                -35, 120,                //CHECKSUM
                -25, 16, -46, -91        //PACKAGE_CHECKSUM
        };

        loginByExistingTracker();
        sendDataPackageExpectingSuccess(givenRequestDataPackage);

        assertTrue(isSuccessDataDelivering());

        final List<DataEntity> actualAllData = findDataFetchingParametersAndTrackerAndAddressOrderedById();
        final int actualAllDataSize = actualAllData.size();
        final int expectedAllDataSize = 1;
        assertEquals(expectedAllDataSize, actualAllDataSize);

        final DataEntity actualData = actualAllData.get(0);
        final DataEntity expectedData = DataEntity.builder()
                .id(1L)
                .dateTime(LocalDateTime.of(2023, 10, 31, 18, 51, 43))
                .coordinate(new Coordinate(53.9163017272949, 27.5623168945312))
                .speed(0.1)
                .course(182)
                .altitude(0)
                .amountOfSatellites(3)
                .hdop(2.123)
                .inputs(0)
                .outputs(0)
                .analogInputs(new double[]{0, 0, 0, 0.439})
                .driverKeyCode("not defined")
                .parameters(emptyList())
                .tracker(GIVEN_EXISTING_TRACKER)
                .address(GIVEN_EXISTING_ADDRESS)
                .build();
        DataEntityUtil.checkEquals(expectedData, actualData);

        final TrackerMileageEntity actualMileage = findTrackerMileage(GIVEN_EXISTING_TRACKER);
        final TrackerMileageEntity expectedMileage = TrackerMileageEntity.builder()
                .id(3L)
                .country(0)
                .urban(0)
                .build();
        TrackerMileageEntityUtil.checkEquals(expectedMileage, actualMileage);

        final String actualKafkaSavedDataConsumerPayload = getKafkaSavedDataConsumerPayload();
        final String expectedKafkaSavedDataConsumerPayloadRegex = "ConsumerRecord\\(topic = saved-data, partition = \\d+, "
                + "leaderEpoch = \\d+, offset = \\d+, CreateTime = \\d+, serialized key size = 8, serialized value size = \\d+, "
                + "headers = RecordHeaders\\(headers = \\[], isReadOnly = false\\), key = 257, value = \\{\"id\": 1, "
                + "\"addressId\": 102, \"epochSeconds\": 1698778303, \"latitude\": 53\\.91630172729492, "
                + "\"longitude\": 27\\.56231689453125, \"course\": 182, \"speed\": 0\\.1, \"altitude\": 0, "
                + "\"amountOfSatellites\": 3, \"hdop\": 2\\.123, \"inputs\": 0, \"outputs\": 0, "
                + "\"serializedAnalogInputs\": \"\\[0\\.0,0\\.0,0\\.0,0\\.439]\", \"driverKeyCode\": \"not defined\", "
                + "\"serializedParameters\": \"\\[]\", \"trackerId\": 257}\\)";
        assertTrue(actualKafkaSavedDataConsumerPayload.matches(expectedKafkaSavedDataConsumerPayloadRegex));

        verifyNoInteractions(mockedNominatimService);
    }

    private void loginByExistingTracker()
            throws IOException {
        client.requestExpectingResponse(
                GIVEN_REQUEST_TO_LOGIN,
                SUCCESS_RESPONSE,
                "Login by existing tracker isn't success"
        );
    }

    private void sendDataPackageExpectingSuccess(final byte[] request)
            throws IOException {
        client.requestExpectingResponse(
                request,
                SUCCESS_RESPONSE,
                "Sending data package isn't success"
        );
    }
}
