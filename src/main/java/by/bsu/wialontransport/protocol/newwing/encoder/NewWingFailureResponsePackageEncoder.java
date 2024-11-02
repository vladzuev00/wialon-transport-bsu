package by.bsu.wialontransport.protocol.newwing.encoder;

import by.bsu.wialontransport.protocol.newwing.model.response.NewWingFailureResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingFailureResponsePackageEncoder extends NewWingPackageEncoder<NewWingFailureResponsePackage> {

    public NewWingFailureResponsePackageEncoder() {
        super(NewWingFailureResponsePackage.class);
    }
}
