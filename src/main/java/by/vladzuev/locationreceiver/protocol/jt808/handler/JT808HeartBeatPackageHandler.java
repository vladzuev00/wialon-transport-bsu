package by.vladzuev.locationreceiver.protocol.jt808.handler;

import by.vladzuev.locationreceiver.protocol.core.handler.packages.ignored.IgnoredPackageHandler;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808HeartBeatPackage;
import by.vladzuev.locationreceiver.protocol.jt808.model.JT808ResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class JT808HeartBeatPackageHandler extends IgnoredPackageHandler<JT808HeartBeatPackage> {

    public JT808HeartBeatPackageHandler() {
        super(JT808HeartBeatPackage.class);
    }

    @Override
    protected JT808ResponsePackage createResponse() {
        throw new UnsupportedOperationException();
    }
}
