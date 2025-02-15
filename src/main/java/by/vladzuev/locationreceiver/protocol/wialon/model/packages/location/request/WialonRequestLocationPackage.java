package by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.request;

import by.vladzuev.locationreceiver.protocol.wialon.model.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.WialonPackage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class WialonRequestLocationPackage implements WialonPackage {
    private final List<WialonLocation> locations;
}
