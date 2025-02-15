package by.vladzuev.locationreceiver.protocol.newwing.encoder;

import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingFailureResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingFailureResponsePackageEncoder extends NewWingPackageEncoder<NewWingFailureResponsePackage> {

    public NewWingFailureResponsePackageEncoder() {
        super(NewWingFailureResponsePackage.class);
    }
}
