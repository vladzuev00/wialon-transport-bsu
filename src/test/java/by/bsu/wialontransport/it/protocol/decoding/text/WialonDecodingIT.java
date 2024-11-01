package by.bsu.wialontransport.it.protocol.decoding.text;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.protocol.core.decoder.TextProtocolDecoder;
import by.bsu.wialontransport.protocol.wialon.model.WialonLocation;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestBulkLocationPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestSingleLocationPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.bsu.wialontransport.model.ParameterType.*;

public final class WialonDecodingIT extends TextProtocolDecodingIT {

    public WialonDecodingIT(@Qualifier("wialonProtocolDecoder") final TextProtocolDecoder decoder) {
        super(decoder);
    }

    @Override
    protected Stream<TestArgument> provideArguments() {
        return Stream.of(
                new TestArgument(
                        "#L#11112222333344445555;password\r\n",
                        new WialonRequestLoginPackage("11112222333344445555", "password")
                ),
                new TestArgument(
                        "#P#\r\n",
                        new WialonRequestPingPackage()
                ),
                new TestArgument(
                        "#D#151122;145643;5354.173;N;02731.335;E;100;15;10;177;545.4554;17;18;5.5,4343.454544334,454.433,1;keydrivercode;122:1:5,123:2:6,124:2:7,par1:3:str,116:2:0.5\r\n",
                        new WialonRequestSingleLocationPackage(
                                new WialonLocation(
                                        LocalDate.of(2022, 11, 15),
                                        LocalTime.of(14, 56, 43),
                                        -53.948055555555555,
                                        27.60972222222222,
                                        15,
                                        100.,
                                        10,
                                        177,
                                        545.4554,
                                        17,
                                        18,
                                        new double[]{5.5, 4343.454544334, 454.433, 1},
                                        "keydrivercode",
                                        Set.of(
                                                Parameter.builder().name("122").type(INTEGER).value("5").build(),
                                                Parameter.builder().name("123").type(DOUBLE).value("6").build(),
                                                Parameter.builder().name("124").type(DOUBLE).value("7").build(),
                                                Parameter.builder().name("par1").type(STRING).value("str").build(),
                                                Parameter.builder().name("116").type(DOUBLE).value("0.5").build()
                                        )
                                )
                        )
                ),
                new TestArgument(
                        "",
                        new WialonRequestBulkLocationPackage(
                                List.of(

                                )
                        )
                )
        );
    }
}
