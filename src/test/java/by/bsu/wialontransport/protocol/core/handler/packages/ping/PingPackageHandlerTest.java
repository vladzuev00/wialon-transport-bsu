package by.bsu.wialontransport.protocol.core.handler.packages.ping;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public final class PingPackageHandlerTest {
    private final TestPingPackageHandler handler = new TestPingPackageHandler();

    @Test
    public void requestShouldBeHandledInternally() {
        final Object givenRequest = new Object();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        assertTrue(actual instanceof TestPackage);
    }

    private static class TestPackage {

    }

    private static final class TestPingPackageHandler extends PingPackageHandler<Object> {

        public TestPingPackageHandler() {
            super(Object.class);
        }

        @Override
        protected Object createResponse() {
            return new TestPackage();
        }
    }
}
