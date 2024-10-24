package by.bsu.wialontransport.protocol.newwing.model.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class NewWingRequestPackage {
    private final int checksum;
}
