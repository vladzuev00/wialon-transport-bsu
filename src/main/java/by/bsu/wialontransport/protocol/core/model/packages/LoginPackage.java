package by.bsu.wialontransport.protocol.core.model.packages;

import java.util.Optional;

public interface LoginPackage extends Package {
    String getImei();
    Optional<String> findPassword();
}
