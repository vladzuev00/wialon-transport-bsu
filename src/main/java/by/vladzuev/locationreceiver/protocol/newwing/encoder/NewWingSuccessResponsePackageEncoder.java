package by.vladzuev.locationreceiver.protocol.newwing.encoder;

import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingSuccessResponsePackageEncoder extends NewWingPackageEncoder<NewWingSuccessResponsePackage> {

    public NewWingSuccessResponsePackageEncoder() {
        super(NewWingSuccessResponsePackage.class);
    }
}
