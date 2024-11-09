package by.bsu.wialontransport.protocol.core.model.packages.login;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class LoginPackage {
    private final String imei;
}
