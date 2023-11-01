package by.bsu.wialontransport.protocol.newwing.model.packages.builder;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import by.bsu.wialontransport.protocol.newwing.model.packages.DataNewWingPackage;
import lombok.Setter;

import java.util.List;

@Setter
public final class DataNewWingPackageBuilder extends NewWingPackageBuilder<DataNewWingPackage> {
    private List<NewWingData> data;

    @Override
    protected DataNewWingPackage build(final int checksum) {
        return new DataNewWingPackage(checksum, this.data);
    }
}