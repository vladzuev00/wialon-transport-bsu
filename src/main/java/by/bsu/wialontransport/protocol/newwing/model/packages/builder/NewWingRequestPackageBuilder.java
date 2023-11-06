package by.bsu.wialontransport.protocol.newwing.model.packages.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.NewWingRequestPackage;
import lombok.Setter;

@Setter
public abstract class NewWingRequestPackageBuilder<P extends NewWingRequestPackage> {
    private int checksum;

    public final P build() {
        return this.build(this.checksum);
    }

    protected abstract P build(final int checksum);
}