package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.LoginNewWingPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class LoginNewWingPackageBuilderTest {

    @Test
    public void packageShouldBeBuilt() {
        final LoginNewWingPackageBuilder givenBuilder = new LoginNewWingPackageBuilder();

        final String givenImei = "111112222233333";
        givenBuilder.setImei(givenImei);

        final int givenChecksum = 111;

        final LoginNewWingPackage actual = givenBuilder.build(givenChecksum);
        final LoginNewWingPackage expected = new LoginNewWingPackage(givenChecksum, givenImei);
        assertEquals(expected, actual);
    }
}