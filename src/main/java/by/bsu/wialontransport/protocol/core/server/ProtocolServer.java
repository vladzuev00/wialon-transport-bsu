package by.bsu.wialontransport.protocol.core.server;

import by.bsu.wialontransport.configuration.property.ProtocolServerConfiguration;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.exceptionhandler.ProtocolExceptionHandler;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class ProtocolServer {
    private final InetSocketAddress inetSocketAddress;
    private final ContextAttributeManager contextAttributeManager;
    private final EventLoopGroup loopGroupProcessingConnection;
    private final EventLoopGroup loopGroupProcessingData;
    private final int connectionLifeTimeoutSeconds;

    public ProtocolServer(final ContextAttributeManager contextAttributeManager,
                          final ProtocolServerConfiguration configuration) {
        this.contextAttributeManager = contextAttributeManager;
        inetSocketAddress = new InetSocketAddress(configuration.getHost(), configuration.getPort());
        loopGroupProcessingConnection = new NioEventLoopGroup(configuration.getThreadCountProcessingConnection());
        loopGroupProcessingData = new NioEventLoopGroup(configuration.getThreadCountProcessingData());
        connectionLifeTimeoutSeconds = configuration.getConnectionLifeTimeoutSeconds();
    }

    public final void run() {
        try {
            createServerBootstrap()
                    .bind()
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (final InterruptedException cause) {
            throw new ServerRunningException(cause);
        }
    }

    public final void stop() {
        try {
            loopGroupProcessingConnection.shutdownGracefully().sync();
            loopGroupProcessingData.shutdownGracefully().sync();
        } catch (final InterruptedException cause) {
            throw new ServerStoppingException(cause);
        }
    }

    protected abstract ProtocolDecoder<?, ?> createDecoder();

    protected abstract ProtocolEncoder createEncoder();

    protected abstract ProtocolHandler createHandler();

    private ServerBootstrap createServerBootstrap() {
        return new ServerBootstrap()
                .group(loopGroupProcessingConnection, loopGroupProcessingData)
                .channel(NioServerSocketChannel.class)
                .localAddress(inetSocketAddress)
                .childHandler(createChannelInitializer());
    }

    private ChannelInitializer<SocketChannel> createChannelInitializer() {
        return new ChannelInitializer<>() {

            @Override
            public void initChannel(final SocketChannel channel) {
                channel.pipeline()
                        .addLast(
                                createDecoder(),
                                createReadTimeoutHandler(),
                                createEncoder(),
                                createHandler(),
                                createExceptionHandler()
                        );
            }

        };
    }

    private ReadTimeoutHandler createReadTimeoutHandler() {
        return new ReadTimeoutHandler(connectionLifeTimeoutSeconds, SECONDS);
    }

    private ProtocolExceptionHandler createExceptionHandler() {
        return new ProtocolExceptionHandler(contextAttributeManager);
    }

    static final class ServerRunningException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerRunningException() {

        }

        @SuppressWarnings("unused")
        public ServerRunningException(final String description) {
            super(description);
        }

        public ServerRunningException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerRunningException(final String description, final Exception cause) {
            super(description, cause);
        }
    }

    static final class ServerStoppingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ServerStoppingException() {

        }

        @SuppressWarnings("unused")
        public ServerStoppingException(final String description) {
            super(description);
        }

        public ServerStoppingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ServerStoppingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
