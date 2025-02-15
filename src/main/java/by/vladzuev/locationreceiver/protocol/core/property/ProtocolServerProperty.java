package by.vladzuev.locationreceiver.protocol.core.property;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class ProtocolServerProperty {
    private final String host;
    private final Integer port;
    private final Integer threadCountProcessingConnection;
    private final Integer threadCountProcessingData;
    private final Integer connectionLifeTimeoutSeconds;
}
