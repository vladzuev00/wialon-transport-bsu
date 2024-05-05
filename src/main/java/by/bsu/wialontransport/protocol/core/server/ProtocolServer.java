package by.bsu.wialontransport.protocol.core.server;

import by.bsu.wialontransport.config.property.protocolserver.ProtocolServerConfig;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.decoder.ProtocolDecoder;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import by.bsu.wialontransport.protocol.core.encoder.packages.PackageEncoder;
import by.bsu.wialontransport.protocol.core.exceptionhandler.ProtocolExceptionHandler;
import by.bsu.wialontransport.protocol.core.handler.ProtocolHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;
import java.util.List;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

public abstract class ProtocolServer<PACKAGE_DECODER extends PackageDecoder<?, ?>, PACKAGE_ENCODER extends PackageEncoder<?>> {
    private final InetSocketAddress inetSocketAddress;
    private final EventLoopGroup loopGroupProcessingConnection;
    private final EventLoopGroup loopGroupProcessingData;
    private final int connectionLifeTimeoutSeconds;
    private final ServerRunningContext runningContext;

    public ProtocolServer(final ProtocolServerConfig config,
                          final ContextAttributeManager contextAttributeManager,
                          final ConnectionManager connectionManager,
                          final List<PACKAGE_DECODER> packageDecoders,
                          final List<PACKAGE_ENCODER> packageEncoders,
                          final List<? extends PackageHandler<?>> packageHandlers) {
        inetSocketAddress = new InetSocketAddress(config.getHost(), config.getPort());
        loopGroupProcessingConnection = new NioEventLoopGroup(config.getThreadCountProcessingConnection());
        loopGroupProcessingData = new NioEventLoopGroup(config.getThreadCountProcessingData());
        connectionLifeTimeoutSeconds = config.getConnectionLifeTimeoutSeconds();
        runningContext = new ServerRunningContext(
                contextAttributeManager,
                connectionManager,
                packageDecoders,
                packageEncoders,
                packageHandlers
        );
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
            currentThread().interrupt();
        }
    }

    public final void stop() {
        shutdown(loopGroupProcessingConnection);
        shutdown(loopGroupProcessingData);
    }

    protected abstract ProtocolDecoder<?, ?> createProtocolDecoder(final ServerRunningContext context);

    protected abstract ProtocolEncoder createProtocolEncoder(final ServerRunningContext context);

    protected abstract ProtocolHandler createProtocolHandler(final ServerRunningContext context);

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
                                createProtocolDecoder(runningContext),
                                createReadTimeoutHandler(),
                                createProtocolEncoder(runningContext),
                                createProtocolHandler(runningContext),
                                createExceptionHandler()
                        );
            }
        };
    }

    private ReadTimeoutHandler createReadTimeoutHandler() {
        return new ReadTimeoutHandler(connectionLifeTimeoutSeconds, SECONDS);
    }

    private ProtocolExceptionHandler createExceptionHandler() {
        return new ProtocolExceptionHandler(runningContext.contextAttributeManager);
    }

    private static void shutdown(final EventLoopGroup eventLoopGroup) {
        try {
            eventLoopGroup.shutdownGracefully().sync();
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
        }
    }

    @RequiredArgsConstructor
    @Getter
    public final class ServerRunningContext {
        private final ContextAttributeManager contextAttributeManager;
        private final ConnectionManager connectionManager;
        private final List<PACKAGE_DECODER> packageDecoders;
        private final List<PACKAGE_ENCODER> packageEncoders;
        private final List<? extends PackageHandler<?>> packageHandlers;
    }
}
