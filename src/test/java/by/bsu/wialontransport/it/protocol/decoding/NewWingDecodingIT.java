package by.bsu.wialontransport.it.protocol.decoding;

import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Stream;

public final class NewWingDecodingIT extends BinaryProtocolDecodingIT {

    public NewWingDecodingIT(@Qualifier("newWingProtocolDecoder") final BinaryProtocolDecoder decoder) {
        super(decoder);
    }

    @Override
    protected Stream<TestArgument> provideArguments() {
        return Stream.of(
                new TestArgument("475052534743c10bb00de70e", new NewWingLoginPackage((short) 3009)),
                new TestArgument("475052534749c10cc10cb00de70e", new NewWingEventCountPackage())
        );
    }
}
