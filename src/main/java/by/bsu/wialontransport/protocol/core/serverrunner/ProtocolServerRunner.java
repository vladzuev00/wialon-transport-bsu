package by.bsu.wialontransport.protocol.core.serverrunner;

import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.exceptionhandler.ProtocolExceptionHandler;
import by.bsu.wialontransport.protocol.core.model.ProtocolServer;
import by.bsu.wialontransport.protocol.core.property.ProtocolServerProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public final class ProtocolServerRunner {
    private final List<ProtocolServer> servers;
    private final ContextAttributeManager contextAttributeManager;

    public void run() {
        servers.stream()
                .map(server -> new Thread(() -> run(server)))
                .forEach(Thread::start);
    }

    private void run(final ProtocolServer server) {
        final ProtocolServerProperty property = server.getProperty();
        try (final EventLoops eventLoops = new EventLoops(property)) {
            new ServerBootstrap()
                    .group(eventLoops.getParentGroup(), eventLoops.getChildGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(createChildHandler(server))
                    .localAddress(new InetSocketAddress(property.getHost(), property.getPort()))
                    .bind(property.getPort())
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
        }
    }

    private ChannelHandler createChildHandler(final ProtocolServer server) {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(final SocketChannel channel) {
                channel.pipeline()
                        .addLast(
                                server.getDecoder(),
                                new ReadTimeoutHandler(server.getProperty().getConnectionLifeTimeoutSeconds(), SECONDS),
                                server.getEncoder(),
                                server.getHandler(),
                                new ProtocolExceptionHandler(contextAttributeManager)
                        );
            }
        };
    }


    @Getter
    private static final class EventLoops implements AutoCloseable {
        private final EventLoopGroup parentGroup;
        private final EventLoopGroup childGroup;

        public EventLoops(final ProtocolServerProperty property) {
            parentGroup = new NioEventLoopGroup(property.getThreadCountProcessingConnection());
            childGroup = new NioEventLoopGroup(property.getThreadCountProcessingData());
        }

        @Override
        public void close() {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
