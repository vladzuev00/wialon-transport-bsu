package by.bsu.wialontransport.protocol.wialon.model.packages.data.request;

import by.bsu.wialontransport.protocol.wialon.model.WialonData;

import java.util.List;

public final class WialonRequestBlackBoxPackage extends AbstractWialonRequestDataPackage {
    public static final String PREFIX = "#B#";

    public WialonRequestBlackBoxPackage(final List<WialonData> data) {
        super(data);
    }

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
