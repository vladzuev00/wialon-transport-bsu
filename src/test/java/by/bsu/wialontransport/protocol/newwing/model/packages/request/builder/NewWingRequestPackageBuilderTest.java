package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class NewWingRequestPackageBuilderTest {

    @Test
    public void packageShouldBeBuilt() {
        final NewWingRequestPackageBuilder<?> givenBuilder = new TestNewWingRequestPackageBuilder();

        final int givenChecksum = 400;
        givenBuilder.setChecksum(givenChecksum);

        final NewWingRequestPackage actual = givenBuilder.build();
        final NewWingRequestPackage expected = new TestNewWingRequestPackage(givenChecksum);
        assertEquals(expected, actual);
    }

    private static final class TestNewWingRequestPackage extends NewWingRequestPackage {

        public TestNewWingRequestPackage(final int checksum) {
            super(checksum);
        }
    }

    private static final class TestNewWingRequestPackageBuilder
            extends NewWingRequestPackageBuilder<TestNewWingRequestPackage> {

        @Override
        protected TestNewWingRequestPackage build(final int checksum) {
            return new TestNewWingRequestPackage(checksum);
        }
    }
}