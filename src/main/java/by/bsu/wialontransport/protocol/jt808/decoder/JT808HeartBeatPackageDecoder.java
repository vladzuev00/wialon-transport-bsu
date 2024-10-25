package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808HeartBeatPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808HeartBeatPackageDecoder extends JT808PackageDecoder {
    private static final byte[] REQUIRED_PREFIX = {126, 0, 2};

    public JT808HeartBeatPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected JT808HeartBeatPackage decodeInternal(final ByteBuf buffer, final String phoneNumber) {
        return new JT808HeartBeatPackage();
    }
}
