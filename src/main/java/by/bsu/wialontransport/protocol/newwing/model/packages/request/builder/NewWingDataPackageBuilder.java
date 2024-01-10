package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
import lombok.Setter;

import java.util.List;

@Setter
public final class NewWingDataPackageBuilder extends NewWingRequestPackageBuilder<NewWingDataPackage> {
    private List<NewWingData> data;

    @Override
    protected NewWingDataPackage build(final int checksum) {
        return new NewWingDataPackage(checksum, this.data);
    }
}