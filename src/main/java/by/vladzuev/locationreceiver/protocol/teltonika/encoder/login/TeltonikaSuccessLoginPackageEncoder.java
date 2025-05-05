package by.vladzuev.locationreceiver.protocol.teltonika.encoder.login;

import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseSuccessLoginPackage;
import org.springframework.stereotype.Component;

@Component
public final class TeltonikaSuccessLoginPackageEncoder extends TeltonikaLoginPackageEncoder<TeltonikaResponseSuccessLoginPackage> {

    public TeltonikaSuccessLoginPackageEncoder() {
        super(TeltonikaResponseSuccessLoginPackage.class);
    }
}
