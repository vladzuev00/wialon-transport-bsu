package by.vladzuev.locationreceiver.protocol.teltonika.model.login;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class TeltonikaResponseLoginPackage {
    private final byte value;
}
