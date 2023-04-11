package by.bsu.wialontransport.protocol.core.service.receivingdata.filter;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DataFilterTest extends AbstractContextTest {

    @Autowired
    private DataFilter dataFilter;

    @Test
    public void dataShouldBeValidInCaseNotExistingPreviousData() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfDateTimeIsLessThanMinimalAllowable() {
        final Data givenData = createData(
                LocalDateTime.of(1900, 1, 1, 1, 1, 1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfDateTimeIsMoreThanMaximalAllowable() {
        final Data givenData = createData(
                now().plusHours(1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfAmountOfSatellitesIsLessThanMinimalAllowable() {
        final Data givenData = createData(
                now(),
                2,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowable() {
        final Data givenData = createData(
                now(),
                1000,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfHDOPParameterAbsents() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfHDOPParameterIsLessThanMinimalAllowable() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "-0.01"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfHDOPParameterIsMoreThanMaximalAllowable() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "7.01"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfVDOPParameterAbsents() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfVDOPParameterIsLessThanMinimalAllowable() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "-0.01"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfVDOPParameterIsMoreThanMaximalAllowable() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "7.01"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfPDOPParameterAbsents() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfPDOPParameterIsLessThanMinimalAllowable() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "-0.01")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfPDOPParameterIsMoreThanMaximalAllowable() {
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "7.01")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeValidInCaseExistingPreviousData() {
        final Data givenPreviousData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                9,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5.1"),
                        "124", createDoubleParameter("124", "6.2")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeValidBecauseOfIncorrectOrder() {
        final LocalDateTime givenDateTimePreviousData = now();
        final Data givenPreviousData = createData(
                givenDateTimePreviousData,
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                givenDateTimePreviousData.minusSeconds(1),
                9,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5.1"),
                        "124", createDoubleParameter("124", "6.2")
                )
        );

        final boolean actual = this.dataFilter.isValid(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfAmountOfSatellitesIsLessThanMinimalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                2,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                1000,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfHDOPParameterAbsents() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfHDOPParameterIsLessThanMinimalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "-0.01"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfHDOPParameterIsMoreThanMaximalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "7.01"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfVDOPParameterAbsents() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "7"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfVDOPParameterIsLessThanMinimalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "-0.01"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfVDOPParameterIsMoreThanMaximalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "7.01"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfPDOPParameterAbsents() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfPDOPParameterIsLessThanMinimalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "4"),
                        "124", createDoubleParameter("124", "-0.01")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfPDOPParameterIsMoreThanMaximalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "7.1")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedBecauseOfDateTimeIsLessThanMinimalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                LocalDateTime.of(1900, 1, 1, 1, 1, 1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedBecauseOfDateTimeIsMoreThanMaximalAllowable() {
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                now().plusHours(1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedBecauseOfNotCorrectOrder() {
        final LocalDateTime givenDateTimePreviousData = now();
        final Data givenPreviousData = createData(
                now(),
                3,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final Data givenCurrentData = createData(
                givenDateTimePreviousData.minusHours(1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    private static Data createData(final LocalDateTime dateTime, final int amountOfSatellites,
                                   final Map<String, Parameter> parameterByNames) {
        return Data.builder()
                .date(dateTime.toLocalDate())
                .time(dateTime.toLocalTime())
                .amountOfSatellites(amountOfSatellites)
                .parametersByNames(parameterByNames)
                .build();
    }

    private static Parameter createDoubleParameter(final String name, final String value) {
        return Parameter.builder()
                .name(name)
                .type(DOUBLE)
                .value(value)
                .build();
    }
}
