package by.bsu.wialontransport.protocol.core.handler.packages;

import io.netty.channel.ChannelHandlerContext;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static java.lang.Integer.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class PackageHandlerTest {
    private final TestPackageHandler handler = new TestPackageHandler();

    @Test
    public void handlerShouldBeAbleHandleRequest() {
        final TestPackage givenRequest = new TestPackage(MAX_VALUE);

        assertTrue(handler.isAbleHandle(givenRequest));
    }

    @Test
    public void handlerShouldNotBeAbleHandleRequest() {
        final Object givenRequest = new Object();

        assertFalse(handler.isAbleHandle(givenRequest));
    }

    @Test
    public void requestShouldBeHandled() {
        final TestPackage givenRequest = new TestPackage(5);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        handler.handle(givenRequest, givenContext);

        final TestPackage expectedResponse = new TestPackage(6);
        verify(givenContext, times(1)).writeAndFlush(eq(expectedResponse));
    }

    @Test
    public void requestShouldNotBeHandled() {
        final Object givenRequest = new Object();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        assertThrows(ClassCastException.class, () -> handler.handle(givenRequest, givenContext));

        verifyNoInteractions(givenContext);
    }

    @Value
    private static class TestPackage {
        int value;
    }

    private static final class TestPackageHandler extends PackageHandler<TestPackage> {

        public TestPackageHandler() {
            super(TestPackage.class);
        }

        @Override
        protected TestPackage handleInternal(final TestPackage request, final ChannelHandlerContext context) {
            return new TestPackage(request.getValue() + 1);
        }
    }
}
