package by.bsu.wialontransport.protocol.wialon.temphandler.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public final class FinisherPackageHandlerTest {
    private final FinisherPackageHandler handler;

    public FinisherPackageHandlerTest() {
        this.handler = new FinisherPackageHandler();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void packageShouldNotBeHandledIndependently() {
        final WialonPackage givenPackage = new WialonPackage() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.handler.handleIndependently(givenPackage, givenContext);
    }
}
