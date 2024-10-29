package by.bsu.wialontransport.it.protocol.decoding.text;

import by.bsu.wialontransport.protocol.core.decoder.TextProtocolDecoder;
import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonRequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.ping.WialonRequestPingPackage;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Stream;

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
                )
        );
    }
}
