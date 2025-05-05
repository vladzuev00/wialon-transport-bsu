package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TeltonikaLocationDecoderTest {
    private final TeltonikaLocationDecoder decoder = new TeltonikaLocationDecoder();

    @Test
    public void locationShouldBeDecoded() {
        final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00000113fc208dff000f14f650209cca80006f00d60400040004030101150316030001460000015d00"));

        final TeltonikaLocation actual = decoder.decode(givenBuffer);
        final TeltonikaLocation expected = new TeltonikaLocation(
                ofEpochSecond(1185345998335L, 0, UTC),
                54.7146368,
                25.3032016,
                (short) 111,
                (short) 214,
                (byte) 4,
                (short) 4
        );
        assertEquals(expected, actual);

        assertEquals(0, givenBuffer.readableBytes());
    }
}
