//package by.bsu.wialontransport.protocol.core.handler.packages;
//
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import io.netty.channel.ChannelHandlerContext;
//import lombok.Getter;
//import lombok.Setter;
//import org.junit.Test;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.*;
//
//public final class PackageHandlerTest {
//    private static final Package GIVEN_RESPONSE = new Package() {
//    };
//
//    private final TestPackageHandler handler = new TestPackageHandler();
//
//    @Test
//    public void handlerShouldBeAbleToHandle() {
//        final Package givenRequest = new TestPackage();
//
//        final boolean actual = handler.isAbleToHandle(givenRequest);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void handlerShouldNotBeAbleToHandle() {
//        final Package givenRequest = new Package() {
//        };
//
//        final boolean actual = handler.isAbleToHandle(givenRequest);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void requestShouldBeHandled() {
//        final Package givenRequest = new TestPackage();
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        handler.handle(givenRequest, givenContext);
//
//        verify(givenContext, times(1)).writeAndFlush(same(GIVEN_RESPONSE));
//    }
//
//    @Test(expected = ClassCastException.class)
//    public void requestShouldNotBeHandledBecauseOfNotSuitableType() {
//        final Package givenRequest = new Package() {
//        };
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        handler.handle(givenRequest, givenContext);
//    }
//
//    private static final class TestPackage implements Package {
//
//    }
//
//    @Setter
//    @Getter
//    private static final class TestPackageHandler extends PackageHandler<TestPackage> {
//
//        public TestPackageHandler() {
//            super(TestPackage.class);
//        }
//
//        @Override
//        protected Package handleInternal(final TestPackage request, final ChannelHandlerContext context) {
//            return GIVEN_RESPONSE;
//        }
//    }
//}
