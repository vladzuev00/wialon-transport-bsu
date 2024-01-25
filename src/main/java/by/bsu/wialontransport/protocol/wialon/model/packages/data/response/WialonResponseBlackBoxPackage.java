package by.bsu.wialontransport.protocol.wialon.model.packages.data.response;

import by.bsu.wialontransport.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonResponseBlackBoxPackage extends WialonPackage {
    public static final String PREFIX = "#AB#";

    private final int amountFixedMessages;

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
