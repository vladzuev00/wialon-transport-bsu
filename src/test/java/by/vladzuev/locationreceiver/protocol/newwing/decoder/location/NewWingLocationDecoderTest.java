package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.newwing.model.NewWingLocation;
import by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class NewWingLocationDecoderTest {
    private final NewWingLocationDecoder decoder = new NewWingLocationDecoder();

    @Test
    public void locationShouldBeDecoded() {
        try (final MockedStatic<NewWingUtil> mockedUtil = mockStatic(NewWingUtil.class)) {
            final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00000000"));

            final LocalTime givenTime = LocalTime.of(10, 20, 30);
            mockedUtil.when(() -> NewWingUtil.decodeTime(same(givenBuffer))).thenReturn(givenTime);

            final double givenLatitude = 5.5;
            mockedUtil.when(() -> NewWingUtil.decodeLatitude(same(givenBuffer))).thenReturn(givenLatitude);

            final double givenLongitude = 6.6;
            mockedUtil.when(() -> NewWingUtil.decodeLongitude(same(givenBuffer))).thenReturn(givenLongitude);

            final double givenHdop = 9;
            mockedUtil.when(() -> NewWingUtil.decodeHdop(same(givenBuffer))).thenReturn(givenHdop);

            final short givenCourse = 7;
            mockedUtil.when(() -> NewWingUtil.decodeShort(same(givenBuffer))).thenReturn(givenCourse);

            final double givenSpeed = 8;
            mockedUtil.when(() -> NewWingUtil.decodeSpeed(same(givenBuffer))).thenReturn(givenSpeed);

            final LocalDate givenDate = LocalDate.of(2024, 10, 28);
            mockedUtil.when(() -> NewWingUtil.decodeDate(same(givenBuffer))).thenReturn(givenDate);

            final double[] givenAnalogInputs = {11, 12, 13, 14};
            mockedUtil.when(() -> NewWingUtil.decodeAnalogInputs(same(givenBuffer))).thenReturn(givenAnalogInputs);

            final NewWingLocation actual = decoder.decode(givenBuffer);
            final NewWingLocation expected = new NewWingLocation(
                    givenDate,
                    givenTime,
                    givenLatitude,
                    givenLongitude,
                    givenCourse,
                    givenSpeed,
                    givenHdop,
                    givenAnalogInputs
            );
            assertEquals(expected, actual);

            assertEquals(0, givenBuffer.readableBytes());
        }
    }
}
