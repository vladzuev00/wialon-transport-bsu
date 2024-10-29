package by.bsu.wialontransport.it.protocol.decoding.binary;

import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.newwing.model.NewWingLocation;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLocationPackage;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public final class NewWingDecodingIT extends BinaryProtocolDecodingIT {

    public NewWingDecodingIT(@Qualifier("newWingProtocolDecoder") final BinaryProtocolDecoder decoder) {
        super(decoder);
    }

    @Override
    protected Stream<TestArgument> provideArguments() {
        return Stream.of(
                new TestArgument(
                        "475052534743c10bb00de70e",
                        new NewWingLoginPackage((short) 3009)
                ),
                new TestArgument(
                        "475052534749c10cc10cb00de70e",
                        new NewWingEventCountPackage()
                ),
                new TestArgument(
                        "47505253534912332bea14ba07ad0acb06027bb60000000a1f0a17000000000000b701ea7fdd7812332bea14ba07ad0acb06027bb60000000a1f0a17000000000000b712ea7fdd78e710d2a5",
                        new NewWingLocationPackage(
                                List.of(
                                        new NewWingLocation(
                                                LocalDateTime.of(2023, 10, 31, 18, 51, 43),
                                                53.91630172729492,
                                                27.56231689453125,
                                                (short) 182,
                                                0.1,
                                                2.123,
                                                new double[]{0, 0, 0, 439}
                                        ),
                                        new NewWingLocation(
                                                LocalDateTime.of(2023, 10, 31, 18, 51, 43),
                                                53.91630172729492,
                                                27.56231689453125,
                                                (short) 182,
                                                0.1,
                                                2.123,
                                                new double[]{0, 0, 0, 4791}
                                        )
                                )
                        )
                )
        );
    }
}
