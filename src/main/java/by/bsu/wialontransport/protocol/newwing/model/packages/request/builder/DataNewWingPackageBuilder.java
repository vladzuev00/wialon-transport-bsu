package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.DataNewWingPackage;
import lombok.Setter;

import java.util.List;

@Setter
public final class DataNewWingPackageBuilder extends NewWingRequestPackageBuilder<DataNewWingPackage> {
    private List<NewWingData> data;

    @Override
    protected DataNewWingPackage build(final int checksum) {
        return new DataNewWingPackage(checksum, this.data);
    }
}