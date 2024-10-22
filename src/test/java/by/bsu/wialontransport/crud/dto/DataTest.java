package by.bsu.wialontransport.crud.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class DataTest {

    @Test
    public void cityNameShouldBeFound() {
        final Location givenData = Location.builder()
                .address(
                        Address.builder()
                                .cityName("city")
                                .build())
                .build();

        final String actual = givenData.findCityName();
        final String expected = "city";
        assertEquals(expected, actual);
    }

    @Test
    public void countryNameShouldBeFound() {
        final Location givenData = Location.builder()
                .address(
                        Address.builder()
                                .countryName("country")
                                .build())
                .build();

        final String actual = givenData.findCountryName();
        final String expected = "country";
        assertEquals(expected, actual);
    }
}
