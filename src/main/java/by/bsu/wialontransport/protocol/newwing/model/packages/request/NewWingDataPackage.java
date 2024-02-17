package by.bsu.wialontransport.protocol.newwing.model.packages.request;

import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class NewWingDataPackage extends NewWingRequestPackage {
    private final List<NewWingData> data;

    public NewWingDataPackage(final int checksum, final List<NewWingData> data) {
        super(checksum);
        this.data = data;
    }
}
