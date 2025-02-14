package by.bsu.wialontransport.protocol.newwing.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class NewWingResponsePackage {
    private final String value;
}
