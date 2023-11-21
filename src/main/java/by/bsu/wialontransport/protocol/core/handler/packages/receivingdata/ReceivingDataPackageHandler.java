package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.DataPackage;

public abstract class ReceivingDataPackageHandler<PACKAGE extends DataPackage> extends PackageHandler<PACKAGE> {


    public ReceivingDataPackageHandler(final Class<PACKAGE> handledPackageType) {
        super(handledPackageType);
    }

}
