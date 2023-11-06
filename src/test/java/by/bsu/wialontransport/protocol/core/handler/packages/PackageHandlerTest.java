package by.bsu.wialontransport.protocol.core.handler.packages;

import by.bsu.wialontransport.protocol.packages.Package;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class PackageHandlerTest {

    @Test
    public void handlerShouldBeAbleToHandle() {
        final TestPackageHandler givenHandler = new TestPackageHandler();
        final Package givenPackage = new TestPackage();

        final boolean actual = givenHandler.isAbleToHandle(givenPackage);
        assertTrue(actual);
    }

    @Test
    public void handlerShouldNotBeAbleToHandle() {
        final TestPackageHandler givenHandler = new TestPackageHandler();
        final Package givenPackage = new Package() {
        };

        final boolean actual = givenHandler.isAbleToHandle(givenPackage);
        assertFalse(actual);
    }

    @Test
    public void packageShouldBeHandled() {
        final TestPackageHandler givenHandler = new TestPackageHandler();
        final Package givenPackage = new TestPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        givenHandler.handle(givenPackage, givenContext);

        assertTrue(givenHandler.isPackageHandled());
    }

    @Test(expected = ClassCastException.class)
    public void packageShouldNotBeHandled() {
        final TestPackageHandler givenHandler = new TestPackageHandler();
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        givenHandler.handle(givenPackage, givenContext);
    }

    private static final class TestPackage implements Package {

    }

    @Setter
    @Getter
    private static final class TestPackageHandler extends PackageHandler<TestPackage> {
        private boolean packageHandled;

        public TestPackageHandler() {
            super(TestPackage.class);
        }

        @Override
        protected void handleConcretePackage(final TestPackage requestPackage, final ChannelHandlerContext context) {
            this.packageHandled = true;
        }
    }
}
