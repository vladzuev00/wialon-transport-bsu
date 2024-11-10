package by.bsu.wialontransport.protocol.core.handler.packages.ignored;

import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public final class IgnoredPackageHandlerTest {
    private final TestIgnoredPackageHandler handler = new TestIgnoredPackageHandler();

    @Test
    public void requestShouldBeHandledInternally() {
        final Object givenRequest = new Object();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        assertTrue(actual instanceof TestPackage);
    }

    private static class TestPackage {

    }

    private static final class TestIgnoredPackageHandler extends IgnoredPackageHandler<Object> {

        public TestIgnoredPackageHandler() {
            super(Object.class);
        }

        @Override
        protected Object createResponse() {
            return new TestPackage();
        }
    }
}
