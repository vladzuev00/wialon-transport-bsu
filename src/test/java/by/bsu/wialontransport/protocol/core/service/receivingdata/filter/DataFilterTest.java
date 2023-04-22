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
    private DataPropertyValidator propertyValidator;

    @Test
    public void dataShouldBeValidInCaseNotExistingPreviousData() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfDateTimeIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                LocalDateTime.of(1900, 1, 1, 1, 1, 1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfDateTimeIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now().plusHours(1),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfAmountOfSatellitesIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                2,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                1000,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfHDOPParameterAbsents() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfHDOPParameterIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "-0.01"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfHDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "7.01"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfVDOPParameterAbsents() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfVDOPParameterIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "-0.01"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfVDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "7.01"),
                        "124", createDoubleParameter("124", "6")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfPDOPParameterAbsents() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfPDOPParameterIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "-0.01")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseNotExistingPreviousDataBecauseOfPDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
        final Data givenData = createData(
                now(),
                10,
                Map.of(
                        "122", createDoubleParameter("122", "4"),
                        "123", createDoubleParameter("123", "5"),
                        "124", createDoubleParameter("124", "7.01")
                )
        );

        final boolean actual = givenFilter.isValid(givenData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeValidInCaseExistingPreviousData() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isValid(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeValidInCaseExistingPreviousDataBecauseOfIncorrectOrder() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isValid(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfAmountOfSatellitesIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfAmountOfSatellitesIsLessThanMinimalAllowableBecauseFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfAmountOfSatellitesIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfAmountOfSatellitesIsMoreThanMaximalAllowableBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfHDOPParameterAbsents() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfHDOPParameterAbsentsBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfHDOPParameterIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfHDOPParameterIsLessThanMinimalAllowableBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfHDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfHDOPParameterIsMoreThanMaximalAllowableBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfVDOPParameterAbsents() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfVDOPParameterAbsentsBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfVDOPParameterIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedDespiteOfVDOPParameterIsLessThanMinimalAllowableBecauseOfFilteringIsNotEnable() {
        final DataFilter givenFilter = createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfVDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfVDOPParameterIsMoreThanMaximalAllowableBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfPDOPParameterAbsents() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfPDOPParameterAbsentsBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(false);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfPDOPParameterIsLessThanMinimalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    @Test
    public void dataShouldNotBeNeededToBeFixedDespiteOfPDOPParameterIsLessThanMinimalAllowableBecauseOfFilteringIsNotEnabled() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertFalse(actual);
    }

    @Test
    public void dataShouldBeNeededToBeFixedBecauseOfPDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
        assertTrue(actual);
    }

    //TODO: start from this
    @Test
    public void dataShouldNotBeNeededToBeFixedBecauseOfPDOPParameterIsMoreThanMaximalAllowable() {
        final DataFilter givenFilter = this.createFilter(true);
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

        final boolean actual = givenFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
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

//        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
//        assertFalse(actual);
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

//        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
//        assertFalse(actual);
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

//        final boolean actual = this.dataFilter.isNeedToBeFixed(givenCurrentData, givenPreviousData);
//        assertFalse(actual);
    }

    private DataFilter createFilter(final boolean filteringEnable) {
        return new DataFilter(this.propertyValidator, filteringEnable);
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
