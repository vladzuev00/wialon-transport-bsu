package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class LoginNewWingPackageBuilderTest {

    @Test
    public void packageShouldBeBuilt() {
        final LoginNewWingPackageBuilder givenBuilder = new LoginNewWingPackageBuilder();

        final String givenImei = "111112222233333";
        givenBuilder.setImei(givenImei);

        final int givenChecksum = 111;

        final NewWingLoginPackage actual = givenBuilder.build(givenChecksum);
        final NewWingLoginPackage expected = new NewWingLoginPackage(givenChecksum, givenImei);
        assertEquals(expected, actual);
    }
}