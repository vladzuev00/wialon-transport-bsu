package by.vladzuev.locationreceiver.protocol.teltonika.decoder.location;

import by.vladzuev.locationreceiver.protocol.teltonika.decoder.TeltonikaPackageDecoder;
import by.vladzuev.locationreceiver.protocol.teltonika.holder.LoginSuccessHolder;
import by.vladzuev.locationreceiver.protocol.teltonika.model.location.TeltonikaRequestLocationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
public final class TeltonikaRequestLocationPackageDecoder extends TeltonikaPackageDecoder {
    private final TeltonikaLocationDecoder locationDecoder;

    public TeltonikaRequestLocationPackageDecoder(final LoginSuccessHolder loginSuccessHolder,
                                                  final TeltonikaLocationDecoder locationDecoder) {
        super(loginSuccessHolder, true);
        this.locationDecoder = locationDecoder;
    }

    @Override
    protected TeltonikaRequestLocationPackage decodeInternal(final ByteBuf buffer) {
        final byte locationCount = buffer.readByte();
        return range(0, locationCount)
                .mapToObj(i -> locationDecoder.decode(buffer))
                .collect(collectingAndThen(toList(), TeltonikaRequestLocationPackage::new));
    }
}
