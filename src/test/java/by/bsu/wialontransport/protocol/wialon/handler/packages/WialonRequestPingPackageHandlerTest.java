package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonRequestPingPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.ping.WialonResponsePingPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class WialonRequestPingPackageHandlerTest {
    private final WialonRequestPingPackageHandler handler = new WialonRequestPingPackageHandler();

    @Test
    public void packageShouldBeHandledInternally() {
        final WialonRequestPingPackage givenRequest = new WialonRequestPingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package actual = handler.handleInternal(givenRequest, givenContext);
        assertNotNull(actual);
        assertTrue(actual instanceof WialonResponsePingPackage);
    }
}
