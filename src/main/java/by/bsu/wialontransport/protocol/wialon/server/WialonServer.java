package by.bsu.wialontransport.protocol.wialon.server;

import by.bsu.wialontransport.protocol.wialon.tempdecoder.WialonDecoder;
import by.bsu.wialontransport.protocol.wialon.encoder.WialonEncoder;
import by.bsu.wialontransport.protocol.wialon.temphandler.WialonExceptionHandler;
import by.bsu.wialontransport.protocol.wialon.temphandler.WialonHandler;
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
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public final class WialonServer implements AutoCloseable {
    private final EventLoopGroup loopGroupProcessingConnection;
    private final EventLoopGroup loopGroupProcessingData;
    private final int port;
    private final Supplier<WialonDecoder> decoderFactory;
    private final Supplier<ReadTimeoutHandler> readTimeoutHandlerFactory;
    private final Supplier<WialonEncoder> encoderFactory;
    private final Supplier<WialonHandler> requestHandlerFactory;
    private final Supplier<WialonExceptionHandler> exceptionHandlerFactory;

    public void run() {
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.loopGroupProcessingConnection, this.loopGroupProcessingData)
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
            this.loopGroupProcessingConnection.shutdownGracefully().sync();
            this.loopGroupProcessingData.shutdownGracefully().sync();
        } catch (final InterruptedException cause) {
            throw new WialonServerShutdownException(cause);
        }
    }

    private ChannelInitializer<SocketChannel> createChannelInitializer() {
        return new ChannelInitializer<>() {

            @Override
            public void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(
                        decoderFactory.get(),
                        readTimeoutHandlerFactory.get(),
                        encoderFactory.get(),
                        requestHandlerFactory.get(),
                        exceptionHandlerFactory.get()
                );
            }

        };
    }
}
