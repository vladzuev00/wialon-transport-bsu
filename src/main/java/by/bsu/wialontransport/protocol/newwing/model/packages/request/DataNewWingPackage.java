package by.bsu.wialontransport.protocol.newwing.model.packages.request;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import lombok.Getter;

import java.util.List;

@Getter
public final class DataNewWingPackage extends NewWingRequestPackage {
    private final List<NewWingData> data;

    public DataNewWingPackage(final int checksum, final List<NewWingData> data) {
        super(checksum);
        this.data = data;
    }
}
