package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class WialonResponseBlackBoxPackage extends WialonPackage {
    private static final String PREFIX = "#AB#";

    private final int amountFixedMessages;

    @Override
    public String findPrefix() {
        return PREFIX;
    }
}
