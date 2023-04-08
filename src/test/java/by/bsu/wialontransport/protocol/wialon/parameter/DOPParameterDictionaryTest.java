package by.bsu.wialontransport.protocol.wialon.parameter;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary.*;
import static org.junit.Assert.*;

public final class DOPParameterDictionaryTest {

    @Test
    public void stringShouldBeAliasForDOPParameter() {
        final String givenString = "par123";

        assertTrue(VDOP.isAlias(givenString));
    }

    @Test
    public void stringShouldNotBeAliasForDOPParameter() {
        final String givenString = "par123";

        assertFalse(HDOP.isAlias(givenString));
    }

    @Test
    public void dictionaryShouldBeFoundByAlias() {
        final String givenString = "par123";

        final Optional<DOPParameterDictionary> optionalActual = findByAlias(givenString);
        assertTrue(optionalActual.isPresent());

        final DOPParameterDictionary actual = optionalActual.get();
        assertSame(VDOP, actual);
    }

    @Test
    public void dictionaryShouldBeFoundByNotExistingAlias() {
        final String givenString = "wrong";

        final Optional<DOPParameterDictionary> optionalActual = findByAlias(givenString);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void anyAliasShouldBeFound() {
        final String actual = HDOP.findAnyAlias();
        final List<String> allowableValuesOfActual = List.of("par122", "122");
        assertTrue(allowableValuesOfActual.contains(actual));
    }
}
