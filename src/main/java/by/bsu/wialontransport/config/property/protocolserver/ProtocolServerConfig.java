package by.bsu.wialontransport.config.property.protocolserver;

import by.bsu.wialontransport.validation.annotation.Host;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.InetSocketAddress;

@Getter
public abstract class ProtocolServerConfig {
    private final InetSocketAddress inetSocketAddress;
    private final EventLoopGroup loopGroupProcessingConnection;
    private final EventLoopGroup loopGroupProcessingData;
    private final int connectionLifeTimeoutSeconds;

    public ProtocolServerConfig(@Host final String host,
                                @Min(1) @Max(65535) final int port,
                                @Min(1) @Max(255) final int threadCountProcessingConnection,
                                @Min(1) @Max(255) final int threadCountProcessingData,
                                @Min(1) @Max(600) final int connectionLifeTimeoutSeconds) {
        inetSocketAddress = new InetSocketAddress(host, port);
        loopGroupProcessingConnection = new NioEventLoopGroup(threadCountProcessingConnection);
        loopGroupProcessingData = new NioEventLoopGroup(threadCountProcessingData);
        this.connectionLifeTimeoutSeconds = connectionLifeTimeoutSeconds;
    }
}
