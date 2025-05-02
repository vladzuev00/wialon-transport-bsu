package by.vladzuev.locationreceiver.protocol.newwing.decoder.location;

import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingLocation;
import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.vladzuev.locationreceiver.protocol.newwing.util.NewWingUtil.*;
import static io.netty.buffer.ByteBufUtil.decodeHexDump;
import static io.netty.buffer.Unpooled.wrappedBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

public final class NewWingLocationDecoderTest {
    private final NewWingLocationDecoder decoder = new NewWingLocationDecoder();

    @Test
    public void locationShouldBeDecoded() {
        try (final MockedStatic<NewWingUtil> mockedUtil = mockStatic(NewWingUtil.class)) {
            final ByteBuf givenBuffer = wrappedBuffer(decodeHexDump("00000000"));

            final LocalTime givenTime = LocalTime.of(10, 20, 30);
            mockedUtil.when(() -> decodeTime(same(givenBuffer))).thenReturn(givenTime);

            final double givenLatitude = 5.5;
            mockedUtil.when(() -> decodeLatitude(same(givenBuffer))).thenReturn(givenLatitude);

            final double givenLongitude = 6.6;
            mockedUtil.when(() -> decodeLongitude(same(givenBuffer))).thenReturn(givenLongitude);

            final double givenHdop = 9;
            mockedUtil.when(() -> decodeHdop(same(givenBuffer))).thenReturn(givenHdop);

            final short givenCourse = 7;
            mockedUtil.when(() -> decodeCourse(same(givenBuffer))).thenReturn(givenCourse);

            final double givenSpeed = 8;
            mockedUtil.when(() -> decodeSpeed(same(givenBuffer))).thenReturn(givenSpeed);

            final LocalDate givenDate = LocalDate.of(2024, 10, 28);
            mockedUtil.when(() -> decodeDate(same(givenBuffer))).thenReturn(givenDate);

            final double[] givenAnalogInputs = {11, 12, 13, 14};
            mockedUtil.when(() -> decodeAnalogInputs(same(givenBuffer))).thenReturn(givenAnalogInputs);

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

            mockedUtil.verify(() -> skipFlagByte(same(givenBuffer)), times(1));
            mockedUtil.verify(() -> skipDiscreteInputStateByte(same(givenBuffer)), times(1));
            mockedUtil.verify(() -> skipChecksum(same(givenBuffer)), times(1));
        }
    }
}
