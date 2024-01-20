package by.bsu.wialontransport.protocol.newwing.encoder;

import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.newwing.encoder.packages.NewWingFailureResponsePackageEncoder;
import by.bsu.wialontransport.protocol.newwing.encoder.packages.NewWingSuccessResponsePackageEncoder;

import java.util.List;

public final class NewWingProtocolEncoder extends ProtocolEncoder {

    public NewWingProtocolEncoder(final NewWingSuccessResponsePackageEncoder successResponsePackageEncoder,
                                  final NewWingFailureResponsePackageEncoder failureResponsePackageEncoder) {
        super(List.of(successResponsePackageEncoder, failureResponsePackageEncoder));
    }
}
