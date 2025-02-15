package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import by.vladzuev.locationreceiver.protocol.jt808.model.JT808Location;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808LocationPackage;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JT808LocationPackageDecoderTest {
    private final TestJT808LocationPackageDecoder decoder = new TestJT808LocationPackageDecoder();

    @Test
    public void bufferShouldBeDecodedInternally() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00020100420001000000000001015881C906CA8E0500000000000023072707091430011F31010051080000000000000000560231005708000200000000000063020000FD02002600420001000000000001015881C906CA8E0500000000000023072707091430011F31010051080000000000000000560231005708000200000000000063020000FD020026"));
        final String givenPhoneNumber = "";

        final JT808LocationPackage actual = decoder.decodeInternal(givenBuffer, givenPhoneNumber);
        final JT808LocationPackage expected = new JT808LocationPackage(
                List.of(
                        new JT808Location(
                                LocalDateTime.of(2023, 7, 27, 7, 9, 14),
                                22.577609,
                                11.3937925,
                                (short) 0,
                                (short) 0,
                                (short) 0
                        ),
                        new JT808Location(
                                LocalDateTime.of(2023, 7, 27, 7, 9, 14),
                                22.577609,
                                11.3937925,
                                (short) 0,
                                (short) 0,
                                (short) 0
                        )
                )
        );
        assertEquals(expected, actual);
    }

    private static final class TestJT808LocationPackageDecoder extends JT808LocationPackageDecoder {
        private static final byte[] REQUIRED_PREFIX = {126, 2, 0};

        public TestJT808LocationPackageDecoder() {
            super(REQUIRED_PREFIX);
        }

        @Override
        protected int decodeLocationCount(final ByteBuf buffer) {
            return buffer.readShort();
        }

        @Override
        protected void skipUntilFirstLocation(final ByteBuf buffer) {
            buffer.skipBytes(Byte.BYTES);
        }

        @Override
        protected void skipLocationPrefix(final ByteBuf buffer) {
            buffer.skipBytes(Short.BYTES);
        }
    }
}
