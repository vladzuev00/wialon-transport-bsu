package by.vladzuev.locationreceiver.protocol.teltonika.encoder.login;

import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseFailedLoginPackage;
import org.springframework.stereotype.Component;

@Component
public final class TeltonikaFailedLoginPackageEncoder extends TeltonikaLoginPackageEncoder<TeltonikaResponseFailedLoginPackage> {

    public TeltonikaFailedLoginPackageEncoder() {
        super(TeltonikaResponseFailedLoginPackage.class);
    }
}
