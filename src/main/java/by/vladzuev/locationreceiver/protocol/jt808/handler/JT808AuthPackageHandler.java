package by.vladzuev.locationreceiver.protocol.jt808.handler;

import by.vladzuev.locationreceiver.protocol.core.handler.packages.ignored.IgnoredPackageHandler;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808AuthPackage;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808ResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class JT808AuthPackageHandler extends IgnoredPackageHandler<JT808AuthPackage> {

    public JT808AuthPackageHandler() {
        super(JT808AuthPackage.class);
    }

    @Override
    protected JT808ResponsePackage createResponse() {
        throw new UnsupportedOperationException();
    }
}
