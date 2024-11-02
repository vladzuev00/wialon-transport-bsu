package by.bsu.wialontransport.protocol.newwing.encoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingSuccessResponsePackageEncoder extends NewWingPackageEncoder<NewWingSuccessResponsePackage> {

    public NewWingSuccessResponsePackageEncoder() {
        super(NewWingSuccessResponsePackage.class);
    }
}
