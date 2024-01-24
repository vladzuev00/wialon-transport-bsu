package by.bsu.wialontransport.configuration.property;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.InetSocketAddress;

@Getter
@EqualsAndHashCode
@ToString
public abstract class ProtocolServerConfiguration {
    private final InetSocketAddress inetSocketAddress;
    private final EventLoopGroup loopGroupProcessingConnection;
    private final EventLoopGroup loopGroupProcessingData;
    private final int connectionLifeTimeoutSeconds;

    public ProtocolServerConfiguration(final String host,
                                       final int port,
                                       final int threadCountProcessingConnection,
                                       final int threadCountProcessingData,
                                       final int connectionLifeTimeoutSeconds) {
        inetSocketAddress = new InetSocketAddress(host, port);
        loopGroupProcessingConnection = new NioEventLoopGroup(threadCountProcessingConnection);
        loopGroupProcessingData = new NioEventLoopGroup(threadCountProcessingData);
        this.connectionLifeTimeoutSeconds = connectionLifeTimeoutSeconds;
    }
}
