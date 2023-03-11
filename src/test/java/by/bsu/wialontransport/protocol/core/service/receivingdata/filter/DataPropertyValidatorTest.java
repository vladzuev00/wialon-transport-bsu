package by.bsu.wialontransport.protocol.core.service.receivingdata.filter;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;

import by.bsu.wialontransport.crud.dto.Parameter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DataPropertyValidatorTest extends AbstractContextTest {

    @Autowired
    private DataPropertyValidator validator;

    @Test
    public void amountOfSatellitesShouldBeValid() {
        final Data givenData = Data.builder()
                .amountOfSatellites(3)
                .build();

        final boolean valid = this.validator.isValidAmountOfSatellites(givenData);
        assertTrue(valid);
    }

    @Test
    public void amountOfSatellitesShouldNotBeValidBecauseOfLessThanMinimalAllowable() {
        final Data givenData = Data.builder()
                .amountOfSatellites(2)
                .build();

        final boolean valid = this.validator.isValidAmountOfSatellites(givenData);
        assertFalse(valid);
    }

    @Test
    public void amountOfSatellitesShouldNotBeValidBecauseOfMoreThanMaximalAllowable() {
        final Data givenData = Data.builder()
                .amountOfSatellites(1000)
                .build();

        final boolean valid = this.validator.isValidAmountOfSatellites(givenData);
        assertFalse(valid);
    }

    @Test
    public void dateTimeShouldBeValid() {
        final Data givenData = Data.builder()
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();

        final boolean valid = this.validator.isValidDateTime(givenData);
        assertTrue(valid);
    }

    @Test
    public void dateTimeShouldNotBeValidBecauseOfLessThanMinimalAllowable() {
        final LocalDateTime givenDateTime = LocalDateTime.of(
                1999, 1, 1, 1, 1, 1);
        final Data givenData = Data.builder()
                .date(givenDateTime.toLocalDate())
                .time(givenDateTime.toLocalTime())
                .build();

        final boolean valid = this.validator.isValidDateTime(givenData);
        assertFalse(valid);
    }

    @Test
    public void dateTimeShouldNotBeValidBecauseOfMoreThanMaximalAllowable() {
        final LocalDateTime givenDateTime = LocalDateTime.now().plusSeconds(100);
        final Data givenData = Data.builder()
                .date(givenDateTime.toLocalDate())
                .time(givenDateTime.toLocalTime())
                .build();

        final boolean valid = this.validator.isValidDateTime(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldBeValid() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par123", createDoubleParameter("par123", "4"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertTrue(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfHDOPAbsents() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par123", createDoubleParameter("par123", "4"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfHDOPIsLessThanMinimalAllowable() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "-0.01"),
                                "par123", createDoubleParameter("par123", "4"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfHDOPIsMoreThanMaximalAllowable() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "7.01"),
                                "par123", createDoubleParameter("par123", "4"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfVDOPAbsents() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfVDOPIsLessThanMinimalAllowable() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par123", createDoubleParameter("par123", "-0.01"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfVDOPIsMoreThanMaximalAllowable() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par123", createDoubleParameter("par123", "7.01"),
                                "par124", createDoubleParameter("par124", "5")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfPDOPAbsents() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par123", createDoubleParameter("par123", "4")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfPDOPIsLessThanMinimalAllowable() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par123", createDoubleParameter("par123", "7"),
                                "par124", createDoubleParameter("par124", "-0.01")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    @Test
    public void dopParametersShouldNotBeValidBecauseOfPDOPIsMoreThanMaximalAllowable() {
        final Data givenData = Data.builder()
                .parametersByNames(
                        Map.of(
                                "par122", createDoubleParameter("par122", "3"),
                                "par123", createDoubleParameter("par123", "7"),
                                "par124", createDoubleParameter("par124", "7.1")
                        )
                ).build();

        final boolean valid = this.validator.isValidDOPParameters(givenData);
        assertFalse(valid);
    }

    private static Parameter createDoubleParameter(final String name, final String value) {
        return Parameter.builder()
                .name(name)
                .type(DOUBLE)
                .value(value)
                .build();
    }
}
