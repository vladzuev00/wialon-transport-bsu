package by.bsu.wialontransport.protocol.core.handler.packages.login.factory;

import by.bsu.wialontransport.protocol.core.model.login.LoginPackage;
import org.springframework.stereotype.Component;

@Component
public final class TrackerImeiFactory {
    private static final String IMEI_TEMPLATE = "%20s";
    private static final char SPACE = ' ';
    private static final char ZERO = '0';

    public String create(final LoginPackage request) {
        return IMEI_TEMPLATE.formatted(request.getImei()).replace(SPACE, ZERO);
    }
}
