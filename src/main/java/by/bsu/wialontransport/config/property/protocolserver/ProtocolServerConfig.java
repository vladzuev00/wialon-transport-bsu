package by.bsu.wialontransport.config.property.protocolserver;

import by.bsu.wialontransport.validation.annotation.Host;
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

    @NotNull
    @Min(1)
    @Max(65535)
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
