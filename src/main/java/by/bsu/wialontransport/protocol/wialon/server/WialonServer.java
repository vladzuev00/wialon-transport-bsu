package by.bsu.wialontransport.protocol.wialon.server;

import by.bsu.wialontransport.protocol.wialon.decoder.WialonDecoder;
import by.bsu.wialontransport.protocol.wialon.encoder.WialonEncoder;
import by.bsu.wialontransport.protocol.wialon.handler.WialonExceptionHandler;
import by.bsu.wialontransport.protocol.wialon.handler.WialonHandler;
import by.bsu.wialontransport.protocol.wialon.server.exception.WialonRunningServerException;
import by.bsu.wialontransport.protocol.wialon.server.exception.WialonServerShutdownException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@RequiredArgsConstructor
public final class WialonServer implements AutoCloseable {
    private final EventLoopGroup connectionLoopGroup;
    private final EventLoopGroup dataProcessLoopGroup;
    private final int port;
    private final WialonDecoder decoder;
    private final ReadTimeoutHandler readTimeoutHandler;
    private final WialonEncoder encoder;
    private final WialonHandler requestHandler;
    private final WialonExceptionHandler exceptionHandler;

    public void run() {
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.connectionLoopGroup, this.dataProcessLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(this.createChannelInitializer());
            final ChannelFuture channelFuture = serverBootstrap.bind()
                    .sync();
            channelFuture.channel()
                    .closeFuture()
                    .sync();
        } catch (final InterruptedException cause) {
            throw new WialonRunningServerException(cause);
        }
    }

    @Override
    public void close() {
        try {
            this.connectionLoopGroup.shutdownGracefully().sync();
            this.dataProcessLoopGroup.shutdownGracefully().sync();
        } catch (final InterruptedException cause) {
            throw new WialonServerShutdownException(cause);
        }
    }

    private ChannelInitializer<SocketChannel> createChannelInitializer() {
        return new ChannelInitializer<>() {

            @Override
            public void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        .addLast(decoder, readTimeoutHandler, encoder, requestHandler, exceptionHandler);
            }

        };
    }
}
