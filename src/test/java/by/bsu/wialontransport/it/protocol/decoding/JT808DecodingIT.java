package by.bsu.wialontransport.it.protocol.decoding;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.protocol.core.decoder.BinaryProtocolDecoder;
import by.bsu.wialontransport.protocol.jt808.model.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public final class JT808DecodingIT extends AbstractSpringBootTest {

    @Autowired
    @Qualifier("jt808ProtocolDecoder")
    private BinaryProtocolDecoder decoder;

    @ParameterizedTest
    @MethodSource("provideHexDumpAndExpectedPackage")
    public void packageShouldBeDecoded(final String givenHexDump, final Object expected) {
        final EmbeddedChannel givenChannel = new EmbeddedChannel(decoder);
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump(givenHexDump));
        givenChannel.writeInbound(givenBuffer);
        final Object actual = givenChannel.readInbound();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideHexDumpAndExpectedPackage() {
        return Stream.of(
                Arguments.of(
                        "7e0100003607006195286504fa0000000038363937374e5438303800000000000000000000000000000031393532383635004c42313233343536373839303132333435607e",
                        new JT808RegistrationPackage("070061952865", "86977")
                ),
                Arguments.of(
                        "7e0100002c0182700570781022001f00000000000000434152564953204d442d34343453440000000000303035373037380142313233343536bc7e",
                        new JT808RegistrationPackage("018270057078", "")
                ),
                Arguments.of(
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
                Arguments.of(
                        "7e000200000072610190040378ff7e",
                        new JT808HeartBeatPackage()
                ),
                Arguments.of(
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
                Arguments.of(
                        "7e0102000c07006195286504fc3037303036313935323836354c7e",
                        new JT808AuthenticationPackage()
                )
        );
    }
}
