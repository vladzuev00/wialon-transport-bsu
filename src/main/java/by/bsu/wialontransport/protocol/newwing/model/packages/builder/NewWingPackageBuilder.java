package by.bsu.wialontransport.protocol.newwing.model.packages.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.NewWingPackage;
import lombok.Setter;

@Setter
public abstract class NewWingPackageBuilder<P extends NewWingPackage> {
    private int checksum;

    public final P build() {
        return this.build(this.checksum);
    }

    protected abstract P build(final int checksum);
}