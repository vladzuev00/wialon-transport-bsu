package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808Location;
import by.bsu.wialontransport.protocol.jt808.model.JT808LocationPackage;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.protocol.jt808.util.JT808Util.*;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public abstract class JT808LocationPackageDecoder extends JT808PackageDecoder {
    private static final int EXTRA_DATA_BYTE_COUNT = 38;

    public JT808LocationPackageDecoder(final byte[] requiredPrefix) {
        super(requiredPrefix);
    }

    @Override
    protected final JT808LocationPackage decodeInternal(final ByteBuf buffer, final String phoneNumber) {
        final int locationCount = decodeLocationCount(buffer);
        skipUntilFirstLocation(buffer);
        return decodeLocations(buffer, locationCount);
    }

    protected abstract int decodeLocationCount(final ByteBuf buffer);

    protected abstract void skipUntilFirstLocation(final ByteBuf buffer);

    protected abstract void skipLocationPrefix(final ByteBuf buffer);

    private JT808LocationPackage decodeLocations(final ByteBuf buffer, final int count) {
        return range(0, count)
                .mapToObj(i -> decodeLocation(buffer))
                .collect(collectingAndThen(toList(), JT808LocationPackage::new));
    }

    private JT808Location decodeLocation(final ByteBuf buffer) {
        skipLocationPrefix(buffer);
        skipAlarmSign(buffer);
        skipStatus(buffer);
        final double latitude = decodeLatitude(buffer);
        final double longitude = decodeLongitude(buffer);
        final short altitude = decodeAltitude(buffer);
        final short speed = decodeSpeed(buffer);
        final short course = decodeCourse(buffer);
        final LocalDateTime dateTime = decodeDateTime(buffer);
        skipExtraData(buffer);
        return new JT808Location(dateTime, latitude, longitude, altitude, speed, course);
    }

    private void skipAlarmSign(final ByteBuf buffer) {
        buffer.skipBytes(Integer.BYTES);
    }

    private void skipStatus(final ByteBuf buffer) {
        buffer.skipBytes(Integer.BYTES);
    }

    private short decodeAltitude(final ByteBuf buffer) {
        return buffer.readShort();
    }

    private short decodeSpeed(final ByteBuf buffer) {
        return buffer.readShort();
    }

    private short decodeCourse(final ByteBuf buffer) {
        return buffer.readShort();
    }

    private void skipExtraData(final ByteBuf buffer) {
        buffer.skipBytes(EXTRA_DATA_BYTE_COUNT);
    }
}
