package by.bsu.wialontransport.protocol.packages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class LoginPackage implements Package {
    private final String imei;
}
