package by.bsu.wialontransport.protocol.it.tempdecode;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import io.netty.buffer.ByteBuf;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public final class NewWingProtocolDecodeIT extends BinaryProtocolDecodeIT {

    @Autowired
    private List<NewWingPackageDecoder> packageDecoders;

    @Test
    public void loginPackageShouldBeDecoded() {
        test(
                new byte[]{
                        71, 80, 82, 83, 71, 67,  //GPRSGC
                        -63, 11,                 //3009
                        -80, 13, -25, 14         //250023344
                },
                new NewWingLoginPackage(250023344, "3009")
        );
    }

    @Test
    public void eventCountPackageShouldBeDecoded() {
        test(
                new byte[]{
                        71, 80, 82, 83, 71, 73, //GPRSGI
                        -63, 12,                //3265
                        -63, 12,                //3265
                        -80, 13, -25, 14        //250023344
                },
                new NewWingEventCountPackage(250023344, (short) 3265, (short) 3265)
        );
    }

    @Test
    public void dataPackageShouldBeDecoded() {
        test(
                new byte[]{
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

                        19,                      //HOURS
                        52,                      //MINUTES
                        44,                      //SECONDS
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
                },
                new NewWingDataPackage(
                        -1512959769,
                        List.of(
                                Location.builder()
                                        .dateTime(LocalDateTime.of(2023, 10, 31, 18, 51, 43))
                                        .coordinate(new Coordinate(53.91630172729492, 27.56231689453125))
                                        .course(182)
                                        .speed(0.10)
                                        .hdop(2.123)
                                        .analogInputs(new double[]{0, 0, 0, 439})
                                        .build(),
                                Location.builder()
                                        .dateTime(LocalDateTime.of(2023, 10, 31, 19, 52, 44))
                                        .coordinate(new Coordinate(53.91630172729492, 27.56231689453125))
                                        .course(182)
                                        .speed(0.10)
                                        .hdop(2.123)
                                        .analogInputs(new double[]{0, 0, 0, 439})
                                        .build()
                        )
                )
        );
    }

    @Override
    protected List<? extends PackageDecoder<ByteBuf>> getPackageDecoders() {
        return packageDecoders;
    }
}
