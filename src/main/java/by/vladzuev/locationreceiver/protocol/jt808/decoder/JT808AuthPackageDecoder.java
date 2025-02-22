package by.vladzuev.locationreceiver.protocol.jt808.decoder;

import by.vladzuev.locationreceiver.protocol.jt808.model.JT808AuthPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808AuthPackageDecoder extends JT808PackageDecoder {
    private static final byte[] PREFIX = {126, 1, 2};

    public JT808AuthPackageDecoder() {
        super(PREFIX);
    }

    @Override
    protected JT808AuthPackage decodeInternal(final ByteBuf buffer, final String phoneNumber) {
        return new JT808AuthPackage();
    }
}
