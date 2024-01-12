package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import lombok.Setter;

@Setter
public abstract class NewWingRequestPackageBuilder<P extends NewWingRequestPackage> {
    private int checksum;

    public final P build() {
        return build(checksum);
    }

    protected abstract P build(final int checksum);
}