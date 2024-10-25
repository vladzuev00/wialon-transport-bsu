package by.bsu.wialontransport.protocol.jt808.decoder;

import by.bsu.wialontransport.protocol.jt808.model.JT808AuthenticationPackage;
import io.netty.buffer.ByteBuf;
import org.springframework.stereotype.Component;

@Component
public final class JT808AuthenticationPackageDecoder extends JT808PackageDecoder {
    private static final byte[] REQUIRED_PREFIX = {126, 1, 2};

    public JT808AuthenticationPackageDecoder() {
        super(REQUIRED_PREFIX);
    }

    @Override
    protected JT808AuthenticationPackage decodeInternal(final ByteBuf buffer, final String phoneNumber) {
        return new JT808AuthenticationPackage();
    }
}
