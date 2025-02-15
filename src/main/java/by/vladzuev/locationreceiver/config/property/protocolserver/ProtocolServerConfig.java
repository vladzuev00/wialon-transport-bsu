package by.vladzuev.locationreceiver.config.property.protocolserver;

import by.vladzuev.locationreceiver.validation.annotation.Host;
import by.vladzuev.locationreceiver.validation.annotation.Port;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class ProtocolServerConfig {

    @Host
    private final String host;

    @Port
    private final Integer port;

    @NotNull
    @Min(1)
    @Max(255)
    private final Integer threadCountProcessingConnection;

    @NotNull
    @Min(1)
    @Max(255)
    private final Integer threadCountProcessingData;

    @NotNull
    @Min(1)
    @Max(600)
    private final Integer connectionLifeTimeoutSeconds;
}
