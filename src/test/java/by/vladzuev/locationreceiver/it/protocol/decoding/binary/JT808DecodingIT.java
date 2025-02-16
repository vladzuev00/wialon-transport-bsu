package by.vladzuev.locationreceiver.it.protocol.decoding.binary;

import by.vladzuev.locationreceiver.protocol.core.decoder.BinaryProtocolDecoder;
import by.vladzuev.locationreceiver.protocol.jt808.model.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public final class JT808DecodingIT extends BinaryProtocolDecodingIT {

    public JT808DecodingIT(@Qualifier("jt808ProtocolDecoder") final BinaryProtocolDecoder decoder) {
        super(decoder);
    }

    @Override
    protected Stream<TestArgument> provideArguments() {
        return Stream.of(
                new TestArgument(
                        "7e0100003607006195286504fa0000000038363937374e5438303800000000000000000000000000000031393532383635004c42313233343536373839303132333435607e",
                        new JT808RegistrationPackage("869770061952865")
                ),
                new TestArgument(
                        "7e0100002c0182700570781022001f00000000000000434152564953204d442d34343453440000000000303035373037380142313233343536bc7e",
                        new JT808RegistrationPackage("8270057078")
                ),
                new TestArgument(
                        "7e0200004207006195286500520001000000000001015881c906ca8e0500000000000023072707091430011f31010051080000000000000000560231005708000200000000000063020000fd020026157e",
                        new JT808LocationPackage(
                                singletonList(
                                        new JT808Location(
                                                LocalDateTime.of(2023, 7, 27, 7, 9, 14),
                                                22.577609,
                                                11.3937925,
                                                (short) 0,
                                                (short) 0,
                                                (short) 0
                                        )
                                )
                        )
                ),
                new TestArgument("7e000200000072610190040378ff7e", new JT808HeartBeatPackage()),
                new TestArgument(
                        "7e07040047070061952865004a00010100420001000000000001015881c906ca8e0500000000000023072707091430011f31010051080000000000000000560231005708000200000000000063020000fd020026107e",
                        new JT808LocationPackage(
                                singletonList(
                                        new JT808Location(
                                                LocalDateTime.of(2023, 7, 27, 7, 9, 14),
                                                22.577609,
                                                11.3937925,
                                                (short) 0,
                                                (short) 0,
                                                (short) 0
                                        )
                                )
                        )
                ),
                new TestArgument("7e0102000c07006195286504fc3037303036313935323836354c7e", new JT808AuthPackage())
        );
    }
}
