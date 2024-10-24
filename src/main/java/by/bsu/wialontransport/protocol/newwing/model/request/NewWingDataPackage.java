package by.bsu.wialontransport.protocol.newwing.model.request;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.protocol.core.model.packages.DataPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class NewWingDataPackage extends NewWingRequestPackage implements DataPackage {
    private final List<Location> data;

    public NewWingDataPackage(final int checksum, final List<Location> data) {
        super(checksum);
        this.data = data;
    }
}
